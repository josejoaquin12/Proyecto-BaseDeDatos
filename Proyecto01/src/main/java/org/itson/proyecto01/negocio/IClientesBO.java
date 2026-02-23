/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.negocio;

import org.itson.proyecto01.dtos.NuevoClienteDTO;
import org.itson.proyecto01.entidades.Cliente;

/**
 *
 * @author Jesus Omar
 */
public interface IClientesBO {
    
    public abstract Cliente crearCliente(NuevoClienteDTO nuevoCliente, Integer idDomicilio)throws NegocioException;
    
    public abstract Cliente obtenerClientePorId(Integer idCliente)throws NegocioException;
    
    public abstract  int autenticarNombreCompletoPassword(String nombreCompleto, String password)throws NegocioException;
    public Cliente actualizarCliente(Integer id, NuevoClienteDTO nuevoCliente, Integer idDomicilio)throws NegocioException;

   
}
