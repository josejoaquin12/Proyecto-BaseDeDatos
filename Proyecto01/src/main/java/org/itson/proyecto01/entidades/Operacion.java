/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 *
 * @author elgps
 */ 
public abstract class Operacion {
    private int idOperacion;
    private LocalDateTime fechaHoraOperacion; 
    private TipoOperacion tipoOperacion; 

    public Operacion() {
    }

    public Operacion( LocalDateTime fechaHoraOperacion, TipoOperacion tipoOperacion) {
        this.fechaHoraOperacion = fechaHoraOperacion;
        this.tipoOperacion = tipoOperacion;
    }

    public int getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    public LocalDateTime getFechaHoraOperacion() {
        return fechaHoraOperacion;
    }

    public void setFechaHoraOperacion(LocalDateTime fechaHoraOperacion) {
        this.fechaHoraOperacion = fechaHoraOperacion;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    
    
}
