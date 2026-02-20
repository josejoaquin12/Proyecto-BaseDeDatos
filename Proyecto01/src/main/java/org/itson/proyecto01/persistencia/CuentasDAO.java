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

public abstract class CuentasDAO implements ICuentasDAO {

    private static final Logger LOGGER = Logger.getLogger(CuentasDAO.class.getName());

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

    @Override
    public Cuenta altaCuenta(NuevaCuentaDTO nuevaCuenta) throws PersistenciaException {

        Connection conexion = null;
        try {
            conexion = ConexionBD.crearConexion();

            String codigoSQLCuenta = """
                                    insert into Cuentas(numero_cuenta, fecha_apertura,
                                                      saldo, estado, id_cliente)
                                    values (?, ?, ?, ?, ?);
                                   """;

            String codigoSQLOperacion = """
                                      insert into Operaciones (tipo_operacion, fecha_hora, monto, id_cuenta)
                                      values (?,?,?,?);
                                      """;

            conexion.setAutoCommit(false); //taransaccion 

            int idCuentaGenerada; //para guardar el id de la cuenta que se creo y usarlo en la operacion

            PreparedStatement comandoCuenta = conexion.prepareStatement(codigoSQLCuenta, Statement.RETURN_GENERATED_KEYS);

            //insertar la nueva cuenta en la tabla cuentas
            comandoCuenta.setString(1, generarNumeroCuenta(conexion));
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
            psOperacion.setString(1, TipoOperacion.ALTA_DE_CUENTA.name());
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
                    nuevaCuenta.getNumeroCuenta(),
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
            throw new PersistenciaException("No se pudo obtener el saldo", ex);
        }
    }

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
