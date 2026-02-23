/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDateTime;
import org.itson.proyecto01.enums.EstadoCuenta;

/**
 * DTO (Data Transfer Object) que representa los datos necesarios para la
 * creación de una nueva cuenta bancaria.
 *
 */
public class NuevaCuentaDTO {

    /**
     * Fecha y hora en la que se abre la cuenta.
     */
    private LocalDateTime fechaApertura;

    /**
     * Saldo actual de la cuenta. Al crear la cuenta, el saldo inicia en 0.0.
     */
    private Double saldo;

    /**
     * Estado actual de la cuenta. Por defecto, la cuenta se crea en estado
     * ACTIVA.
     */
    private EstadoCuenta estado;

    /**
     * Identificador único del cliente al que pertenece la cuenta.
     */
    private Integer idCliente;

    /**
     * Constructor que inicializa una nueva cuenta bancaria.
     *
     * @param fechaApertura fecha y hora en la que se abre la cuenta
     * @param idCliente identificador del cliente propietario de la cuenta
     */
    public NuevaCuentaDTO(LocalDateTime fechaApertura, Integer idCliente) {
        this.fechaApertura = fechaApertura;
        this.saldo = 0.0;
        this.estado = EstadoCuenta.ACTIVA;
        this.idCliente = idCliente;
    }

    /**
     * Obtiene la fecha y hora de apertura de la cuenta.
     *
     * @return fecha de apertura de la cuenta
     */
    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    /**
     * Obtiene el saldo actual de la cuenta.
     *
     * @return saldo de la cuenta
     */
    public Double getSaldo() {
        return saldo;
    }

    /**
     * Obtiene el estado actual de la cuenta.
     *
     * @return estado de la cuenta
     */
    public EstadoCuenta getEstado() {
        return estado;
    }

    /**
     * Obtiene el identificador del cliente propietario de la cuenta.
     *
     * @return id del cliente
     */
    public Integer getIdCliente() {
        return idCliente;
    }

}
