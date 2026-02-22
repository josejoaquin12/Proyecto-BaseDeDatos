/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import java.util.List;
import org.itson.proyecto01.entidades.Operacion;

/**
 *
 * @author Jesus Omar
 */
public interface IOperacionesDAO {
    
    public abstract List<Operacion> obtenerOperaciones(Integer idCliente)throws PersistenciaException;
    
    public abstract List<Operacion> operacionesPorTipo(Integer idCliente, String filtro)throws PersistenciaException;
    
    public abstract List<Operacion> operacionesPorFecha(Integer idCliente, String fechaInicio, String fechaFin)throws PersistenciaException;
    
    public abstract List<Operacion> operacionesPorFechaTipo(Integer idCliente, String filtroTipo, String fechaInicio, String fechaFin)throws PersistenciaException;
}
