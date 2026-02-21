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

    private LocalDateTime fechaApertura;
    private Double saldo;
    private EstadoCuenta estado;
    private Integer idCliente;

    public NuevaCuentaDTO(LocalDateTime fechaApertura, Integer idCliente) {
        this.fechaApertura = fechaApertura;
        this.saldo = 0.0;
        this.estado = EstadoCuenta.ACTIVA;
        this.idCliente = idCliente;
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
