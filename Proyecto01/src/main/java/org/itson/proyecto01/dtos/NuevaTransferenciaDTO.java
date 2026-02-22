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
public class NuevaTransferenciaDTO extends NuevaOperacionDTO {


   private String cuentaOrigen;
    private String cuentaDestino;
    private double monto;

    public NuevaTransferenciaDTO( LocalDateTime fechaHoraOperacion, TipoOperacion tipoOperacion, String cuentaOrigen, String cuentaDestino, double monto) {
        super( fechaHoraOperacion, tipoOperacion, monto);
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
                      
    }

    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }

    public double getMonto() {
        return monto;
    }
    
    
    
}
