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
    
    public Cliente crearCliente(NuevoClienteDTO nuevoCliente, Integer idDomicilio) throws PersistenciaException;
    
    public Cliente obtenerClientePorId(Integer idCliente) throws PersistenciaException;
    
}
