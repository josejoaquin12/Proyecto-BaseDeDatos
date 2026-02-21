package org.itson.proyecto01.dtos;

public class NuevoRetiroDTO {

    private double monto;
    private String numeroCuentaOrigen;

    public NuevoRetiroDTO(double monto, String numeroCuentaOrigen) {
        this.monto = monto;
        this.numeroCuentaOrigen = numeroCuentaOrigen;
    }

    public double getMonto() {
        return monto;
    }

    public String getNumeroCuentaOrigen() {
        return numeroCuentaOrigen;
    }
}
