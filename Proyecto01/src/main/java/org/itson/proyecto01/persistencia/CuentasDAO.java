package org.itson.proyecto01.persistencia;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import org.itson.proyecto01.dtos.NuevaCuentaDTO;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.enums.EstadoCuenta;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 * <p>
 * Implementación del DAO para la entidad <b>Cuenta</b>. Esta clase encapsula el acceso
 * a datos y las operaciones de persistencia relacionadas con la tabla
 * <code>cuentas</code>, así como el registro de operaciones en la tabla
 * <code>operaciones</code> cuando aplica.
 * </p>
 *
 * <p>
 * Entre sus responsabilidades se incluyen:
 * </p>
 * <ul>
 *   <li>Consultar cuentas (todas o solo activas) de un cliente.</li>
 *   <li>Consultar saldo por número de cuenta.</li>
 *   <li>Dar de alta una cuenta (con transacción y registro de operación).</li>
 *   <li>Actualizar saldo.</li>
 *   <li>Cancelar cuenta.</li>
 *   <li>Consultar una cuenta por número de cuenta.</li>
 *   <li>Generar un número de cuenta único.</li>
 * </ul>
 *
 * @author
 */
public class CuentasDAO implements ICuentasDAO {

    private static final Logger LOGGER = Logger.getLogger(CuentasDAO.class.getName());

