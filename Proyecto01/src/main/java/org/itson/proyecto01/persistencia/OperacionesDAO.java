/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.itson.proyecto01.entidades.Operacion;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 * <p>
 * Implementación del DAO para la entidad <b>Operación</b>. Esta clase concentra el acceso
 * a datos para consultar transacciones/operaciones registradas en la tabla
 * <code>Operaciones</code>, relacionándolas con la tabla <code>Cuentas</code>
 * para filtrar por cliente.
 * </p>
 *
 * <p>
 * Permite consultar operaciones:
 * </p>
 * <ul>
 *   <li>De un cliente (todas).</li>
 *   <li>Filtradas por tipo de operación.</li>
 *   <li>Filtradas por rango de fechas.</li>
 *   <li>Filtradas por tipo y rango de fechas.</li>
 * </ul>
 *
 * <p>
 * Los registros leídos se transforman a objetos {@link Operacion} utilizando:
 * </p>
 * <ul>
 *   <li><code>TipoOperacion.valueOf(String)</code> para mapear el tipo.</li>
 *   <li><code>getTimestamp(...).toLocalDateTime()</code> para mapear la fecha y hora.</li>
 * </ul>
 *
 * @author Jesus Omar
 */
public class OperacionesDAO implements IOperacionesDAO {

    private static final Logger LOGGER = Logger.getLogger(OperacionesDAO.class.getName());

