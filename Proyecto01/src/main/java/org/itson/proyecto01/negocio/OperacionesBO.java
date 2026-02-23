/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.time.LocalDateTime;
import java.util.List;
import org.itson.proyecto01.entidades.Operacion;
import org.itson.proyecto01.enums.TipoOperacion;
import org.itson.proyecto01.persistencia.IOperacionesDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 * <p>
 * Objeto de negocio (BO) para la entidad <b>Operación</b>. Esta clase actúa como capa intermedia
 * entre la presentación y la persistencia, delegando las consultas a {@link IOperacionesDAO}
 * y encapsulando el manejo de errores en forma de {@link NegocioException}.
 * </p>
 *
 * <p>
 * Permite consultar operaciones de un cliente con distintos filtros:
 * </p>
 * <ul>
 *   <li>Todas las operaciones del cliente.</li>
 *   <li>Operaciones filtradas por {@link TipoOperacion}.</li>
 *   <li>Operaciones dentro de un rango de fechas.</li>
 *   <li>Operaciones filtradas por tipo y rango de fechas.</li>
 * </ul>
 *
 * @author Jesus Omar
 */
public class OperacionesBO implements IOperacionesBO {

    /**
     * DAO de operaciones utilizado para realizar consultas en la base de datos.
     */
    private final IOperacionesDAO operacionesDAO;

    /**
     * <p>
     * Construye el BO de operaciones recibiendo el DAO por inyección de dependencia.
     * </p>
     *
     * @param operacionesDAO implementación de {@link IOperacionesDAO} para operaciones de persistencia.
     */
    public OperacionesBO(IOperacionesDAO operacionesDAO) {
        this.operacionesDAO = operacionesDAO;
    }

    /**
     * <p>
     * Obtiene todas las operaciones asociadas a un cliente.
     * </p>
     *
     * @param idCliente identificador del cliente.
     * @return lista de {@link Operacion} del cliente; si no hay registros, retorna lista vacía.
     * @throws NegocioException si ocurre un error al consultar en persistencia.
     */
    @Override
    public List<Operacion> obtenerOperaciones(Integer idCliente) throws NegocioException {
        try {
            List<Operacion> listaOperaciones = operacionesDAO.obtenerOperaciones(idCliente);
            return listaOperaciones;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al consultar las operaciones", null);
        }
    }

    /**
     * <p>
     * Obtiene las operaciones de un cliente filtradas por tipo de operación.
     * </p>
     *
     * @param idCliente identificador del cliente.
     * @param filtro tipo de operación a consultar.
     * @return lista de {@link Operacion} que coinciden con el tipo; si no hay, retorna lista vacía.
     * @throws NegocioException si ocurre un error al consultar en persistencia.
     */
    @Override
    public List<Operacion> operacionesPorTipo(Integer idCliente, TipoOperacion filtro) throws NegocioException {
        try {
            List<Operacion> listaOperaciones = operacionesDAO.operacionesPorTipo(idCliente, filtro);
            return listaOperaciones;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al consultar las operaciones", null);
        }
    }

    /**
     * <p>
     * Obtiene las operaciones de un cliente dentro de un rango de fechas.
     * </p>
     *
     * @param idCliente identificador del cliente.
     * @param fechaInicio fecha/hora inicial del rango (inclusive).
     * @param fechaFin fecha/hora final del rango (inclusive).
     * @return lista de {@link Operacion} dentro del rango; si no hay, retorna lista vacía.
     * @throws NegocioException si ocurre un error al consultar en persistencia.
     */
    @Override
    public List<Operacion> operacionesPorFecha(Integer idCliente, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws NegocioException {
        try {
            List<Operacion> listaOperaciones = operacionesDAO.operacionesPorFecha(idCliente, fechaInicio, fechaFin);
            return listaOperaciones;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al consultar las operaciones", null);
        }
    }

    /**
     * <p>
     * Obtiene las operaciones de un cliente filtradas por tipo y dentro de un rango de fechas.
     * </p>
     *
     * @param idCliente identificador del cliente.
     * @param filtroTipo tipo de operación a consultar.
     * @param fechaInicio fecha/hora inicial del rango (inclusive).
     * @param fechaFin fecha/hora final del rango (inclusive).
     * @return lista de {@link Operacion} que cumplan ambos filtros; si no hay, retorna lista vacía.
     * @throws NegocioException si ocurre un error al consultar en persistencia.
     */
    @Override
    public List<Operacion> operacionesPorFechaTipo(Integer idCliente, TipoOperacion filtroTipo, LocalDateTime fechaInicio, LocalDateTime fechaFin) throws NegocioException {
        try {
            List<Operacion> listaOperaciones = operacionesDAO.operacionesPorFechaTipo(idCliente, filtroTipo, fechaInicio, fechaFin);
            return listaOperaciones;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al consultar las operaciones", null);
        }
    }

}