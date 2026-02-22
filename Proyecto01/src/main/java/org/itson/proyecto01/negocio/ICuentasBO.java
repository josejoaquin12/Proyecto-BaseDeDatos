/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.util.List;
import org.itson.proyecto01.dtos.NuevaCuentaDTO;
import org.itson.proyecto01.entidades.Cuenta;

/**
 *
 * @author joset
 */
public interface ICuentasBO {
    public List<Cuenta> consultarCuentasClienteActivas(Integer idCliente) throws NegocioException;
    
    public List<Cuenta> consultarCuentasCliente(Integer idCliente) throws NegocioException;

    public Cuenta obtenerCuentaporNumeroCuenta(String numeroCuenta) throws NegocioException;

    public Cuenta altaCuenta() throws NegocioException;

    public Cuenta cancelarCuenta(String numeroCuenta) throws NegocioException;
}
