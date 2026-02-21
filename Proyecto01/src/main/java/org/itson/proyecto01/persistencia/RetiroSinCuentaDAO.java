package org.itson.proyecto01.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.itson.proyecto01.entidades.RetiroSinCuenta;

public class RetiroSinCuentaDAO implements IRetiroSinCuentaDAO {

    private ICuentasDAO cuentasDAO;

    public RetiroSinCuentaDAO(ICuentasDAO cuentasDAO) {
        this.cuentasDAO = cuentasDAO;
    }

    @Override
    public void realizarRetiro(RetiroSinCuenta retiro) throws PersistenciaException {

        Connection conexion = null;

        try {

            conexion = ConexionBD.crearConexion();
            conexion.setAutoCommit(false); 

            String sqlActualizarSaldo = """
                    UPDATE cuentas
                    SET saldo = saldo - ?
                    WHERE numero_cuenta = ?
                    """;

            PreparedStatement psSaldo = conexion.prepareStatement(sqlActualizarSaldo);
            psSaldo.setDouble(1, retiro.getMonto());
            psSaldo.setString(2, retiro.getNumeroCuentaOrigen().toString());
            psSaldo.executeUpdate();
            
            String sqlOperacion = """
                    INSERT INTO OPERACIONES 
                    (tipo_operacion, fecha_hora, monto, id_cuenta)
                    VALUES (?,?,?,?)
                    """;

            PreparedStatement psOperacion = conexion.prepareStatement(sqlOperacion,PreparedStatement.RETURN_GENERATED_KEYS);
            
            psOperacion.setString(1, retiro.getTipoOperacion());
            psOperacion.setTimestamp(2, Timestamp.valueOf(retiro.getFechaHora()));
            psOperacion.setDouble(3, retiro.getMonto());
            String numeroCuenta = retiro.getNumeroCuentaOrigen().toString();
            psOperacion.setInt(4, cuentasDAO.obtenerCuentaporNumeroCuenta(numeroCuenta).getId());

            psOperacion.executeUpdate();

            ResultSet rs = psOperacion.getGeneratedKeys();
            rs.next();
            
            int idTransaccion = rs.getInt(1);
            
            String sqlRetiro = """
                    INSERT INTO RETIROS_SIN_CUENTA
                    (id_transaccion, folio, contrasena, estado, fecha_expiracion)
                    VALUES (?,?,?,?,?)
                    """;

            PreparedStatement psRetiro = conexion.prepareStatement(sqlRetiro);
            psRetiro.setInt(1, idTransaccion);
            psRetiro.setString(2, retiro.getFolio());
            psRetiro.setString(3, retiro.getContrasena());
            psRetiro.setString(4, retiro.getEstado());
            psRetiro.setTimestamp(5, Timestamp.valueOf(retiro.getFechaExpiracion()));

            psRetiro.executeUpdate();

            conexion.commit();
            
            conexion.close();

        } catch (SQLException e) {

            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            throw new PersistenciaException("Error al realizar retiro sin cuenta.", e);
        }
    }
}