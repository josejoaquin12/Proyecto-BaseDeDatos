/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDateTime;
import org.itson.proyecto01.enums.TipoOperacion;


/**
 * DTO (Data Transfer Object) que representa una nueva operación bancaria.
 * Contiene la información esencial de la transacción: fecha y hora,
 * tipo de operación y monto.
 *
 * <p>Se utiliza para transferir datos entre capas de la aplicación
 * sin exponer la lógica interna.</p>
 */


public class NuevaOperacionDTO {

    /**
     * Fecha y hora en que se realizó la operación.
     */
    private LocalDateTime fechaHoraOperacion;
    
    /**
     * Tipo de operación realizada (ejemplo: alta de cuenta, retiro, transferencia).
     */
    private TipoOperacion tipoOperacion;
    
    /**
     * Monto de la transacción asociada a la operación.
     */
    private double montoTransaccion;

    
    /**
     * Constructor que inicializa una nueva operación con sus datos principales.
     *
     * @param fechaHoraOperacion fecha y hora de la operación
     * @param tipoOperacion tipo de operación realizada
     * @param montoTransaccion monto de la transacción
     */
    public NuevaOperacionDTO(LocalDateTime fechaHoraOperacion, TipoOperacion tipoOperacion, double montoTransaccion) {
        this.fechaHoraOperacion = fechaHoraOperacion;
        this.tipoOperacion = tipoOperacion;
        this.montoTransaccion = montoTransaccion;
    }

    /**
     * Obtiene la fecha y hora de la operación.
     *
     * @return fecha y hora de la operación
     */
    public LocalDateTime getFechaHoraOperacion() {
        return fechaHoraOperacion;
    }
/**
     * Obtiene el tipo de operación realizada.
     *
     * @return tipo de operación
     */
    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Obtiene el monto de la transacción.
     *
     * @return monto de la transacción
     */
    public double getMontoTransaccion(){
        return montoTransaccion;
    }
    
}
