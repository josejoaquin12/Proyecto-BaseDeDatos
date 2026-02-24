package org.itson.proyecto01.dtos;

/**
 * DTO (Data Transfer Object) que representa la información de un nuevo retiro bancario.
 * Contiene los datos básicos necesarios para realizar un retiro desde una cuenta.
 *
 * <p>Se utiliza para transferir datos entre capas de la aplicación
 * sin incluir lógica de negocio.</p>
 */
public class NuevoRetiroDTO {

    /**
     * Monto del retiro.
     */
    private double monto;

    /**
     * Número de cuenta desde la cual se realiza el retiro.
     */
    private String numeroCuentaOrigen;

    /**
     * Constructor que inicializa un nuevo retiro con sus datos principales.
     *
     * @param monto monto del retiro
     * @param numeroCuentaOrigen número de cuenta de origen
     */
    public NuevoRetiroDTO(double monto, String numeroCuentaOrigen) {
        this.monto = monto;
        this.numeroCuentaOrigen = numeroCuentaOrigen;
    }

    /**
     * Obtiene el monto del retiro.
     *
     * @return monto del retiro
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Obtiene el número de cuenta de origen del retiro.
     *
     * @return número de cuenta de origen
     */
    public String getNumeroCuentaOrigen() {
        return numeroCuentaOrigen;
    }
}

