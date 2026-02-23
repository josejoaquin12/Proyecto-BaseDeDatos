/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase que representa la entidad Cliente en el sistema.
 * <p>
 * Esta clase actúa como un objeto de transferencia de datos de persistencia
 * (POJO) que mapea la información de los usuarios registrados, incluyendo sus
 * datos personales, credenciales de acceso y la relación con su domicilio.
 * </p> * * @author Jesus Omar
 */
public class Cliente {

    private Integer id;
    private String nombres;
    private String apellidoP;
    private String apellidoM;
    private LocalDate fechaNacimiento;
    private String contrasenia;
    private LocalDateTime fechaRegistro;
    private Integer edad;
    private Integer idDomicilio;

    /**
     * Constructor por defecto.
     */
    public Cliente() {

    }

    /**
     * Constructor completo para inicializar un cliente con todos sus atributos,
     * incluyendo la contraseña.
     *
     * * @param id Identificador único.
     * @param nombres Nombres del cliente.
     * @param apellidoP Apellido paterno.
     * @param apellidoM Apellido materno.
     * @param fechaNacimiento Fecha de nacimiento.
     * @param contrasenia Contraseña de acceso.
     * @param fechaRegistro Marca de tiempo del registro.
     * @param edad Edad del cliente.
     * @param idDomicilio ID del domicilio vinculado.
     */
    public Cliente(Integer id, String nombres, String apellidoP, String apellidoM, LocalDate fechaNacimiento, String contrasenia, LocalDateTime fechaRegistro, Integer edad, Integer idDomicilio) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.fechaNacimiento = fechaNacimiento;
        this.contrasenia = contrasenia;
        this.fechaRegistro = fechaRegistro;
        this.edad = edad;
        this.idDomicilio = idDomicilio;
    }

    /**
     * Constructor para inicializar un cliente sin incluir la contraseña.
     * <p>
     * Útil para consultas de visualización de perfil o listados donde no se
     * requiere el manejo de credenciales por seguridad.
     * </p>
     *
     * * @param id Identificador único.
     * @param nombres Nombres del cliente.
     * @param apellidoP Apellido paterno.
     * @param apellidoM Apellido materno.
     * @param fechaNacimiento Fecha de nacimiento.
     * @param fechaRegistro Marca de tiempo del registro.
     * @param edad Edad del cliente.
     * @param idDomicilio ID del domicilio vinculado.
     */
    public Cliente(Integer id, String nombres, String apellidoP, String apellidoM, LocalDate fechaNacimiento, LocalDateTime fechaRegistro, Integer edad, Integer idDomicilio) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.edad = edad;
        this.idDomicilio = idDomicilio;
    }

    /**
     * Asigna el identificador único del cliente. Generalmente usado por el
     * framework de persistencia tras un insert.
     *
     * @param id Identificador a establecer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador único del cliente.
     *
     * @return {@code Integer} con el ID de la base de datos.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Obtiene el o los nombres del cliente.
     *
     * @return {@code String} con los nombres registrados.
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Establece el nombre del cliente.
     *
     * @param nombres Cadena con el nombre del usuario.
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Obtiene el primer apellido del cliente.
     *
     * @return {@code String} con el apellido paterno.
     */
    public String getApellidoP() {
        return apellidoP;
    }

    /**
     * Establece el apellido paterno del cliente.
     *
     * @param apellidoP Cadena con el apellido paterno.
     */
    public void setApellidoP(String apellidoP) {
        this.apellidoP = apellidoP;
    }

    /**
     * Obtiene el segundo apellido del cliente.
     *
     * @return {@code String} con el apellido materno.
     */
    public String getApellidoM() {
        return apellidoM;
    }

    /**
     * Establece el apellido materno del cliente.
     *
     * @param apellidoM Cadena con el apellido materno.
     */
    public void setApellidoM(String apellidoM) {
        this.apellidoM = apellidoM;
    }

    /**
     * Obtiene la fecha de nacimiento del cliente.
     *
     * @return Objeto {@link LocalDate} con la fecha.
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento del cliente.
     *
     * @param fechaNacimiento Objeto {@link LocalDate} a registrar.
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene la contraseña almacenada del cliente.
     *
     * @return {@code String} con la contraseña (preferiblemente hasheada).
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * Establece la contraseña de acceso del cliente.
     *
     * @param contrasenia Cadena con la nueva contraseña.
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * Obtiene la marca de tiempo exacta del registro del cliente.
     *
     * @return Objeto {@link LocalDateTime} con la fecha y hora de creación.
     */
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Establece la fecha de registro del cliente.
     *
     * @param fechaRegistro Objeto {@link LocalDateTime} con el momento del
     * alta.
     */
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Obtiene la edad del cliente calculada al momento de la consulta o
     * registro.
     *
     * @return {@code Integer} con la edad en años.
     */
    public Integer getEdad() {
        return edad;
    }

    /**
     * Establece la edad del cliente.
     *
     * @param edad Valor entero de la edad.
     */
    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    /**
     * Obtiene el ID del domicilio vinculado a este cliente.
     *
     * @return {@code Integer} que referencia a la tabla Domicilios.
     */
    public Integer getIdDomicilio() {
        return idDomicilio;
    }

    /**
     * Vincula al cliente con un registro de domicilio específico.
     *
     * @param idDomicilio ID de la llave foránea de domicilio.
     */
    public void setIdDomicilio(Integer idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

}
