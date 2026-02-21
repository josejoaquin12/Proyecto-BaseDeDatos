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
 *
 * @author joset
 */
public class CuentasBO implements ICuentasBO {

    private final ICuentasDAO cuentasDAO;

    public CuentasBO(ICuentasDAO cuentasDAO) {
        this.cuentasDAO = cuentasDAO;
    }

    @Override
    public List<Cuenta> consultarCuentasCliente(Integer idCliente) throws NegocioException {
        try {
            List<Cuenta> listaCuentasCliente = this.cuentasDAO.obtenerCuentas(idCliente);
            return listaCuentasCliente;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al consultar la lista de cliente", ex);
        }
    }

    @Override
    public Cuenta obtenerCuentaporNumeroCuenta(String numeroCuenta) throws NegocioException {
        try {
            Cuenta cuenta = this.cuentasDAO.obtenerCuentaporNumeroCuenta(numeroCuenta);
            return cuenta;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error, no se encontro la cuenta", null);
        }
    }

    @Override
    public Cuenta altaCuenta() throws NegocioException {
        try {
            Cliente clienteSesion = SesionControl.getSesion().getCliente();
            if (clienteSesion == null) {
                throw new NegocioException("No hay cliente activo.", null);
            }

            NuevaCuentaDTO nuevaCuenta = new NuevaCuentaDTO(LocalDateTime.now(), clienteSesion.getId());

            return cuentasDAO.altaCuenta(nuevaCuenta);

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al crear la cuenta.", ex);
        }
    }

    @Override
    public Cuenta cancelarCuenta(String numeroCuenta) throws NegocioException {
        try {
            Cliente clienteSesion = SesionControl.getSesion().getCliente();
            if (clienteSesion == null) {
                throw new NegocioException("No hay cliente en sesión.", null);
            }

            Cuenta cuenta = cuentasDAO.obtenerCuentaporNumeroCuenta(numeroCuenta);
            
            if (cuenta == null) {
                throw new NegocioException("La cuenta no existe.", null);
            }

            if (!cuenta.getIdCliente().equals(clienteSesion.getId())) {
                throw new NegocioException("La cuenta no pertenece al cliente en sesión.", null);
            }
            if (cuenta.getEstado() == EstadoCuenta.CANCELADA) {
                throw new NegocioException("La cuenta ya está cancelada.", null);
            }
            if (cuenta.getSaldo() != 0.0) {
                throw new NegocioException("No se puede cancelar una cuenta con saldo.", null);
            }

            cuentasDAO.cancelarCuenta(cuenta.getId());
            cuenta.setEstado(EstadoCuenta.CANCELADA);

            return cuenta;

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al cancelar la cuenta.", ex);
        }
    }

}


