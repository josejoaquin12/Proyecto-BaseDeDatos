/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;
import org.itson.proyecto01.dtos.NuevaTransferenciaDTO;
import org.itson.proyecto01.entidades.Transferencia;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
public class TransferenciasDAO implements ITransferenciasDAO {

    private static final Logger LOGGER = Logger.getLogger(TransferenciasDAO.class.getName());

    private ICuentasDAO cuentasDAO;

    @Override
    public Transferencia realizarTransferencia(NuevaTransferenciaDTO nuevaTransferencia) throws PersistenciaException {

        try {

            //sql para restar el saldo de la cuenta origen
            String codigoSQLRestaOrigen = """
                               UPDATE Cuenta 
                                    SET saldo = saldo - ? 
                               WHERE numero_cuenta = ?
                               """;

            //sql para sumar al saldo de la cuenta destino
            String codigoSQLSumaDestino = """
                                UPDATE Cuenta 
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
                               INSERT INTO TRANSFERENCIAS (id_cuenta_destino) 
                               VALUES (?);
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
            PreparedStatement comandoOperacion = conexion.prepareStatement(codigoSQLOperacion);

            comandoOperacion.setString(1, nuevaTransferencia.getTipoOperacion().name());
            comandoOperacion.setTimestamp(2, Timestamp.valueOf(nuevaTransferencia.getFechaHoraOperacion()));
            comandoOperacion.setDouble(3, nuevaTransferencia.getMonto());
            comandoOperacion.setInt(4, 1); //CLIENTE HARCODEADO 

            PreparedStatement comandoTransferencia = conexion.prepareStatement(codigoSQLTransferencia);
            comandoTransferencia.setInt(1, 1); //CLIENTE HARCODEADO 

            //ejecucion
            comandoOperacion.execute();
            comandoTransferencia.execute();

            LOGGER.fine("Se realizó la transferencia con éxito.");

            //Creacion del objeto transferencia
            Transferencia transferencia = new Transferencia(
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
