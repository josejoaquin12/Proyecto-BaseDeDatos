/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDateTime;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 *
 * @author Carmen Andrea Lara Osuna
 */
public class NuevaTransferenciaDTO extends NuevaOperacionDTO {

    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private float monto;

    public NuevaTransferenciaDTO(int idOperacion, LocalDateTime fechaHoraOperacion, TipoOperacion tipoOperacion, Cuenta cuentaOrigen, Cuenta cuentaDestino, float monto) {
        super(idOperacion, fechaHoraOperacion, tipoOperacion);
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
                      
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public float getMonto() {
        return monto;
    }
    
    
    
}
