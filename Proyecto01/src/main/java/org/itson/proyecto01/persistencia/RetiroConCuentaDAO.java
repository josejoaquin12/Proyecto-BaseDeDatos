package org.itson.proyecto01.persistencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.Retiro;

public class RetiroConCuentaDAO implements IRetiroConCuentaDAO {

    private ICuentasDAO cuentasDAO;

    public RetiroConCuentaDAO(ICuentasDAO cuentasDAO) {
        this.cuentasDAO = cuentasDAO;
    }

    @Override
    public void realizarRetiro(Retiro retiro) throws PersistenciaException {
        try {
            Connection conexion = ConexionBD.crearConexion();
            
            String sqlOperacion = """
                    insert into operaciones 
                    (tipo_operacion, fecha_hora, monto, id_cuenta)
                    VALUES (?,?,?,?)
                    """;

            PreparedStatement psOperacion = conexion.prepareStatement(sqlOperacion,PreparedStatement.RETURN_GENERATED_KEYS);
            
            psOperacion.setString(1, retiro.getTipoOperacion());
            psOperacion.setTimestamp(2, Timestamp.valueOf(retiro.getFechaHora()));
            psOperacion.setDouble(3, retiro.getMonto()); 
            
            Cuenta cuenta = cuentasDAO.obtenerCuentaporNumeroCuenta(retiro.getNumeroCuentaOrigen());            
            psOperacion.setInt(4, cuenta.getId());

            psOperacion.executeUpdate();

            ResultSet rs = psOperacion.getGeneratedKeys();
            rs.next();
            
            int idTransaccion = rs.getInt(1);
            
            String sqlRetiro = """
                    insert into retirossincuenta
                    (id_transaccion, folio, contrasena, fecha_expiracion, estado)
                    VALUES (?,?,?,?,?)
                    """;

            PreparedStatement psRetiro = conexion.prepareStatement(sqlRetiro);
            psRetiro.setInt(1, idTransaccion);
            psRetiro.setString(2, retiro.getFolio());
            psRetiro.setString(3, retiro.getContrasena());
            psRetiro.setTimestamp(4, Timestamp.valueOf(retiro.getFechaExpiracion()));
            psRetiro.setString(5, retiro.getEstado());
            psRetiro.executeUpdate();         
            conexion.close();

        } catch (SQLException e) {
            throw new PersistenciaException(" :Error al realizar retiro sin cuenta.", e);
        }
    }

    @Override
    public Retiro buscarPorFolioYContrasena(String folio, String contrasena)throws PersistenciaException {

        try {
            Connection conexion = ConexionBD.crearConexion();

            String sql = """
                SELECT r.folio, r.estado, r.fecha_expiracion,
                       o.monto, c.numero_cuenta
                FROM retirossincuenta r
                JOIN Operaciones o ON r.id_transaccion = o.id_transaccion
                JOIN Cuentas c ON o.id_cuenta = c.id_cuenta
                WHERE r.folio = ? AND r.contrasena = ?
            """;

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, folio);
            ps.setString(2, contrasena);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Retiro retiro = new Retiro();
                retiro.setFolio(rs.getString("folio"));
                retiro.setEstado(rs.getString("estado"));
                retiro.setFechaExpiracion(rs.getTimestamp("fecha_expiracion").toLocalDateTime());
                retiro.setMonto(rs.getDouble("monto"));
                retiro.setNumeroCuentaOrigen(rs.getString("numero_cuenta"));
                return retiro;
            }

            conexion.close();
            return null;

        } catch (SQLException ex) {
            throw new PersistenciaException(" :Error al buscar retiro", ex);
        }
    }
    @Override
    public void cobrarRetiro(Retiro retiro) throws PersistenciaException {
        try {
            Connection conexion = ConexionBD.crearConexion();

            CallableStatement cs = conexion.prepareCall(
                "{ CALL CobrarRetiroSinCuenta(?) }"
            );
            cs.setString(1, retiro.getFolio());

            cs.execute();
            conexion.close();

        } catch (SQLException ex) {
            throw new PersistenciaException(" :Error al cobrar retiro", ex);
        }
    }
    }