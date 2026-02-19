/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.itson.proyecto01.dtos.NuevoClienteDTO;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 *
 * @author Jesus Omar
 */
public class ClientesBO implements IClientesBO {
    
    private final IClientesDAO clientesDAO;
    
    public ClientesBO(IClientesDAO clientesDAO){
        this.clientesDAO = clientesDAO;
    }

    @Override
    public Cliente crearCliente(NuevoClienteDTO nuevoCliente, Integer idDomicilio)throws NegocioException{
        // Limite de edad para validar que el cliente sea mayor de edad
        LocalDate edadMinima = LocalDate.now().minusYears(18);
        // Validaciones
        if(nuevoCliente.getNombres() == null){
            throw new NegocioException("El nombre no puede estar vacio", null);
        }
        if(nuevoCliente.getApellidoP() == null){
            throw new NegocioException("El apellido paterno no puede estar vacio", null);
        }
        if(nuevoCliente.getApelidoM() == null){
            throw new NegocioException("El apellido materno no puede estar vacio", null);
        }
        if(nuevoCliente.getFechaNacimiento() == null){
            throw new NegocioException("La fecha de nacimiento no puede estar vacia", null);
        }
        if(nuevoCliente.getContrasenia() == null){
            throw new NegocioException("La contrasena no puede estar vacia", null);
        }
        if(nuevoCliente.getNombres().length() > 100){
            throw new NegocioException("El nombre es demasiado largo", null);
        }
        if(nuevoCliente.getApellidoP().length() > 100){
            throw new NegocioException("El apellido es demasiado largo", null);
        }
        if(nuevoCliente.getApelidoM().length() > 100){
            throw new NegocioException("El apellido es demasiado largo", null);
        }
        if(nuevoCliente.getFechaNacimiento().isAfter(edadMinima)){
            throw new NegocioException("La edad minima para abrir una cuenta es de 18", null);
        }
        if(nuevoCliente.getContrasenia().length() < 6){
            throw new NegocioException("La contrasena debe contener minimo 6 caracteres", null);
        }
        
        try{
            Cliente cliente = this.clientesDAO.crearCliente(nuevoCliente, idDomicilio);
            return cliente;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al registrarse", null);
        }
           
    }

    @Override
    public Cliente obtenerClientePorId(Integer idCliente) throws NegocioException {
        try{
            Cliente cliente = this.clientesDAO.obtenerClientePorId(idCliente);
            return cliente;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error, no se encontro el cliente", null);
        }
    }
    
}
