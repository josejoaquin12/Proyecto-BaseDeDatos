/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.util.List;
import org.itson.proyecto01.entidades.Operacion;
import org.itson.proyecto01.persistencia.IOperacionesDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 *
 * @author Jesus Omar
 */
public class OperacionesBO implements IOperacionesBO{
    
    private final IOperacionesDAO operacionesDAO;
    
    public OperacionesBO(IOperacionesDAO operacionesDAO){
        this.operacionesDAO = operacionesDAO;
    }

    @Override
    public List<Operacion> obtenerOperaciones(Integer idCliente) throws NegocioException {
        try{
            List<Operacion> listaOperaciones = operacionesDAO.obtenerOperaciones(idCliente);
            return listaOperaciones;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al consultar las operaciones", null);
        }
    }

    @Override
    public List<Operacion> operacionesPorTipo(Integer idCliente, String filtro) throws NegocioException {
        try{
            List<Operacion> listaOperaciones = operacionesDAO.operacionesPorTipo(idCliente, filtro);
            return listaOperaciones;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al consultar las operaciones", null);
        }
    }

    @Override
    public List<Operacion> operacionesPorFecha(Integer idCliente, String fechaInicio, String fechaFin) throws NegocioException {
        try{
            List<Operacion> listaOperaciones = operacionesDAO.operacionesPorFecha(idCliente, fechaInicio, fechaFin);
            return listaOperaciones;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al consultar las operaciones", null);
        }
    }

    @Override
    public List<Operacion> operacionesPorFechaTipo(Integer idCliente, String filtroTipo, String fechaInicio, String fechaFin) throws NegocioException {
        try{
            List<Operacion> listaOperaciones = operacionesDAO.operacionesPorFechaTipo(idCliente, filtroTipo, fechaInicio, fechaFin);
            return listaOperaciones;
        }catch(PersistenciaException ex){
            throw new NegocioException("Error al consultar las operaciones", null);
        }
    }
    
}
