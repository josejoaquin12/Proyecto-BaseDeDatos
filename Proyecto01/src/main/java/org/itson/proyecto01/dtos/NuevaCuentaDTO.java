/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDateTime;
import org.itson.proyecto01.enums.EstadoCuenta;

/**
 *
 * @author elgps
 */
public class NuevaCuentaDTO {
    
     private Integer id;
    private String numeroCuenta;
    private LocalDateTime fechaApertura;
    private Double saldo= 0.0;
    private EstadoCuenta estado;
    private Integer idCliente;

    public NuevaCuentaDTO(Integer id, String numeroCuenta, LocalDateTime fechaApertura, EstadoCuenta estado, Integer idCliente) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.fechaApertura = fechaApertura;
        this.saldo = 0.0;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    public Integer getId() {
        return id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public Double getSaldo() {
        return saldo;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public Integer getIdCliente() {
        return idCliente;
    }
    
    
    
    
}
