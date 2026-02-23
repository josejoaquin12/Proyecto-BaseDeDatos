/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import org.itson.proyecto01.control.SesionControl;
import org.itson.proyecto01.dtos.NuevoDomicilioDTO;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.entidades.Domicilio;
import org.itson.proyecto01.persistencia.IDomiciliosDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 * <p>
 * Objeto de negocio (BO) para la entidad <b>Domicilio</b>. Implementa reglas y validaciones
 * de negocio antes de delegar la operación a la capa de persistencia ({@link IDomiciliosDAO}).
 * </p>
 *
 * <p>
 * Responsabilidades principales:
 * </p>
 * <ul>
 *   <li>Validar datos de entrada para registrar un domicilio (campos obligatorios y longitudes).</li>
 *   <li>Validar formato numérico de <code>numero</code> y <code>codigoPostal</code>.</li>
 *   <li>Registrar un domicilio mediante {@link IDomiciliosDAO#registrarDomicilio(NuevoDomicilioDTO)}.</li>
 *   <li>Consultar un domicilio por ID mediante {@link IDomiciliosDAO#obtenerDomicilioID(Integer)}.</li>
 *   <li>Actualizar el domicilio del cliente en sesión usando el ID almacenado en sesión.</li>
 * </ul>
 *
 * <p>
 * Esta clase obtiene al cliente de sesión mediante {@link SesionControl}, lo cual permite
 * identificar el domicilio asociado al cliente actual para operaciones como actualización.
 * </p>
 *
 * @author Jesus Omar
 */
public class DomiciliosBO implements IDomiciliosBO {

    /**
     * DAO de domicilios utilizado para operaciones de persistencia.
     */
    private final IDomiciliosDAO domiciliosDAO;

    /**
     * Cliente actual obtenido desde la sesión (SesionControl).
     * Se utiliza para obtener el ID del domicilio asociado al cliente en sesión.
     */
    private Cliente Cliente = SesionControl.getSesion().getCliente();

    /**
     * <p>
     * Construye el BO de domicilios recibiendo su dependencia DAO por inyección.
     * </p>
     *
     * @param domiciliosDAO implementación de {@link IDomiciliosDAO} para operaciones de base de datos.
     */
    public DomiciliosBO(IDomiciliosDAO domiciliosDAO) {
        this.domiciliosDAO = domiciliosDAO;
    }

    /**
     * <p>
     * Registra un nuevo domicilio aplicando validaciones de negocio.
     * </p>
     *
     * <p>
     * Validaciones realizadas:
     * </p>
     * <ul>
     *   <li>Ningún campo requerido puede ser <code>null</code>.</li>
     *   <li>Longitudes máximas:
     *     <ul>
     *       <li><code>calle</code>, <code>colonia</code>, <code>ciudad</code>, <code>estado</code>: 100</li>
     *       <li><code>numero</code>: 20</li>
     *       <li><code>codigoPostal</code>: 10</li>
     *     </ul>
     *   </li>
     *   <li><code>numero</code> y <code>codigoPostal</code> deben ser valores numéricos válidos
     *       (se valida con <code>Integer.parseInt</code>).</li>
     * </ul>
     *
     * @param nuevoDomicilio DTO con los datos del domicilio a registrar.
     * @return {@link Domicilio} registrado (con ID generado) si todo fue exitoso.
     * @throws NegocioException si alguna validación falla o si ocurre un error en persistencia.
     */
    @Override
    public Domicilio registrarDomicilio(NuevoDomicilioDTO nuevoDomicilio) throws NegocioException {
        // Validar que no vengan campos vacios
        if (nuevoDomicilio.getCalle() == null) {
            throw new NegocioException("La calle no puede estar vacia", null);
        }
        if (nuevoDomicilio.getNumero() == null) {
            throw new NegocioException("La numero no puede estar vacio", null);
        }
        if (nuevoDomicilio.getColonia() == null) {
            throw new NegocioException("La colonia no puede estar vacia", null);
        }
        if (nuevoDomicilio.getCiudad() == null) {
            throw new NegocioException("La ciudad no puede estar vacia", null);
        }
        if (nuevoDomicilio.getEstado() == null) {
            throw new NegocioException("El estado no puede estar vacio", null);
        }
        if (nuevoDomicilio.getCodigoPostal() == null) {
            throw new NegocioException("El codigo postal no puede estar vacio", null);
        }
        // Validar que los strings no sean tan largos
        if (nuevoDomicilio.getCalle().length() > 100) {
            throw new NegocioException("El nombre de la calle es demasiado largo", null);
        }
        if (nuevoDomicilio.getNumero().length() > 20) {
            throw new NegocioException("El numero del domicilio es demasiado largo", null);
        }
        if (nuevoDomicilio.getColonia().length() > 100) {
            throw new NegocioException("El nombre de la colonia es demasiado largo", null);
        }
        if (nuevoDomicilio.getCiudad().length() > 100) {
            throw new NegocioException("El nombre de la ciudad es demasiado largo", null);
        }
        if (nuevoDomicilio.getEstado().length() > 100) {
            throw new NegocioException("El nombre del estado es demasiado largo", null);
        }
        if (nuevoDomicilio.getCodigoPostal().length() > 10) {
            throw new NegocioException("El codigo postal es demasiado largo", null);
        }
        try {
            int numdomicilio = Integer.parseInt(nuevoDomicilio.getNumero());
            int codigoPostal = Integer.parseInt(nuevoDomicilio.getCodigoPostal());
            return domiciliosDAO.registrarDomicilio(nuevoDomicilio);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al crear domicilio", ex);
        } catch (NumberFormatException ex) {
            throw new NegocioException("Error al crear domicilio: numero domicilio o codigo postal invalido ", ex);
        }
    }

    /**
     * <p>
     * Obtiene un domicilio por su identificador.
     * </p>
     *
     * @param idDom identificador del domicilio.
     * @return {@link Domicilio} encontrado; puede ser <code>null</code> si no existe.
     * @throws NegocioException si ocurre un error al consultar en persistencia.
     */
    @Override
    public Domicilio obtenerDomicilioPorID(Integer idDom) throws NegocioException {
        try {
            return domiciliosDAO.obtenerDomicilioID(idDom);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al obtener el domicilio" + ex.getMessage(), ex);
        }
    }

    /**
     * <p>
     * Actualiza el domicilio asociado al cliente en sesión.
     * </p>
     *
     * <p>
     * Validaciones realizadas:
     * </p>
     * <ul>
     *   <li>Ningún campo requerido puede ser <code>null</code>.</li>
     *   <li>Longitudes máximas:
     *     <ul>
     *       <li><code>calle</code>, <code>colonia</code>, <code>ciudad</code>, <code>estado</code>: 100</li>
     *       <li><code>numero</code>: 20</li>
     *       <li><code>codigoPostal</code>: 10</li>
     *     </ul>
     *   </li>
     *   <li><code>numero</code> y <code>codigoPostal</code> deben ser numéricos.</li>
     * </ul>
     *
     * <p>
     * La actualización se realiza sobre el ID del domicilio del cliente actual:
     * <code>Cliente.getIdDomicilio()</code>.
     * </p>
     *
     * @param nuevoDomicilio DTO con los nuevos datos del domicilio.
     * @return {@link Domicilio} actualizado si el DAO modificó al menos una fila; si no, puede retornar <code>null</code>.
     * @throws NegocioException si alguna validación falla o si ocurre un error en persistencia.
     */
    @Override
    public Domicilio actualizarDomicilio(NuevoDomicilioDTO nuevoDomicilio) throws NegocioException {
        // Validar que no vengan campos vacios
        if (nuevoDomicilio.getCalle() == null) {
            throw new NegocioException("La calle no puede estar vacia", null);
        }
        if (nuevoDomicilio.getNumero() == null) {
            throw new NegocioException("La numero no puede estar vacio", null);
        }
        if (nuevoDomicilio.getColonia() == null) {
            throw new NegocioException("La colonia no puede estar vacia", null);
        }
        if (nuevoDomicilio.getCiudad() == null) {
            throw new NegocioException("La ciudad no puede estar vacia", null);
        }
        if (nuevoDomicilio.getEstado() == null) {
            throw new NegocioException("El estado no puede estar vacio", null);
        }
        if (nuevoDomicilio.getCodigoPostal() == null) {
            throw new NegocioException("El codigo postal no puede estar vacio", null);
        }
        // Validar que los strings no sean tan largos
        if (nuevoDomicilio.getCalle().length() > 100) {
            throw new NegocioException("El nombre de la calle es demasiado largo", null);
        }
        if (nuevoDomicilio.getNumero().length() > 20) {
            throw new NegocioException("El numero del domicilio es demasiado largo", null);
        }
        if (nuevoDomicilio.getColonia().length() > 100) {
            throw new NegocioException("El nombre de la colonia es demasiado largo", null);
        }
        if (nuevoDomicilio.getCiudad().length() > 100) {
            throw new NegocioException("El nombre de la ciudad es demasiado largo", null);
        }
        if (nuevoDomicilio.getEstado().length() > 100) {
            throw new NegocioException("El nombre del estado es demasiado largo", null);
        }
        if (nuevoDomicilio.getCodigoPostal().length() > 10) {
            throw new NegocioException("El codigo postal es demasiado largo", null);
        }
        try {
            int numdomicilio = Integer.parseInt(nuevoDomicilio.getNumero());
            int codigoPostal = Integer.parseInt(nuevoDomicilio.getCodigoPostal());

            return domiciliosDAO.actualizarDomicilio(nuevoDomicilio, Cliente.getIdDomicilio());
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al crear domicilio", ex);
        } catch (NumberFormatException ex) {
            throw new NegocioException("Error al crear domicilio: numero domicilio o codigo postal invalido ", ex);
        }
    }

}