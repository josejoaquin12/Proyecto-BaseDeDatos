/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDateTime;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
public class NuevaOperacionDTO {

    private LocalDateTime fechaHoraOperacion;
    private TipoOperacion tipoOperacion;
    private double montoTransaccion;

    public NuevaOperacionDTO(LocalDateTime fechaHoraOperacion, TipoOperacion tipoOperacion, double montoTransaccion) {
        this.fechaHoraOperacion = fechaHoraOperacion;
        this.tipoOperacion = tipoOperacion;
        this.montoTransaccion = montoTransaccion;
    }

    public LocalDateTime getFechaHoraOperacion() {
        return fechaHoraOperacion;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public double getMontoTransaccion(){
        return montoTransaccion;
    }
    
}
