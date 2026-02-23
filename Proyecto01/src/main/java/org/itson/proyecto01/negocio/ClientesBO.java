/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.time.LocalDate;
import org.itson.proyecto01.dtos.NuevoClienteDTO;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * <p>
 * Objeto de negocio (BO) para la entidad <b>Cliente</b>. Esta clase implementa la lógica
 * de negocio relacionada con:
 * </p>
 * <ul>
 *   <li>Registro de clientes (validaciones + alta en persistencia).</li>
 *   <li>Actualización de datos de cliente (validaciones + persistencia).</li>
 *   <li>Autenticación por nombre completo y contraseña (BCrypt).</li>
 *   <li>Consulta de cliente por identificador.</li>
 * </ul>
 *
 * <p>
 * Este BO se apoya en {@link IClientesDAO} para realizar las operaciones con la base
 * de datos, y encapsula validaciones como:
 * </p>
 * <ul>
 *   <li>Campos obligatorios no nulos.</li>
 *   <li>Longitud máxima de nombres y apellidos.</li>
 *   <li>Edad mínima de 18 años para registro/actualización.</li>
 *   <li>Reglas básicas de contraseña al registrar.</li>
 * </ul>
 *
 * @author Jesus Omar
 */
public class ClientesBO implements IClientesBO {

    /**
     * DAO de clientes utilizado para operaciones de persistencia.
     */
    private final IClientesDAO clientesDAO;

    /**
     * <p>
     * Construye el BO de clientes recibiendo su dependencia DAO por inyección.
     * </p>
     *
     * @param clientesDAO implementación de {@link IClientesDAO} para operaciones de base de datos.
     */
    public ClientesBO(IClientesDAO clientesDAO) {
        this.clientesDAO = clientesDAO;
    }

    /**
     * <p>
     * Registra un nuevo cliente aplicando reglas de negocio y delegando el guardado al DAO.
     * </p>
     *
     * <p>
     * Reglas principales:
     * </p>
     * <ul>
     *   <li>Todos los campos requeridos deben ser distintos de <code>null</code>.</li>
     *   <li>Nombres y apellidos no deben exceder 100 caracteres.</li>
     *   <li>El cliente debe ser mayor o igual a 18 años.</li>
     *   <li>La contraseña debe tener al menos 6 caracteres.</li>
     * </ul>
     *
     * <p>
     * Nota: Se calcula una contraseña hasheada con <code>BCrypt</code>. (El objeto {@link Cliente}
     * local se construye con hash, pero finalmente el método delega al DAO usando el DTO.)
     * </p>
     *
     * @param nuevoCliente DTO con los datos del cliente a registrar.
     * @param idDomicilio identificador del domicilio asociado al cliente.
     * @return el {@link Cliente} creado y persistido.
     * @throws NegocioException si alguna validación falla o si ocurre un error de persistencia.
     */
    @Override
    public Cliente crearCliente(NuevoClienteDTO nuevoCliente, Integer idDomicilio) throws NegocioException {
        // Limite de edad para validar que el cliente sea mayor de edad
        LocalDate edadMinima = LocalDate.now().minusYears(18);
        // Validaciones
        if (nuevoCliente.getNombres() == null) {
            throw new NegocioException("El nombre no puede estar vacio", null);
        }
        if (nuevoCliente.getApellidoP() == null) {
            throw new NegocioException("El apellido paterno no puede estar vacio", null);
        }
        if (nuevoCliente.getApelidoM() == null) {
            throw new NegocioException("El apellido materno no puede estar vacio", null);
        }
        if (nuevoCliente.getFechaNacimiento() == null) {
            throw new NegocioException("La fecha de nacimiento no puede estar vacia", null);
        }
        if (nuevoCliente.getContrasenia() == null) {
            throw new NegocioException("La contrasena no puede estar vacia", null);
        }
        if (nuevoCliente.getNombres().length() > 100) {
            throw new NegocioException("El nombre es demasiado largo", null);
        }
        if (nuevoCliente.getApellidoP().length() > 100) {
            throw new NegocioException("El apellido es demasiado largo", null);
        }
        if (nuevoCliente.getApelidoM().length() > 100) {
            throw new NegocioException("El apellido es demasiado largo", null);
        }
        if (nuevoCliente.getFechaNacimiento().isAfter(edadMinima)) {
            throw new NegocioException("La edad minima para abrir una cuenta es de 18", null);
        }
        if (nuevoCliente.getContrasenia().length() < 6) {
            throw new NegocioException("La contrasena debe contener minimo 6 caracteres", null);
        }
        String passwordHasheada = BCrypt.hashpw(nuevoCliente.getContrasenia(), BCrypt.gensalt());
        Cliente cliente = new Cliente(null,
                nuevoCliente.getNombres(),
                nuevoCliente.getApellidoP(),
                nuevoCliente.getApelidoM(),
                nuevoCliente.getFechaNacimiento(),
                passwordHasheada,
                nuevoCliente.getFechaRegistro(),
                nuevoCliente.getEdad(),
                idDomicilio
        );
        try {
            cliente = this.clientesDAO.crearCliente(idDomicilio, nuevoCliente, idDomicilio);
            return cliente;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al registrarse", null);
        }

    }

