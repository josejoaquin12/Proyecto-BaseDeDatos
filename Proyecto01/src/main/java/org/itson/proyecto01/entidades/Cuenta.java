/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.itson.proyecto01.enums.EstadoCuenta;

/**
 *
 * @author Jesus Omar
 */
public class Cuenta {
    
    private Integer id;
    private String numeroCuenta;
    private LocalDateTime fechaApertura;
    private Double saldo;
    private EstadoCuenta estado;
    private Integer idCliente;
    
    public Cuenta(){
        
    }

    public Cuenta(Integer id, String numeroCuenta, LocalDateTime fechaApertura, Double saldo, EstadoCuenta estado, Integer idCliente) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.fechaApertura = fechaApertura;
        this.saldo = saldo;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
    
    @Override
    public String toString() {
        return numeroCuenta;
    }

    
}
