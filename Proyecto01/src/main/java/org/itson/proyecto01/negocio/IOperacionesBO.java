/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.time.LocalDateTime;
import java.util.List;
import org.itson.proyecto01.entidades.Operacion;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 *
 * @author Jesus Omar
 */
public interface IOperacionesBO {

    public List<Operacion> obtenerOperaciones(Integer idCliente) throws NegocioException;

    public abstract List<Operacion> operacionesPorTipo(Integer idCliente, TipoOperacion filtro) throws NegocioException;

    public abstract List<Operacion> operacionesPorFecha(Integer idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws NegocioException;

    public abstract List<Operacion> operacionesPorFechaTipo(Integer idCliente, TipoOperacion filtroTipo, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws NegocioException;

}
