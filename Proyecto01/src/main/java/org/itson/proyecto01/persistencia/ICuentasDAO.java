
package org.itson.proyecto01.persistencia;
import java.util.List;
import org.itson.proyecto01.entidades.Cuenta;

/**
 *
 * @author joset
 */


public interface ICuentasDAO {

    public List<Cuenta> obtenerCuentas(Integer idCliente)throws PersistenciaException ;
    
    public List<Cuenta> obtenerCuentasActivas(Integer idCliente)throws PersistenciaException ;

    public Double obtenerSaldoPorNumeroCuenta(String numeroCuenta) throws PersistenciaException;

    public Cuenta obtenerCuentaporNumeroCuenta(String numeroCuenta) throws PersistenciaException;

    public void actualizarSaldo(Integer idCuenta, double nuevoSaldo)throws PersistenciaException ;
    
   // public Cuenta altaCuenta(NuevaCuentaDTO nuevacuenta)throws PersistenciaException ;//TODO

    public void cancelarCuenta(Integer idCuenta) throws PersistenciaException ;
}

