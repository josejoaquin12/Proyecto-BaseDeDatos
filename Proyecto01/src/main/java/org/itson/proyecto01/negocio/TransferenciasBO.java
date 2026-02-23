/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import org.itson.proyecto01.dtos.NuevaTransferenciaDTO;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.Transferencia;
import org.itson.proyecto01.enums.EstadoCuenta;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.ITransferenciasDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 * <p>
 * Objeto de negocio (BO) para la funcionalidad de <b>transferencias</b> entre cuentas.
 * Esta clase valida reglas de negocio y, si todo es correcto, delega la ejecución
 * a la capa de persistencia mediante {@link ITransferenciasDAO}.
 * </p>
 *
 * <p>
 * Validaciones principales realizadas antes de transferir:
 * </p>
 * <ul>
 *   <li>La cuenta origen y destino no pueden ser iguales.</li>
 *   <li>La cuenta destino es obligatoria y debe tener 18 dígitos.</li>
 *   <li>La cuenta origen debe tener saldo mayor que cero y suficiente para cubrir el monto.</li>
 *   <li>No se permiten montos negativos.</li>
 *   <li>El monto máximo permitido por transferencia es de $100,000.</li>
 *   <li>No se permite transferir a una cuenta con estado {@link EstadoCuenta#CANCELADA}.</li>
 * </ul>
 *
 * <p>
 * Dependencias:
 * </p>
 * <ul>
 *   <li>{@link ITransferenciasDAO}: ejecuta la transferencia y registra la operación en BD.</li>
 *   <li>{@link ICuentasDAO}: consulta cuentas y saldos para validaciones previas.</li>
 * </ul>
 *
 * @author joset
 */
public class TransferenciasBO implements ITransferenciasBO {

    /**
     * DAO de transferencias utilizado para ejecutar la transferencia en la base de datos.
     */
    private ITransferenciasDAO transferenciasDAO;

    /**
     * DAO de cuentas utilizado para consultar cuentas y saldos para validaciones.
     */
    private ICuentasDAO cuentasDAO;

    /**
     * <p>
     * Construye el BO de transferencias recibiendo sus dependencias por inyección.
     * </p>
     *
     * @param transferenciasDAO implementación de {@link ITransferenciasDAO}.
     * @param cuentasDAO implementación de {@link ICuentasDAO}.
     */
    public TransferenciasBO(ITransferenciasDAO transferenciasDAO, ICuentasDAO cuentasDAO) {
        this.transferenciasDAO = transferenciasDAO;
        this.cuentasDAO = cuentasDAO;
    }

    /**
     * <p>
     * Realiza una transferencia aplicando reglas de negocio y delegando la ejecución a persistencia.
     * </p>
     *
     * <p>
     * Flujo general:
     * </p>
     * <ul>
     *   <li>Consulta cuenta origen y cuenta destino.</li>
     *   <li>Valida reglas de negocio (cuentas, longitud, saldo, monto, estado).</li>
     *   <li>Si todo es válido, llama a {@link ITransferenciasDAO#realizarTransferencia(NuevaTransferenciaDTO)}.</li>
     * </ul>
     *
     * @param nuevaTransferencia DTO con la información requerida para transferir (origen, destino, monto, fecha, tipo).
     * @return {@link Transferencia} resultante de la operación.
     * @throws NegocioException si alguna validación falla o si ocurre un error en persistencia.
     */
    @Override
    public Transferencia realizarTransferencia(NuevaTransferenciaDTO nuevaTransferencia) throws NegocioException {

        try {
            Cuenta cuentaOrigen = cuentasDAO.obtenerCuentaporNumeroCuenta(nuevaTransferencia.getCuentaOrigen());
            Cuenta cuentaDestino = cuentasDAO.obtenerCuentaporNumeroCuenta(nuevaTransferencia.getCuentaDestino());

            if (nuevaTransferencia.getCuentaOrigen().equals(nuevaTransferencia.getCuentaDestino())) {
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