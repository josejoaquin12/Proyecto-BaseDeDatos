/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import org.itson.proyecto01.dtos.NuevaTransferenciaDTO;
import org.itson.proyecto01.entidades.Cuenta;
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

            String codigoSQLOperacion = """
                               INSERT INTO OPERACIONES (tipo_operacion, fecha_hora, monto, id_cuenta) 
                               VALUES (?,?,?,?);
                               """;

            String codigoSQLTransferencia = """
                               INSERT INTO TRANSFERENCIAS (id_cuenta_destino) 
                               VALUES (?);
                               """;
            Connection conexion = ConexionBD.crearConexion();

            PreparedStatement comandoOperacion = conexion.prepareStatement(codigoSQLOperacion);

            comandoOperacion.setString(1, nuevaTransferencia.getTipoOperacion().name());
            comandoOperacion.setTimestamp(2, Timestamp.valueOf(nuevaTransferencia.getFechaHoraOperacion()));
            comandoOperacion.setFloat(3, nuevaTransferencia.getMonto());
            comandoOperacion.setInt(4, 1); //CLIENTE HARCODEADO 

            PreparedStatement comandoTransferencia = conexion.prepareStatement(codigoSQLTransferencia);
            comandoTransferencia.setInt(1, 1); //CLIENTE HARCODEADO 

            comandoOperacion.execute();
            comandoTransferencia.execute();

            LOGGER.fine("Se realizó la transferencia con éxito.");
            
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
