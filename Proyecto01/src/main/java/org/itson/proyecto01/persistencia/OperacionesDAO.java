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
 *
 * @author Jesus Omar
 */
public class OperacionesDAO implements IOperacionesDAO {

    private static final Logger LOGGER = Logger.getLogger(OperacionesDAO.class.getName());

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

    @Override
    public List<Operacion> operacionesPorTipo(Integer idCliente, String filtro) throws PersistenciaException {
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
            comandoSQL.setString(2, filtro);
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

    @Override
    public List<Operacion> operacionesPorFecha(Integer idCliente, String fechaInicio, String fechaFin) throws PersistenciaException {
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
            comandoSQL.setString(2, fechaInicio);
            comandoSQL.setString(3, fechaFin);
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

    @Override
    public List<Operacion> operacionesPorFechaTipo(Integer idCliente, String filtroTipo, String fechaInicio, String fechaFin) throws PersistenciaException {
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
            comandoSQL.setString(2, filtroTipo);
            comandoSQL.setString(3, fechaInicio);
            comandoSQL.setString(4, fechaFin);
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