package org.itson.proyecto01.entidades;

import java.time.LocalDateTime;

public class Retiro {

    private String numeroCuentaOrigen;
    private double monto;
    private LocalDateTime fechaHora;
    private String tipoOperacion;
    private String folio;
    private String estado;
    private String contrasena;
    private LocalDateTime fechaExpiracion;

    public Retiro(String numeroCuentaOrigen,double monto,String contrasena,LocalDateTime fechaHora,String tipoOperacion,String folio,String estado) {
        this.numeroCuentaOrigen  = numeroCuentaOrigen;
        this.contrasena  = contrasena;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.tipoOperacion = tipoOperacion;
        this.folio = folio;
        this.estado = estado;
        this.fechaExpiracion = this.fechaHora.plusMinutes(10);
    }
    public Retiro(String contrasena,String folio) {
        this.contrasena  = contrasena;
        this.folio = folio;
    }
    public Retiro() {

    }



    public String getNumeroCuentaOrigen() {
        return numeroCuentaOrigen;
    }

    public void setNumeroCuentaOrigen(String numeroCuentaOrigen) {
        this.numeroCuentaOrigen = numeroCuentaOrigen;
    }
    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
     
}
