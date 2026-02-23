/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import org.itson.proyecto01.dtos.NuevoDomicilioDTO;
import org.itson.proyecto01.entidades.Domicilio;
import org.itson.proyecto01.persistencia.IDomiciliosDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 *
 * @author Jesus Omar
 */
public class DomiciliosBO implements IDomiciliosBO{
    
    private final IDomiciliosDAO domiciliosDAO;
    
    public DomiciliosBO (IDomiciliosDAO domiciliosDAO){
        this.domiciliosDAO = domiciliosDAO;
    }
    
    @Override
    public Domicilio registrarDomicilio(NuevoDomicilioDTO nuevoDomicilio) throws NegocioException {
        // Validar que no vengan campos vacios
        if(nuevoDomicilio.getCalle() == null){
            throw new NegocioException("La calle no puede estar vacia", null);
        }
        if(nuevoDomicilio.getNumero()== null){
            throw new NegocioException("La numero no puede estar vacio", null);
        }
        if(nuevoDomicilio.getColonia()== null){
            throw new NegocioException("La colonia no puede estar vacia", null);
        }
        if(nuevoDomicilio.getCiudad()== null){
            throw new NegocioException("La ciudad no puede estar vacia", null);
        }
        if(nuevoDomicilio.getEstado()== null){
            throw new NegocioException("El estado no puede estar vacio", null);
        }
        if(nuevoDomicilio.getCodigoPostal()== null){
            throw new NegocioException("El codigo postal no puede estar vacio", null);
        }
        // Validar que los strings no sean tan largos
        if(nuevoDomicilio.getCalle().length() > 100){
            throw new NegocioException("El nombre de la calle es demasiado largo", null);
        }
        if(nuevoDomicilio.getNumero().length() > 20){
            throw new NegocioException("El numero del domicilio es demasiado largo", null);
        }
        if(nuevoDomicilio.getColonia().length() > 100){
            throw new NegocioException("El nombre de la colonia es demasiado largo", null);
        }
        if(nuevoDomicilio.getCiudad().length() > 100){
            throw new NegocioException("El nombre de la ciudad es demasiado largo", null);
        }
        if(nuevoDomicilio.getEstado().length() > 100){
            throw new NegocioException("El nombre del estado es demasiado largo", null);
        }
        if(nuevoDomicilio.getCodigoPostal().length() > 10){
            throw new NegocioException("El codigo postal es demasiado largo", null);
        }
        try {
            int numdomicilio = Integer.parseInt(nuevoDomicilio.getNumero());
            int codigoPostal = Integer.parseInt(nuevoDomicilio.getCodigoPostal());
            return domiciliosDAO.registrarDomicilio(nuevoDomicilio);
        } catch (PersistenciaException ex  ) {
            throw new NegocioException("Error al crear domicilio", ex);
        }catch (NumberFormatException ex  ) {
            throw new NegocioException("Error al crear domicilio: numero domicilio o codigo postal invalido ", ex);
        }
    }
    
    public Domicilio obtenerDomicilioPorID(Integer idDom)throws NegocioException{
        try {
            return domiciliosDAO.obtenerDomicilioID(idDom);
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al obtener el domicilio" + ex.getMessage(), ex);
        }
    }
    
}
