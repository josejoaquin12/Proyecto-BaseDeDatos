/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

/**
 * DTO (Data Transfer Object) que representa la información de un nuevo
 * domicilio. Contiene los datos básicos de dirección asociados a un cliente.
 *
 * <p>
 * Se utiliza para transferir datos de domicilio entre capas de la aplicación
 * sin incluir lógica de negocio.</p>
 */
public class NuevoDomicilioDTO {

    /**
     * Nombre de la calle del domicilio.
     */
    private String calle;

    /**
     * Número del domicilio (puede incluir interior o referencias).
     */
    private String numero;

    /**
     * Colonia o barrio del domicilio.
     */
    private String colonia;

    /**
     * Ciudad en la que se ubica el domicilio.
     */
    private String ciudad;

    /**
     * Estado o entidad federativa del domicilio.
     */
    private String estado;

    /**
     * Código postal del domicilio.
     */
    private String codigoPostal;

    /**
     * Constructor que inicializa un nuevo domicilio con sus datos principales.
     *
     * @param calle nombre de la calle
     * @param numero número del domicilio
     * @param colonia colonia o barrio
     * @param ciudad ciudad del domicilio
     * @param estado estado o entidad federativa
     * @param codigoPostal código postal
     */
    public NuevoDomicilioDTO(String calle, String numero, String colonia,
            String ciudad, String estado, String codigoPostal) {
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
    }

    /**
     * Obtiene la calle del domicilio.
     *
     * @return calle del domicilio
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Obtiene el número del domicilio.
     *
     * @return número del domicilio
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Obtiene la colonia del domicilio.
     *
     * @return colonia del domicilio
     */
    public String getColonia() {
        return colonia;
    }

    /**
     * Obtiene la ciudad del domicilio.
     *
     * @return ciudad del domicilio
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Obtiene el estado del domicilio.
     *
     * @return estado del domicilio
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Obtiene el código postal del domicilio.
     *
     * @return código postal del domicilio
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }
}