    /**
     * <p>
     * Obtiene la lista de cuentas <b>activas</b> asociadas a un cliente.
     * </p>
     *
     * <p>
     * La consulta filtra por:
     * </p>
     * <ul>
     *   <li><code>id_cliente = ?</code></li>
     *   <li><code>estado = 'ACTIVA'</code></li>
     * </ul>
     *
     * @param idCliente identificador del cliente dueño de las cuentas.
     * @return lista de {@link Cuenta} activas; si no hay registros, retorna una lista vacía.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
    @Override
    public List<Cuenta> obtenerCuentasActivas(Integer idCliente) throws PersistenciaException {
        List<Cuenta> listaCuentas = new LinkedList<>();

        try {
            Connection conexion = ConexionBD.crearConexion();
            String codigoSQL = """
                select id_cuenta, numero_cuenta, fecha_apertura, saldo, estado, id_cliente
                from  cuentas
                where id_cliente = ? and estado = 'ACTIVA'
                """;
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);
            comandoSQL.setInt(1, idCliente);
            ResultSet resultados = comandoSQL.executeQuery();

            while (resultados.next()) {
                Integer id = resultados.getInt("id_cuenta");
                String numerocuenta = resultados.getString("numero_cuenta");
                LocalDateTime fechaApertura = resultados.getTimestamp("fecha_apertura").toLocalDateTime();
                double saldo = resultados.getDouble("saldo");
                EstadoCuenta estado = EstadoCuenta.valueOf(resultados.getString("estado"));
                Integer codigoCliente = resultados.getInt("id_cliente");

                Cuenta cuenta = new Cuenta(
                        id,
                        numerocuenta,
                        fechaApertura,
                        saldo,
                        estado,
                        codigoCliente
                );

                listaCuentas.add(cuenta);
            }

            conexion.close();
            return listaCuentas;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo Consultar las Cuentas 'Activas' ", ex);
        }
    }

    /**
     * <p>
     * Obtiene la lista de <b>todas</b> las cuentas asociadas a un cliente, sin filtrar por estado.
     * </p>
     *
     * @param idCliente identificador del cliente dueño de las cuentas.
     * @return lista de {@link Cuenta} del cliente; si no hay registros, retorna una lista vacía.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
    @Override
    public List<Cuenta> obtenerCuentas(Integer idCliente) throws PersistenciaException {
        List<Cuenta> listaCuentas = new LinkedList<>();

        try {
            Connection conexion = ConexionBD.crearConexion();
            String codigoSQL = """
                select id_cuenta, numero_cuenta, fecha_apertura, saldo, estado, id_cliente
                from cuentas
                where id_cliente = ? 
                """;
            PreparedStatement comandoSQL = conexion.prepareStatement(codigoSQL);
            comandoSQL.setInt(1, idCliente);
            ResultSet resultados = comandoSQL.executeQuery();

            while (resultados.next()) {
                Integer id = resultados.getInt("id_cuenta");
                String numerocuenta = resultados.getString("numero_cuenta");
                LocalDateTime fechaApertura = resultados.getTimestamp("fecha_apertura").toLocalDateTime();
                double saldo = resultados.getDouble("saldo");
                EstadoCuenta estado = EstadoCuenta.valueOf(resultados.getString("estado"));
                Integer codigoCliente = resultados.getInt("id_cliente");

                Cuenta cuenta = new Cuenta(
                        id,
                        numerocuenta,
                        fechaApertura,
                        saldo,
                        estado,
                        codigoCliente
                );

                listaCuentas.add(cuenta);
            }

            conexion.close();
            return listaCuentas;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo Consultar las Cuentas 'Activas' ", ex);
        }
    }

    /**
     * <p>
     * Obtiene el saldo asociado a un número de cuenta.
     * </p>
     *
     * @param numeroCuenta número de cuenta a buscar.
     * @return el saldo de la cuenta si existe; si no existe la cuenta, retorna <code>null</code>.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
    @Override
    public Double obtenerSaldoPorNumeroCuenta(String numeroCuenta) throws PersistenciaException {

        try {
            Connection conexion = ConexionBD.crearConexion();

            String codigoSQL = """
                select saldo
                from cuentas
                where numero_cuenta = ?
                """;

            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            comando.setString(1, numeroCuenta);

            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
                return resultado.getDouble("saldo");
            }

            conexion.close();
            return null;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo obtener el saldo", ex);
        }
    }

    /**
     * <p>
     * Da de alta una nueva cuenta para un cliente y registra una operación de tipo
     * <code>ALTA_CUENTA</code> en la tabla <code>operaciones</code>.
     * </p>
     *
     * <p>
     * Se ejecuta dentro de una <b>transacción</b>:
     * </p>
     * <ul>
     *   <li>Inserta el registro en <code>cuentas</code>.</li>
     *   <li>Obtiene el <code>id_cuenta</code> generado.</li>
     *   <li>Inserta el registro correspondiente en <code>operaciones</code>.</li>
     *   <li>Confirma con <code>commit()</code>.</li>
     *   <li>Si falla algo, ejecuta <code>rollback()</code>.</li>
     * </ul>
     *
     * @param nuevaCuenta DTO con la información necesaria para crear la cuenta.
     * @return objeto {@link Cuenta} con el ID generado y datos persistidos.
     * @throws PersistenciaException si ocurre un error en cualquiera de los inserts o en la transacción.
     */
    @Override
    public Cuenta altaCuenta(NuevaCuentaDTO nuevaCuenta) throws PersistenciaException {

        Connection conexion = null;
        try {
            conexion = ConexionBD.crearConexion();

            String codigoSQLCuenta = """
                                    insert into cuentas(numero_cuenta, fecha_apertura,
                                                      saldo, estado, id_cliente)
                                    values (?, ?, ?, ?, ?);
                                   """;

            String codigoSQLOperacion = """
                                      insert into operaciones (tipo_operacion, fecha_hora, monto, id_cuenta)
                                      values (?,?,?,?);
                                      """;

            conexion.setAutoCommit(false); //taransaccion 

            int idCuentaGenerada; //para guardar el id de la cuenta que se creo y usarlo en la operacion

            PreparedStatement comandoCuenta = conexion.prepareStatement(codigoSQLCuenta, Statement.RETURN_GENERATED_KEYS);

            String numeroCuentaGenerado = generarNumeroCuenta(conexion);
            //insertar la nueva cuenta en la tabla cuentas
            comandoCuenta.setString(1, numeroCuentaGenerado);
            comandoCuenta.setTimestamp(2, Timestamp.valueOf(nuevaCuenta.getFechaApertura()));
            comandoCuenta.setDouble(3, nuevaCuenta.getSaldo());
            comandoCuenta.setString(4, nuevaCuenta.getEstado().name());
            comandoCuenta.setInt(5, nuevaCuenta.getIdCliente());

            comandoCuenta.executeUpdate();

            ResultSet resultado = comandoCuenta.getGeneratedKeys();
            if (resultado.next()) {
                idCuentaGenerada = resultado.getInt(1);
            } else {
                throw new PersistenciaException("No se pudo obtener el id_cuenta generado.", null);
            }

            //insertar la operacion de alte de cuenta de la cuenta que acabamos de crear
            PreparedStatement psOperacion = conexion.prepareStatement(codigoSQLOperacion);
            psOperacion.setString(1, TipoOperacion.ALTA_CUENTA.name());
            psOperacion.setTimestamp(2, Timestamp.valueOf(nuevaCuenta.getFechaApertura()));
            psOperacion.setDouble(3, nuevaCuenta.getSaldo());
            psOperacion.setInt(4, idCuentaGenerada);
            psOperacion.executeUpdate();

            // confirmar transaccion
            conexion.commit();
            LOGGER.info("Cuenta creada correctamente con operación de alta.");

            // cear objeto Cuenta 
            return new Cuenta(
                    idCuentaGenerada,
                    numeroCuentaGenerado,
                    nuevaCuenta.getFechaApertura(),
                    nuevaCuenta.getSaldo(),
                    nuevaCuenta.getEstado(),
                    nuevaCuenta.getIdCliente()
            );

        } catch (SQLException ex) {
            if (conexion != null) {
                try {
                    conexion.rollback(); //si uno de los insert no se pudo hacer hace que el otro se borre
                    LOGGER.warning("Rollback ejecutado por error en alta de cuenta.");
                } catch (SQLException rollbackEx) {
                    LOGGER.severe("Error al hacer rollback: " + rollbackEx.getMessage());
                }
            }
            throw new PersistenciaException("No se pudo crear la cuenta.", ex);
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException ex) {
                    LOGGER.severe("Error al cerrar la conexión: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * <p>
     * Actualiza el saldo de una cuenta por su identificador.
     * </p>
     *
     * @param idCuenta identificador de la cuenta a modificar.
     * @param nuevoSaldo nuevo saldo a establecer.
     * @throws PersistenciaException si ocurre un error al ejecutar el <code>UPDATE</code>.
     */
    @Override
    public void actualizarSaldo(Integer idCuenta, double nuevoSaldo) throws PersistenciaException {

        try {
            Connection conexion = ConexionBD.crearConexion();

            String codigoSQL = """
                update cuentas
                set saldo = ?
                where id_cuenta = ?
                """;

            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            comando.setDouble(1, nuevoSaldo);
            comando.setInt(2, idCuenta);

            comando.executeUpdate();
            conexion.close();

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo actualizar el saldo", ex);
        }
    }

    /**
     * <p>
     * Cancela una cuenta cambiando su estado a <code>CANCELADA</code>.
     * </p>
     *
     * @param idCuenta identificador de la cuenta a cancelar.
     * @throws PersistenciaException si ocurre un error al ejecutar el <code>UPDATE</code>.
     */
    @Override
    public void cancelarCuenta(Integer idCuenta) throws PersistenciaException {

        try {
            Connection conexion = ConexionBD.crearConexion();

            String codigoSQL = """
                update cuentas
                set estado = 'CANCELADA'
                where id_cuenta = ?
                """;

            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            comando.setInt(1, idCuenta);

            comando.executeUpdate();
            conexion.close();

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo cancelar la cuenta", ex);
        }
    }

    /**
     * <p>
     * Obtiene una cuenta por su número de cuenta.
     * </p>
     *
     * @param numeroCuenta número de cuenta a buscar.
     * @return objeto {@link Cuenta} si existe el registro.
     * @throws PersistenciaException si la cuenta no existe, si el número es incorrecto
     *                              o si ocurre un error al consultar.
     */
    @Override
    public Cuenta obtenerCuentaporNumeroCuenta(String numeroCuenta) throws PersistenciaException {
        try {
            Connection conexion = ConexionBD.crearConexion();

            String codigoSQL = """
                select id_cuenta, numero_cuenta, fecha_apertura, saldo, estado, id_cliente
                from cuentas
                where numero_cuenta = ?
                """;

            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            comando.setString(1, numeroCuenta);

            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
                Integer id = resultado.getInt("id_cuenta");
                String numerocuenta = resultado.getString("numero_cuenta");
                LocalDateTime fechaApertura = resultado.getTimestamp("fecha_apertura").toLocalDateTime();
                double saldo = resultado.getDouble("saldo");
                EstadoCuenta estado = EstadoCuenta.valueOf(resultado.getString("estado"));
                Integer codigoCliente = resultado.getInt("id_cliente");

                Cuenta cuentaNueva = new Cuenta(id, numerocuenta, fechaApertura, saldo, estado, codigoCliente);
                conexion.close();
                return cuentaNueva;

            } else {
                conexion.close();
                throw new PersistenciaException("La cuenta no existe o el número es incorrecto.", null);

            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo obtener la cuenta", ex);
        }
    }

    /**
     * <p>
     * Genera un número de cuenta aleatorio de 18 dígitos y verifica que no exista en la base de datos.
     * </p>
     *
     * <p>
     * El método repite la generación hasta que el número sea único, consultando:
     * </p>
     * <ul>
     *   <li><code>SELECT COUNT(id_cuenta) FROM Cuentas WHERE numero_cuenta = ?</code></li>
     * </ul>
     *
     * @param conexion conexión activa a la base de datos (usada para validar unicidad).
     * @return número de cuenta único de 18 dígitos.
     * @throws SQLException si ocurre un error durante la validación en base de datos.
     */
    private String generarNumeroCuenta(Connection conexion) throws SQLException {
        Random random = new Random();
        String numeroCuenta;
        boolean existe;

        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 18; i++) {
                sb.append(random.nextInt(10));
            }
            numeroCuenta = sb.toString();

            String sqlCheck = "SELECT COUNT(id_cuenta) FROM Cuentas WHERE numero_cuenta = ?";
            PreparedStatement comando = conexion.prepareStatement(sqlCheck);
            comando.setString(1, numeroCuenta);
            ResultSet resultado = comando.executeQuery();
            resultado.next();
            existe = resultado.getInt(1) > 0;
            resultado.close();
            comando.close();

        } while (existe);

        return numeroCuenta;
    }

}