    /**
     * <p>
     * Obtiene todas las operaciones registradas para un cliente.
     * </p>
     *
     * <p>
     * Realiza un <code>INNER JOIN</code> entre <code>Operaciones</code> y <code>Cuentas</code>
     * para filtrar por <code>c.id_cliente</code>.
     * </p>
     *
     * @param idCliente identificador del cliente del cual se desea consultar operaciones.
     * @return lista de {@link Operacion} asociadas al cliente; si no hay registros, retorna lista vacía.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
    @Override
    public List<Operacion> obtenerOperaciones(Integer idCliente) throws PersistenciaException {
        List<Operacion> listaOperaciones = new LinkedList<>();
        try {
            Connection conexion = ConexionBD.crearConexion();
            String codigoSQL = """
                                SELECT o.id_transaccion, o.tipo_operacion, o.fecha_hora, o.monto, c.numero_cuenta
                                FROM Operaciones o
                                INNER JOIN Cuentas c ON o.id_cuenta = c.id_cuenta
                                WHERE c.id_cliente = ?;
                                """;
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);
            comandoSQL.setInt(1, idCliente);
            ResultSet resultados = comandoSQL.executeQuery();

            while (resultados.next()) {
                int idOperacion = resultados.getInt("id_transaccion");
                String tipo = resultados.getString("tipo_operacion");
                TipoOperacion tipoOperacion = TipoOperacion.valueOf(tipo);
                LocalDateTime fechaHoraOperacion = resultados.getTimestamp("fecha_hora").toLocalDateTime();
                double monto = resultados.getDouble("monto");
                String numeroCuenta = resultados.getString("numero_cuenta");

                Operacion operacion = new Operacion(
                        idOperacion,
                        fechaHoraOperacion,
                        tipoOperacion,
                        monto,
                        numeroCuenta
                );

                listaOperaciones.add(operacion);
            }

            conexion.close();
            return listaOperaciones;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al buscar las operaciones", null);
        }
    }

    /**
     * <p>
     * Obtiene las operaciones de un cliente filtradas por un tipo de operación específico.
     * </p>
     *
     * <p>
     * El filtro se aplica sobre <code>o.tipo_operacion</code>.
     * </p>
     *
     * @param idCliente identificador del cliente.
     * @param filtro tipo de operación a consultar.
     * @return lista de {@link Operacion} que coinciden con el tipo; si no hay, retorna lista vacía.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
    @Override
    public List<Operacion> operacionesPorTipo(Integer idCliente, TipoOperacion filtro) throws PersistenciaException {
        List<Operacion> listaOperaciones = new LinkedList<>();
        try {
            Connection conexion = ConexionBD.crearConexion();
            String codigoSQL = """
                                SELECT o.id_transaccion, o.tipo_operacion, o.fecha_hora, o.monto, c.numero_cuenta
                                FROM Operaciones o
                                INNER JOIN Cuentas c ON o.id_cuenta = c.id_cuenta
                                WHERE c.id_cliente = ? AND o.tipo_operacion = ?;
                                """;
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);
            comandoSQL.setInt(1, idCliente);
            comandoSQL.setString(2, filtro.name());
            ResultSet resultados = comandoSQL.executeQuery();

            while (resultados.next()) {
                int idOperacion = resultados.getInt("id_transaccion");
                String tipo = resultados.getString("tipo_operacion");
                TipoOperacion tipoOperacion = TipoOperacion.valueOf(tipo);
                LocalDateTime fechaHoraOperacion = resultados.getTimestamp("fecha_hora").toLocalDateTime();
                double monto = resultados.getDouble("monto");
                String numeroCuenta = resultados.getString("numero_cuenta");

                Operacion operacion = new Operacion(
                        idOperacion,
                        fechaHoraOperacion,
                        tipoOperacion,
                        monto,
                        numeroCuenta
                );

                listaOperaciones.add(operacion);
            }

            conexion.close();
            return listaOperaciones;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al buscar las operaciones", null);
        }
    }

    /**
     * <p>
     * Obtiene las operaciones de un cliente dentro de un rango de fechas.
     * </p>
     *
     * <p>
     * Aplica el filtro con <code>BETWEEN ? AND ?</code> sobre la columna <code>o.fecha_hora</code>.
     * </p>
     *
     * @param idCliente identificador del cliente.
     * @param fechaInicio fecha/hora inicial del rango (inclusive).
     * @param fechaFin fecha/hora final del rango (inclusive).
     * @return lista de {@link Operacion} dentro del rango; si no hay, retorna lista vacía.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
    @Override
    public List<Operacion> operacionesPorFecha(Integer idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws PersistenciaException {
        List<Operacion> listaOperaciones = new LinkedList<>();
        try {
            Connection conexion = ConexionBD.crearConexion();
            String codigoSQL = """
                                SELECT o.id_transaccion, o.tipo_operacion, o.fecha_hora, o.monto, c.numero_cuenta
                                FROM Operaciones o
                                INNER JOIN Cuentas c ON o.id_cuenta = c.id_cuenta
                                WHERE c.id_cliente = ? AND o.fecha_hora BETWEEN ? AND ?;
                                """;
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);
            comandoSQL.setInt(1, idCliente);
            comandoSQL.setObject(2, fechaInicio);
            comandoSQL.setObject(3, fechaFin);
            ResultSet resultados = comandoSQL.executeQuery();

            while (resultados.next()) {
                int idOperacion = resultados.getInt("id_transaccion");
                String tipo = resultados.getString("tipo_operacion");
                TipoOperacion tipoOperacion = TipoOperacion.valueOf(tipo);
                LocalDateTime fechaHoraOperacion = resultados.getTimestamp("fecha_hora").toLocalDateTime();
                double monto = resultados.getDouble("monto");
                String numeroCuenta = resultados.getString("numero_cuenta");

                Operacion operacion = new Operacion(
                        idOperacion,
                        fechaHoraOperacion,
                        tipoOperacion,
                        monto,
                        numeroCuenta
                );

                listaOperaciones.add(operacion);
            }

            conexion.close();
            return listaOperaciones;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al buscar las operaciones", null);
        }
    }

    /**
     * <p>
     * Obtiene las operaciones de un cliente filtradas por tipo y por un rango de fechas.
     * </p>
     *
     * <p>
     * Aplica dos filtros:
     * </p>
     * <ul>
     *   <li><code>o.tipo_operacion = ?</code></li>
     *   <li><code>o.fecha_hora BETWEEN ? AND ?</code></li>
     * </ul>
     *
     * @param idCliente identificador del cliente.
     * @param filtroTipo tipo de operación a consultar.
     * @param fechaInicio fecha/hora inicial del rango (inclusive).
     * @param fechaFin fecha/hora final del rango (inclusive).
     * @return lista de {@link Operacion} que cumplan ambos filtros; si no hay, retorna lista vacía.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
    @Override
    public List<Operacion> operacionesPorFechaTipo(Integer idCliente, TipoOperacion filtroTipo, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws PersistenciaException {
        List<Operacion> listaOperaciones = new LinkedList<>();
        try {
            Connection conexion = ConexionBD.crearConexion();
            String codigoSQL = """
                                SELECT o.id_transaccion, o.tipo_operacion, o.fecha_hora, o.monto, c.numero_cuenta
                                FROM Operaciones o
                                INNER JOIN Cuentas c ON o.id_cuenta = c.id_cuenta
                                WHERE c.id_cliente = ?
                                AND o.tipo_operacion = ?
                                AND o.fecha_hora BETWEEN ? AND ?;
                                """;
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);
            comandoSQL.setInt(1, idCliente);
            comandoSQL.setString(2, filtroTipo.name());
            comandoSQL.setObject(3, fechaInicio);
            comandoSQL.setObject(4, fechaFin);
            ResultSet resultados = comandoSQL.executeQuery();

            while (resultados.next()) {
                int idOperacion = resultados.getInt("id_transaccion");
                String tipo = resultados.getString("tipo_operacion");
                TipoOperacion tipoOperacion = TipoOperacion.valueOf(tipo);
                LocalDateTime fechaHoraOperacion = resultados.getTimestamp("fecha_hora").toLocalDateTime();
                double monto = resultados.getDouble("monto");
                String numeroCuenta = resultados.getString("numero_cuenta");

                Operacion operacion = new Operacion(
                        idOperacion,
                        fechaHoraOperacion,
                        tipoOperacion,
                        monto,
                        numeroCuenta
                );

                listaOperaciones.add(operacion);
            }

            conexion.close();
            return listaOperaciones;
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("Error al buscar las operaciones", null);
        }
    }

}