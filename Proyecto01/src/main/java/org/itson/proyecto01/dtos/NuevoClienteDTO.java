/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) que representa la información de un nuevo cliente.
 * Contiene los datos personales y de registro necesarios para dar de alta a un
 * cliente en el sistema bancario.
 *
 * <p>
 * Se utiliza para transferir datos entre capas de la aplicación sin incluir
 * lógica de negocio.</p>
 */
public class NuevoClienteDTO {

    /**
     * Nombres del cliente.
     */
    private String nombres;

    /**
     * Apellido paterno del cliente.
     */
    private String apellidoP;

    /**
     * Apellido materno del cliente.
     */
    private String apelidoM;

    /**
     * Fecha de nacimiento del cliente.
     */
    private LocalDate fechaNacimiento;

    /**
     * Contraseña del cliente para acceso al sistema.
     */
    private String contrasenia;

    /**
     * Fecha y hora en que se registró el cliente en el sistema.
     */
    private LocalDateTime fechaRegistro;

    /**
     * Id del domicilio asociado al cliente.
     */
    private Integer idDomicilio;

    /**
     * Edad del cliente.
     */
    private Integer edad;

    /**
     * Constructor que inicializa un nuevo cliente con todos sus datos,
     * incluyendo contraseña.
     *
     * @param nombres nombres del cliente
     * @param apellidoP apellido paterno del cliente
     * @param apelidoM apellido materno del cliente
     * @param fechaNacimiento fecha de nacimiento del cliente
     * @param contrasenia contraseña del cliente
     * @param fechaRegistro fecha y hora de registro
     * @param edad edad del cliente
     * @param idDomicilio identificador del domicilio asociado
     */
    public NuevoClienteDTO(String nombres, String apellidoP, String apelidoM, LocalDate fechaNacimiento, String contrasenia, LocalDateTime fechaRegistro, Integer edad, Integer idDomicilio) {
        this.nombres = nombres;
        this.apellidoP = apellidoP;
        this.apelidoM = apelidoM;
        this.fechaNacimiento = fechaNacimiento;
        this.contrasenia = contrasenia;
        this.fechaRegistro = fechaRegistro;
        this.edad = edad;
        this.idDomicilio = idDomicilio;
    }

    /**
     * Constructor que inicializa un nuevo cliente sin contraseña.
     *
     * @param nombres nombres del cliente
     * @param apellidoP apellido paterno del cliente
     * @param apelidoM apellido materno del cliente
     * @param fechaNacimiento fecha de nacimiento del cliente
     * @param fechaRegistro fecha y hora de registro
     * @param edad edad del cliente
     * @param idDomicilio identificador del domicilio asociado
     */
    public NuevoClienteDTO(String nombres, String apellidoP, String apelidoM, LocalDate fechaNacimiento, LocalDateTime fechaRegistro, Integer edad, Integer idDomicilio) {
        this.nombres = nombres;
        this.apellidoP = apellidoP;
        this.apelidoM = apelidoM;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.edad = edad;
        this.idDomicilio = idDomicilio;
    }

    /**
     * Obtiene los nombres del cliente.
     *
     * @return nombres del cliente
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Obtiene el apellido paterno del cliente.
     *
     * @return apellido paterno
     */
    public String getApellidoP() {
        return apellidoP;
    }

    /**
     * Obtiene el apellido materno del cliente.
     *
     * @return apellido materno
     */
    public String getApelidoM() {
        return apelidoM;
    }

    /**
     * Obtiene la fecha de nacimiento del cliente.
     *
     * @return fecha de nacimiento
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Obtiene la contraseña del cliente.
     *
     * @return contraseña
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * Obtiene la edad del cliente.
     *
     * @return edad
     */
    public Integer getEdad() {
        return edad;
    }

    /**
     * Obtiene la fecha y hora de registro del cliente.
     *
     * @return fecha y hora de registro
     */
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Obtiene el identificador del domicilio asociado al cliente.
     *
     * @return identificador del domicilio
     */
    public Integer getIdDomicilio() {
        return idDomicilio;
    }
}
