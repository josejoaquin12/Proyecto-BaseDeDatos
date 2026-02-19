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

    private int idOperacion;
    private LocalDateTime fechaHoraOperacion;
    private TipoOperacion tipoOperacion;

    public NuevaOperacionDTO(int idOperacion, LocalDateTime fechaHoraOperacion, TipoOperacion tipoOperacion) {
        this.idOperacion = idOperacion;
        this.fechaHoraOperacion = fechaHoraOperacion;
        this.tipoOperacion = tipoOperacion;
    }

    public int getIdOperacion() {
        return idOperacion;
    }

    public LocalDateTime getFechaHoraOperacion() {
        return fechaHoraOperacion;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    
    
}
