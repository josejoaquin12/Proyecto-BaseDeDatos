/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

import java.time.LocalDateTime;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 *
 * @author elgps
 */
public class Transferencia extends Operacion{
    
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private double monto;
    
    public Transferencia(){
        super();
        this.setTipoOperacion(TipoOperacion.TRANSFERENCIA);    
    }

    public Transferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto, LocalDateTime fechaHoraOperacion, Object tipoOperacion) {
        super( fechaHoraOperacion, TipoOperacion.TRANSFERENCIA);
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }
    
    
}
