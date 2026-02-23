package org.itson.proyecto01.negocio;

import java.time.LocalDateTime;
import java.util.List;
import org.itson.proyecto01.control.SesionControl;
import org.itson.proyecto01.dtos.NuevaCuentaDTO;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.enums.EstadoCuenta;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 * <p>
 * Objeto de negocio (BO) para la entidad <b>Cuenta</b>. Esta clase contiene las reglas
 * y validaciones de negocio necesarias antes de delegar las operaciones a la capa
 * de persistencia ({@link ICuentasDAO}).
 * </p>
 *
 * <p>
 * Responsabilidades principales:
 * </p>
 * <ul>
 *   <li>Consultar cuentas de un cliente (todas o solo activas).</li>
 *   <li>Obtener una cuenta por su número de cuenta.</li>
 *   <li>Dar de alta una cuenta para el cliente en sesión.</li>
 *   <li>Cancelar una cuenta aplicando reglas de validación (propiedad, estado y saldo).</li>
 * </ul>
 *
 * <p>
 * La clase utiliza {@link SesionControl} para obtener al cliente actualmente autenticado
 * cuando la operación depende de la sesión (por ejemplo, alta y cancelación).
 * </p>
 *
 * @author joset
 */
public class CuentasBO implements ICuentasBO {

    /**
     * DAO de cuentas utilizado para operaciones de persistencia.
     */
    private final ICuentasDAO cuentasDAO;

    /**
     * <p>
     * Construye el BO de cuentas recibiendo el DAO por inyección de dependencia.
     * </p>
     *
     * @param cuentasDAO implementación de {@link ICuentasDAO} para operaciones en base de datos.
     */
    public CuentasBO(ICuentasDAO cuentasDAO) {
        this.cuentasDAO = cuentasDAO;
    }

    /**
     * <p>
     * Consulta todas las cuentas asociadas a un cliente.
     * </p>
     *
     * @param idCliente identificador del cliente a consultar.
     * @return lista de {@link Cuenta} del cliente; si no hay registros, retorna una lista vacía.
     * @throws NegocioException si ocurre un error en la capa de persistencia.
     */
    @Override
    public List<Cuenta> consultarCuentasCliente(Integer idCliente) throws NegocioException {
        try {
            List<Cuenta> listaCuentasCliente = this.cuentasDAO.obtenerCuentas(idCliente);
            return listaCuentasCliente;
        } catch (PersistenciaException ex) {
            throw new NegocioException(" :Error al consultar la lista de cliente", ex);
        }
    }

    /**
     * <p>
     * Consulta las cuentas <b>activas</b> asociadas a un cliente.
     * </p>
     *
     * @param idCliente identificador del cliente a consultar.
     * @return lista de {@link Cuenta} activas del cliente; si no hay registros, retorna una lista vacía.
     * @throws NegocioException si ocurre un error en la capa de persistencia.
     */
    @Override
    public List<Cuenta> consultarCuentasClienteActivas(Integer idCliente) throws NegocioException {
        try {
            List<Cuenta> listaCuentasCliente = this.cuentasDAO.obtenerCuentasActivas(idCliente);
            return listaCuentasCliente;
        } catch (PersistenciaException ex) {
            throw new NegocioException(" :Error al consultar la lista de cliente", ex);
        }
    }

    /**
     * <p>
     * Obtiene una cuenta por su número de cuenta.
     * </p>
     *
     * @param numeroCuenta número de cuenta a consultar.
     * @return {@link Cuenta} encontrada.
     * @throws NegocioException si ocurre un error o no se encuentra la cuenta.
     */
    @Override
    public Cuenta obtenerCuentaporNumeroCuenta(String numeroCuenta) throws NegocioException {
        try {
            Cuenta cuenta = this.cuentasDAO.obtenerCuentaporNumeroCuenta(numeroCuenta);
            return cuenta;
        } catch (PersistenciaException ex) {
            throw new NegocioException(" :Error, no se encontro la cuenta", null);
        }
    }

    /**
     * <p>
     * Da de alta una nueva cuenta para el <b>cliente actualmente en sesión</b>.
     * </p>
     *
     * <p>
     * Flujo:
     * </p>
     * <ul>
     *   <li>Obtiene el cliente desde {@link SesionControl}.</li>
     *   <li>Valida que exista un cliente activo.</li>
     *   <li>Crea un {@link NuevaCuentaDTO} con la fecha actual y el ID del cliente.</li>
     *   <li>Delegar la creación a {@link ICuentasDAO#altaCuenta(NuevaCuentaDTO)}.</li>
     * </ul>
     *
     * @return {@link Cuenta} creada.
     * @throws NegocioException si no hay cliente en sesión o si ocurre un error al crear la cuenta.
     */
    @Override
    public Cuenta altaCuenta() throws NegocioException {
        try {
            Cliente clienteSesion = SesionControl.getSesion().getCliente();
            if (clienteSesion == null) {
                throw new NegocioException(" :No hay cliente activo.", null);
            }

            NuevaCuentaDTO nuevaCuenta = new NuevaCuentaDTO(LocalDateTime.now(), clienteSesion.getId());

            return cuentasDAO.altaCuenta(nuevaCuenta);

        } catch (PersistenciaException ex) {
            throw new NegocioException(" :Error al crear la cuenta.", null);
        }
    }

    /**
     * <p>
     * Cancela una cuenta del cliente en sesión aplicando reglas de negocio.
     * </p>
     *
     * <p>
     * Reglas aplicadas antes de cancelar:
     * </p>
     * <ul>
     *   <li>Debe existir un cliente en sesión.</li>
     *   <li>La cuenta debe existir.</li>
     *   <li>La cuenta debe pertenecer al cliente en sesión.</li>
     *   <li>La cuenta no debe estar ya cancelada.</li>
     *   <li>La cuenta debe tener saldo igual a <code>0.0</code>.</li>
     * </ul>
     *
     * <p>
     * Si cumple las reglas, se llama a {@link ICuentasDAO#cancelarCuenta(Integer)} y
     * se actualiza el estado del objeto a {@link EstadoCuenta#CANCELADA}.
     * </p>
     *
     * @param numeroCuenta número de cuenta a cancelar.
     * @return {@link Cuenta} cancelada (con estado actualizado).
     * @throws NegocioException si alguna regla falla o si ocurre un error en persistencia.
     */
    @Override
    public Cuenta cancelarCuenta(String numeroCuenta) throws NegocioException {
        try {
            Cliente clienteSesion = SesionControl.getSesion().getCliente();
            if (clienteSesion == null) {
                throw new NegocioException(" :No hay cliente en sesión.", null);
            }

            Cuenta cuenta = cuentasDAO.obtenerCuentaporNumeroCuenta(numeroCuenta);

            if (cuenta == null) {
                throw new NegocioException(" :La cuenta no existe.", null);
            }

            if (!cuenta.getIdCliente().equals(clienteSesion.getId())) {
                throw new NegocioException(" :La cuenta no pertenece al cliente en sesión.", null);
            }
            if (cuenta.getEstado() == EstadoCuenta.CANCELADA) {
                throw new NegocioException(" :La cuenta ya está cancelada.", null);
            }
            if (cuenta.getSaldo() != 0.0) {
                throw new NegocioException(" :No se puede cancelar una cuenta con saldo.", null);
            }

            cuentasDAO.cancelarCuenta(cuenta.getId());
            cuenta.setEstado(EstadoCuenta.CANCELADA);

            return cuenta;

        } catch (PersistenciaException ex) {
            throw new NegocioException(" :Error al cancelar la cuenta.", ex);
        }
    }

}