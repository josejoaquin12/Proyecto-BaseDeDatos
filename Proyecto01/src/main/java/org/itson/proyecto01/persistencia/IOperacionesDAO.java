/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import java.time.LocalDateTime;
import java.util.List;
import org.itson.proyecto01.entidades.Operacion;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 *
 * @author Jesus Omar
 */
public interface IOperacionesDAO {
    
    public abstract List<Operacion> obtenerOperaciones(Integer idCliente)throws PersistenciaException;
    
    public abstract List<Operacion> operacionesPorTipo(Integer idCliente, TipoOperacion filtro)throws PersistenciaException;
    
    public abstract List<Operacion> operacionesPorFecha(Integer idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin)throws PersistenciaException;
    
    public abstract List<Operacion> operacionesPorFechaTipo(Integer idCliente, TipoOperacion filtroTipo, LocalDateTime fechaInicio, LocalDateTime fechaFin)throws PersistenciaException;
}
