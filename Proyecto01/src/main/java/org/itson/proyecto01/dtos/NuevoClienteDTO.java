/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Jesus Omar
 */
public class NuevoClienteDTO {
    
    private String nombres;
    private String apellidoP;
    private String apelidoM;
    private LocalDate fechaNacimiento;
    private String contrasenia;
    private LocalDateTime fechaRegistro;
    private Integer idDomicilio;
    private Integer edad;

    public NuevoClienteDTO(String nombres, String apellidoP, String apelidoM, LocalDate fechaNacimiento, String contrasenia,LocalDateTime fechaRegistro,Integer edad, Integer idDomicilio) {
        this.nombres = nombres;
        this.apellidoP = apellidoP;
        this.apelidoM = apelidoM;
        this.fechaNacimiento = fechaNacimiento;
        this.contrasenia = contrasenia;
        this.fechaRegistro = fechaRegistro;
        this.edad = edad;
        this.idDomicilio = idDomicilio;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidoP() {
        return apellidoP;
    }

    public String getApelidoM() {
        return apelidoM;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getContrasenia() {
        return contrasenia;
    }
    public Integer getEdad() {
        return edad;
    }
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public Integer getIdDomicilio() {
        return idDomicilio;
    }
    
}
