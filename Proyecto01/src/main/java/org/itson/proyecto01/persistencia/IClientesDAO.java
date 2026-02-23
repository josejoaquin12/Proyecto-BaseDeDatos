/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import org.itson.proyecto01.dtos.NuevoClienteDTO;
import org.itson.proyecto01.entidades.Cliente;

/**
 *
 * @author Jesus Omar
 */
public interface IClientesDAO {
    
    public Cliente crearCliente(Integer idDomicilio, NuevoClienteDTO nuevoCliente, Integer idDomicilio1) throws PersistenciaException;
    
    public Cliente obtenerClientePorId(Integer idCliente) throws PersistenciaException;
    
    public int verificarCredenciales(String NombreCompleto) throws PersistenciaException ;
    
    public String obtenerHashPorNombreCompleto(String nombreCompleto)throws PersistenciaException;
    
    public Cliente actualizarCliente( Integer idCliente,NuevoClienteDTO clienteDTO, int idDomicilio) throws PersistenciaException ;
}
