/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.util.List;
import org.itson.proyecto01.dtos.NuevaCuentaDTO;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 *
 * @author joset
 */
public class CuentasBO implements ICuentasBO{
    private final ICuentasDAO cuentasDAO;
     
    public CuentasBO(ICuentasDAO cuentasDAO){
        this.cuentasDAO = cuentasDAO;
    }

    @Override
    public List<Cuenta> consultarCuentasCliente(Integer idCliente) throws NegocioException {
     try{
            List<Cuenta> listaCuentasCliente = this.cuentasDAO.obtenerCuentasActivas(idCliente);
            return listaCuentasCliente;
        }catch(PersistenciaException  ex){
            throw new NegocioException("Error al consultar la lista de cliente", ex);
        }   
    }

    @Override
    public Cuenta obtenerCuentaporNumeroCuenta(String numeroCuenta) throws NegocioException {
        try{
            Cuenta cuenta = this.cuentasDAO.obtenerCuentaporNumeroCuenta(numeroCuenta);
            return cuenta;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error, no se encontro la cuenta", null);
        }
    }

    @Override
    public Cuenta altaCuenta(NuevaCuentaDTO nuevaCuenta) throws NegocioException {

        
    }

    @Override
    public void cancelarCuenta(NuevaCuentaDTO nuevaCuenta) throws NegocioException {


    }
    
}
