/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.util.logging.Logger;
import org.itson.proyecto01.dtos.NuevaTransferenciaDTO;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.Transferencia;
import org.itson.proyecto01.enums.EstadoCuenta;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.ITransferenciasDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 *
 * @author joset
 */
public class TransferenciasBO implements ITransferenciasBO {

    private ITransferenciasDAO transferenciasDAO;
    private ICuentasDAO cuentasDAO;

    public TransferenciasBO(ITransferenciasDAO transferenciasDAO, ICuentasDAO cuentasDAO) {
        this.transferenciasDAO = transferenciasDAO;
        this.cuentasDAO = cuentasDAO;
    }

    @Override
    public Transferencia realizarTransferencia(NuevaTransferenciaDTO nuevaTransferencia) throws NegocioException {

        try {
            Cuenta cuentaOrigen = cuentasDAO.obtenerCuentaporNumeroCuenta(nuevaTransferencia.getCuentaOrigen());
            Cuenta cuentaDestino = cuentasDAO.obtenerCuentaporNumeroCuenta(nuevaTransferencia.getCuentaDestino());

            if (nuevaTransferencia.getCuentaOrigen().equals(nuevaTransferencia.getCuentaDestino()))   {
                throw new NegocioException("La cuenta origen y destino no pueden ser iguales.", null);
            }

            if (nuevaTransferencia.getCuentaDestino() == null) {
                throw new NegocioException("La cuenta destino es obligatoria.", null);
            }

            if (nuevaTransferencia.getCuentaDestino().length() != 18) {
                throw new NegocioException("El numero de cuenta debe tener 18 digitos.", null);
            }

            if (cuentasDAO.obtenerSaldoPorNumeroCuenta(nuevaTransferencia.getCuentaOrigen()) == 0) {
                throw new NegocioException("La cuenta no tiene saldo", null);
            }

            if (cuentasDAO.obtenerSaldoPorNumeroCuenta(nuevaTransferencia.getCuentaOrigen()) < nuevaTransferencia.getMonto()) {
                throw new NegocioException("Fondos insuficientes.", null);
            }

            if (cuentasDAO.obtenerSaldoPorNumeroCuenta(nuevaTransferencia.getCuentaOrigen()) < 0) {
                throw new NegocioException("Los montos negativos son invalidos.", null);
            }

            if (nuevaTransferencia.getMonto() > 100000) {
                throw new NegocioException("No se permite realizar transferencias de más de $100,000.", null);
            }

            if (cuentaDestino.getEstado().equals(EstadoCuenta.CANCELADA)) {
                throw new NegocioException("Imposible transferir a una cuenta cancelada.", null);
            }
            
            Transferencia transferencia = this.transferenciasDAO.realizarTransferencia(nuevaTransferencia);
            return transferencia;
            
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al acceder a la cuenta", ex);

        }

    }
}