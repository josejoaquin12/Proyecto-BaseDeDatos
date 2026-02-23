
package org.itson.proyecto01.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;
import org.itson.proyecto01.dtos.NuevaTransferenciaDTO;
import org.itson.proyecto01.entidades.Transferencia;

/**
 * <p>
 * Implementación del DAO para el registro de <b>transferencias</b> entre cuentas.
 * </p>
 *
 * <p>
 * Esta clase se encarga de:
 * </p>
 * <ul>
 *   <li>Actualizar saldos en la cuenta origen (resta).</li>
 *   <li>Actualizar saldos en la cuenta destino (suma).</li>
 *   <li>Registrar la operación en la tabla <code>OPERACIONES</code>.</li>
 *   <li>Registrar el detalle de la transferencia en la tabla <code>TRANSFERENCIAS</code>.</li>
 * </ul>
 *
 * <p>
 * Además, utiliza un {@link ICuentasDAO} para construir el objeto {@link Transferencia}
 * final con las cuentas origen y destino completas.
 * </p>
 *
 * @author Carmen Andrea Lara Osuna
 */
public class TransferenciasDAO implements ITransferenciasDAO {

    private static final Logger LOGGER = Logger.getLogger(TransferenciasDAO.class.getName());

    /**
     * DAO de cuentas utilizado para consultar información de cuentas (origen y destino)
     * al crear el objeto {@link Transferencia}.
     */
    private ICuentasDAO cuentasDAO;

    /**
     * <p>
     * Construye el DAO de transferencias recibiendo el {@link ICuentasDAO} por inyección
     * de dependencia.
     * </p>
     *
     * @param cuentasDAO implementación de {@link ICuentasDAO} para consultar cuentas por número.
     */
    public TransferenciasDAO(ICuentasDAO cuentasDAO) {
        this.cuentasDAO = cuentasDAO;

    }

    /**
     * <p>
     * Realiza una transferencia entre dos cuentas y registra el movimiento en la base de datos.
     * </p>
     *
     * <p>
     * Flujo general del método:
     * </p>
     * <ul>
     *   <li>Resta el monto al saldo de la cuenta origen.</li>
     *   <li>Suma el monto al saldo de la cuenta destino.</li>
     *   <li>Registra una operación en <code>OPERACIONES</code> y obtiene el <code>id_transaccion</code>.</li>
     *   <li>Registra la transferencia en <code>TRANSFERENCIAS</code> asociándola al <code>id_transaccion</code>.</li>
     *   <li>Crea y retorna un objeto {@link Transferencia} con la información resultante.</li>
     * </ul>
     *
     * <p>
     * Nota: En el código actual existen valores <b>hardcodeados</b>:
     * </p>
     * <ul>
     *   <li><code>id_cuenta</code> en operaciones se asigna como <code>2</code>.</li>
     *   <li><code>id_cuenta_destino</code> en transferencias se asigna como <code>4</code>.</li>
     * </ul>
     * <p>
     * (Se documenta como nota para claridad del comportamiento actual.)
     * </p>
     *
     * @param nuevaTransferencia DTO con los datos de la transferencia (cuenta origen, destino, monto, fecha y tipo).
     * @return objeto {@link Transferencia} creado a partir de los datos registrados.
     * @throws PersistenciaException si ocurre un error al actualizar saldos o registrar la operación/transferencia.
     */
    @Override
    public Transferencia realizarTransferencia(NuevaTransferenciaDTO nuevaTransferencia) throws PersistenciaException {

        try {

            //sql para restar el saldo de la cuenta origen
            String codigoSQLRestaOrigen = """
                               UPDATE cuentas 
                                    SET saldo = saldo - ? 
                               WHERE numero_cuenta = ?
                               """;

            //sql para sumar al saldo de la cuenta destino
            String codigoSQLSumaDestino = """
                                UPDATE cuentas 
                                    SET saldo = saldo + ? 
                                WHERE numero_cuenta = ?
                                """;

            //sql para registrar operacion
            String codigoSQLOperacion = """
                               INSERT INTO OPERACIONES (tipo_operacion, fecha_hora, monto, id_cuenta) 
                               VALUES (?,?,?,?);
                               """;

            //sql para registrar trasferencia
            String codigoSQLTransferencia = """
                               INSERT INTO TRANSFERENCIAS (id_transaccion,id_cuenta_destino) 
                               VALUES (?,?);
                               """;

            //conexión
            Connection conexion = ConexionBD.crearConexion();

            //suma y resta de saldos
            PreparedStatement comandoRestaSaldo = conexion.prepareStatement(codigoSQLRestaOrigen);
            comandoRestaSaldo.setDouble(1, nuevaTransferencia.getMonto());
            comandoRestaSaldo.setString(2, nuevaTransferencia.getCuentaOrigen());
            comandoRestaSaldo.executeUpdate();

            PreparedStatement comandoSumaSaldo = conexion.prepareStatement(codigoSQLSumaDestino);
            comandoSumaSaldo.setDouble(1, nuevaTransferencia.getMonto());
            comandoSumaSaldo.setString(2, nuevaTransferencia.getCuentaDestino());
            comandoSumaSaldo.executeUpdate();

            //registro de operacion y transferencia
            PreparedStatement comandoOperacion = conexion.prepareStatement(codigoSQLOperacion, PreparedStatement.RETURN_GENERATED_KEYS);

            comandoOperacion.setString(1, nuevaTransferencia.getTipoOperacion().name());
            comandoOperacion.setTimestamp(2, Timestamp.valueOf(nuevaTransferencia.getFechaHoraOperacion()));
            comandoOperacion.setDouble(3, nuevaTransferencia.getMonto());
            comandoOperacion.setInt(4, 2); //CLIENTE HARCODEADO 

            comandoOperacion.executeUpdate();
            ResultSet rs = comandoOperacion.getGeneratedKeys();
            rs.next();
            int idTransaccion = rs.getInt(1);

            PreparedStatement comandoTransferencia = conexion.prepareStatement(codigoSQLTransferencia);
            comandoTransferencia.setInt(1, idTransaccion);
            comandoTransferencia.setInt(2, 4);

            comandoTransferencia.executeUpdate(); //CLIENTE HARCODEADO 

            LOGGER.fine("Se realizó la transferencia con éxito.");

            //Creacion del objeto transferencia
            Transferencia transferencia = new Transferencia(
                    idTransaccion,
                    cuentasDAO.obtenerCuentaporNumeroCuenta(nuevaTransferencia.getCuentaOrigen()),
                    cuentasDAO.obtenerCuentaporNumeroCuenta(nuevaTransferencia.getCuentaDestino()),
                    nuevaTransferencia.getMonto(),
                    nuevaTransferencia.getFechaHoraOperacion(),
                    nuevaTransferencia.getTipoOperacion()
            );

            conexion.close();

            return transferencia;

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo realizar la transferencia.", ex);
        }

    }

}