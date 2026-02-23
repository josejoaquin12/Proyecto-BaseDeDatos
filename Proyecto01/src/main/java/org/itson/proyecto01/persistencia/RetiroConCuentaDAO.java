package org.itson.proyecto01.persistencia;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.Retiro;

/**
 * <p>
 * DAO encargado de la persistencia para retiros <b>sin cuenta</b> (retiros por folio/contraseña),
 * pero vinculados a una <b>cuenta origen</b> mediante el registro en la tabla
 * <code>operaciones</code>.
 * </p>
 *
 * <p>
 * Este DAO utiliza un {@link ICuentasDAO} para:
 * </p>
 * <ul>
 *   <li>Buscar la cuenta origen por número de cuenta.</li>
 *   <li>Obtener su identificador y asociarlo a la operación.</li>
 * </ul>
 *
 * <p>
 * Operaciones soportadas:
 * </p>
 * <ul>
 *   <li>Registrar un retiro sin cuenta (inserta en <code>operaciones</code> y <code>retirossincuenta</code>).</li>
 *   <li>Buscar un retiro por folio y contraseña.</li>
 *   <li>Cobrar un retiro mediante un procedimiento almacenado.</li>
 * </ul>
 */
public class RetiroConCuentaDAO implements IRetiroConCuentaDAO {

    /**
     * DAO utilizado para consultar datos de cuentas (por ejemplo, obtener
     * la cuenta origen por número de cuenta).
     */
    private ICuentasDAO cuentasDAO;

    /**
     * <p>
     * Crea una nueva instancia del DAO de retiros con cuenta, recibiendo el DAO
     * de cuentas por inyección de dependencia.
     * </p>
     *
     * @param cuentasDAO implementación de {@link ICuentasDAO} a utilizar para consultar cuentas.
     */
    public RetiroConCuentaDAO(ICuentasDAO cuentasDAO) {
        this.cuentasDAO = cuentasDAO;
    }

    /**
     * <p>
     * Registra un retiro sin cuenta en la base de datos.
     * </p>
     *
     * <p>
     * Flujo general:
     * </p>
     * <ul>
     *   <li>Inserta una fila en <code>operaciones</code> (tipo, fecha, monto y cuenta origen).</li>
     *   <li>Recupera el <code>id_transaccion</code> generado.</li>
     *   <li>Inserta una fila en <code>retirossincuenta</code> asociándola con ese <code>id_transaccion</code>.</li>
     * </ul>
     *
     * <p>
     * Nota: el <code>id_cuenta</code> se obtiene consultando la cuenta origen a partir de
     * <code>retiro.getNumeroCuentaOrigen()</code>.
     * </p>
     *
     * @param retiro objeto {@link Retiro} con la información necesaria para registrar el retiro
     *              (folio, contraseña, fecha expiración, estado, monto, fecha/hora y cuenta origen).
     * @throws PersistenciaException si ocurre un error al insertar en base de datos.
     */
    @Override
    public void realizarRetiro(Retiro retiro) throws PersistenciaException {
        try {
            Connection conexion = ConexionBD.crearConexion();

            String sqlOperacion = """
                    insert into operaciones 
                    (tipo_operacion, fecha_hora, monto, id_cuenta)
                    VALUES (?,?,?,?)
                    """;

            PreparedStatement psOperacion = conexion.prepareStatement(sqlOperacion, PreparedStatement.RETURN_GENERATED_KEYS);

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

    /**
     * <p>
     * Busca un retiro sin cuenta por <b>folio</b> y <b>contraseña</b>.
     * </p>
     *
     * <p>
     * La consulta relaciona:
     * </p>
     * <ul>
     *   <li><code>retirossincuenta</code> (folio, estado, expiración)</li>
     *   <li><code>operaciones</code> (monto)</li>
     *   <li><code>cuentas</code> (número de cuenta origen)</li>
     * </ul>
     *
     * @param folio folio del retiro.
     * @param contrasena contraseña asociada al retiro.
     * @return un objeto {@link Retiro} con los datos encontrados; si no existe, retorna <code>null</code>.
     * @throws PersistenciaException si ocurre un error al consultar la base de datos.
     */
    @Override
    public Retiro buscarPorFolioYContrasena(String folio, String contrasena) throws PersistenciaException {

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

    /**
     * <p>
     * Cobra (liquida) un retiro sin cuenta, delegando la lógica a un procedimiento almacenado.
     * </p>
     *
     * <p>
     * Ejecuta el procedimiento:
     * </p>
     * <ul>
     *   <li><code>CALL CobrarRetiroSinCuenta(?)</code></li>
     * </ul>
     *
     * @param retiro objeto {@link Retiro} del cual se tomará el folio para realizar el cobro.
     * @throws PersistenciaException si ocurre un error al ejecutar el procedimiento almacenado.
     */
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