    /**
     * <p>
     * Actualiza un cliente aplicando validaciones de negocio y delegando la actualización al DAO.
     * </p>
     *
     * <p>
     * Reglas principales:
     * </p>
     * <ul>
     *   <li>Campos requeridos no deben ser <code>null</code>.</li>
     *   <li>Nombres y apellidos no deben exceder 100 caracteres.</li>
     *   <li>El cliente debe tener al menos 18 años.</li>
     * </ul>
     *
     * @param id identificador del cliente a actualizar.
     * @param nuevoCliente DTO con los nuevos datos del cliente.
     * @param idDomicilio identificador del domicilio asociado.
     * @return el {@link Cliente} actualizado; puede ser <code>null</code> si el DAO no actualiza filas.
     * @throws NegocioException si alguna validación falla o si ocurre un error en persistencia.
     */
    @Override
    public Cliente actualizarCliente(Integer id, NuevoClienteDTO nuevoCliente, Integer idDomicilio) throws NegocioException {
        // Limite de edad para validar que el cliente sea mayor de edad
        LocalDate edadMinima = LocalDate.now().minusYears(18);
        // Validaciones
        if (nuevoCliente.getNombres() == null) {
            throw new NegocioException("El nombre no puede estar vacio", null);
        }
        if (nuevoCliente.getApellidoP() == null) {
            throw new NegocioException("El apellido paterno no puede estar vacio", null);
        }
        if (nuevoCliente.getApelidoM() == null) {
            throw new NegocioException("El apellido materno no puede estar vacio", null);
        }
        if (nuevoCliente.getFechaNacimiento() == null) {
            throw new NegocioException("La fecha de nacimiento no puede estar vacia", null);
        }
        if (nuevoCliente.getNombres().length() > 100) {
            throw new NegocioException("El nombre es demasiado largo", null);
        }
        if (nuevoCliente.getApellidoP().length() > 100) {
            throw new NegocioException("El apellido es demasiado largo", null);
        }
        if (nuevoCliente.getApelidoM().length() > 100) {
            throw new NegocioException("El apellido es demasiado largo", null);
        }
        if (nuevoCliente.getFechaNacimiento().isAfter(edadMinima)) {
            throw new NegocioException("La edad minima para abrir una cuenta es de 18", null);
        }

        Cliente cliente = new Cliente(id,
                nuevoCliente.getNombres(),
                nuevoCliente.getApellidoP(),
                nuevoCliente.getApelidoM(),
                nuevoCliente.getFechaNacimiento(),
                nuevoCliente.getFechaRegistro(),
                nuevoCliente.getEdad(),
                idDomicilio
        );
        try {
            cliente = clientesDAO.actualizarCliente(id, nuevoCliente, idDomicilio);
            return cliente;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al registrarse" + ex.getMessage(), null);
        }

    }

    /**
     * <p>
     * Autentica a un cliente validando su nombre completo y contraseña.
     * </p>
     *
     * <p>
     * Flujo:
     * </p>
     * <ul>
     *   <li>Valida que <code>nombreCompleto</code> y <code>password</code> no sean <code>null</code>.</li>
     *   <li>Consulta el hash almacenado en BD mediante {@link IClientesDAO#obtenerHashPorNombreCompleto(String)}.</li>
     *   <li>Compara la contraseña en texto plano con el hash usando <code>BCrypt.checkpw</code>.</li>
     *   <li>Si coincide, obtiene y retorna el ID del cliente con {@link IClientesDAO#verificarCredenciales(String)}.</li>
     * </ul>
     *
     * @param nombreCompleto nombre completo del cliente (formato concatenado usado en BD).
     * @param password contraseña en texto plano ingresada por el usuario.
     * @return el ID del cliente si las credenciales son correctas; si no, retorna <code>-1</code>.
     * @throws NegocioException si ocurre un error al consultar en persistencia o si los datos son inválidos.
     */
    @Override
    public int autenticarNombreCompletoPassword(String nombreCompleto, String password)
            throws NegocioException {

        if (nombreCompleto == null || password == null) {
            throw new NegocioException("Datos inválidos", null);
        }

        try {
            String hashBD = clientesDAO.obtenerHashPorNombreCompleto(nombreCompleto);

            if (hashBD == null) {
                return -1;
            }

            if (!BCrypt.checkpw(password, hashBD)) {
                return -1;
            }

            return clientesDAO.verificarCredenciales(nombreCompleto);

        } catch (PersistenciaException ex) {
            throw new NegocioException("Error al autenticar", ex);
        }
    }

    /**
     * <p>
     * Obtiene un cliente por su identificador.
     * </p>
     *
     * @param idCliente identificador del cliente a consultar.
     * @return {@link Cliente} encontrado.
     * @throws NegocioException si el cliente no existe o si ocurre un error al consultar en persistencia.
     */
    @Override
    public Cliente obtenerClientePorId(Integer idCliente) throws NegocioException {
        try {
            Cliente cliente = this.clientesDAO.obtenerClientePorId(idCliente);
            if (cliente == null) {
                throw new NegocioException("El cliente no existe", null);

            }

            return cliente;
        } catch (PersistenciaException ex) {
            throw new NegocioException("Error, no se encontro el cliente", null);
        }
    }
}