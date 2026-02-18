/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

import java.util.GregorianCalendar;

/**
 *
 * @author elgps
 */
public abstract class Operacion {
    private int idOperacion;
    private GregorianCalendar fechaHoraOperacion; 
    private TipoOperacion tipoOperacion; 

    public Operacion() {
    }

    public Operacion(int idOperacion, GregorianCalendar fechaHoraOperacion, TipoOperacion tipoOperacion) {
        this.idOperacion = idOperacion;
        this.fechaHoraOperacion = fechaHoraOperacion;
        this.tipoOperacion = tipoOperacion;
    }

    public int getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    public GregorianCalendar getFechaHoraOperacion() {
        return fechaHoraOperacion;
    }

    public void setFechaHoraOperacion(GregorianCalendar fechaHoraOperacion) {
        this.fechaHoraOperacion = fechaHoraOperacion;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    
    
}
