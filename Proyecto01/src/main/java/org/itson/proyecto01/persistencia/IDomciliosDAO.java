/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import org.itson.proyecto01.dtos.NuevoDomicilioDTO;
import org.itson.proyecto01.entidades.Domicilio;

/**
 *
 * @author Jesus Omar
 */
public interface IDomciliosDAO {
    
    public abstract Domicilio registrarDomicilio(NuevoDomicilioDTO nuevoDomicilio)throws PersistenciaException;
    
}
