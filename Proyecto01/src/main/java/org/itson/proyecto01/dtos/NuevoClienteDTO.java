/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDateTime;

/**
 *
 * @author Jesus Omar
 */
public class NuevoClienteDTO {
    
    private String nombres;
    private String apellidoP;
    private String apelidoM;
    private LocalDateTime fechaNacimiento;
    private String contrasenia;
    private Integer idDomicilio;

    public NuevoClienteDTO(String nombres, String apellidoP, String apelidoM, LocalDateTime fechaNacimiento, String contrasenia, Integer idDomicilio) {
        this.nombres = nombres;
        this.apellidoP = apellidoP;
        this.apelidoM = apelidoM;
        this.fechaNacimiento = fechaNacimiento;
        this.contrasenia = contrasenia;
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

    public LocalDateTime getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public Integer getIdDomicilio() {
        return idDomicilio;
    }
    
}
