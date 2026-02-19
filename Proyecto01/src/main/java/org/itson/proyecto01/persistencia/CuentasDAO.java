package org.itson.proyecto01.persistencia;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import org.itson.proyecto01.dtos.NuevaCuentaDTO;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.enums.EstadoCuenta;

public abstract class CuentasDAO implements ICuentasDAO {

    private static final Logger LOGGER = Logger.getLogger(CuentasDAO.class.getName());

    @Override
    public List<Cuenta> obtenerCuentasActivas(Integer idCliente)throws PersistenciaException {
        List<Cuenta> listaCuentas = new LinkedList<>();
        
        try {
            Connection conexion = ConexionBD.crearConexion();
            String codigoSQL = """
                select id_cuenta, numero_cuenta, fecha_apertura, saldo, estado, id_cliente
                form Cuenta
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
    public List<Cuenta> obtenerCuentas(Integer idCliente)throws PersistenciaException {
        List<Cuenta> listaCuentas = new LinkedList<>();
        
        try {
            Connection conexion = ConexionBD.crearConexion();
            String codigoSQL = """
                select id_cuenta, numero_cuenta, fecha_apertura, saldo, estado, id_cliente
                form Cuenta
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
    public Double obtenerSaldoPorNumeroCuenta(String numeroCuenta)
            throws PersistenciaException {

        try {
            Connection conexion = ConexionBD.crearConexion();

            String codigoSQL = """
                select saldo
                from Cuenta
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

//    @Override
//    public Cuenta altaCuenta(NuevaCuentaDTO nuevacuenta)throws PersistenciaException {
//
//        try {
//            Connection conexion = ConexionBD.crearConexion();
//
//            String codigoSQL = """
//                insert Integero Cuenta(numero_cuenta, fecha_apertura,
//                                   saldo, estado, id_cliente)
//                values (?, ?, ?, ?, ?)
//                """;
//
//            PreparedStatement comando = conexion.prepareStatement(codigoSQL,PreparedStatement.RETURN_GENERATED_KEYS);
//
//            comando.setString(1, nuevacuenta.getNumeroCuenta());
//            comando.setTimestamp(2,
//                    Timestamp.valueOf(nuevacuenta.getFechaApertura()));
//            comando.setDouble(3, nuevacuenta.getSaldo());
//            comando.setString(4, nuevacuenta.getEstado().name());
//            comando.setInt(5, nuevacuenta.getIdCliente());
//
//            comando.executeUpdate();
//
//            conexion.close();
//            LOGGER.info("Cuenta creada correctamente");
//
//            return cuenta;
//
//        } catch (SQLException ex) {
//            LOGGER.severe(ex.getMessage());
//            throw new PersistenciaException("No se pudo crear la cuenta", ex);
//        }
//    }
    //TODO

    @Override
    public void actualizarSaldo(Integer idCuenta, double nuevoSaldo)throws PersistenciaException {

        try {
            Connection conexion = ConexionBD.crearConexion();

            String codigoSQL = """
                update Cuenta
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
                update Cuenta
                set estado = 'CANCELADA'
                where id_cuenta = ?
                """;

            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            comando.setInt(1, idCuenta);

            comando.executeUpdate();
            conexion.close();

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException( "No se pudo cancelar la cuenta", ex);
        }
    }
}

