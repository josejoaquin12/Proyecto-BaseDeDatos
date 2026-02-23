/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

/**
 * Clase que representa la entidad Domicilio en el sistema.
 * <p>
 * Esta clase funciona como un Objeto de Persistencia (POJO) que almacena la
 * ubicación física de un cliente. Se relaciona de manera directa con la entidad
 * {@link Cliente} para completar su información de registro y cumplir con los
 * requisitos de apertura de cuenta.
 * </p>
 *
 * @author Jesus Omar
 */
public class Domicilio {

    private Integer id;
    private String calle;
    private String numero;
    private String colonia;
    private String ciudad;
    private String estado;
    private String codigoPostal;

    /**
     * Constructor por defecto.
     */
    public Domicilio() {

    }

    /**
     * Constructor completo para inicializar todos los atributos del domicilio.
     *
     * @param id Identificador único.
     * @param calle Nombre de la calle.
     * @param numero Número de la propiedad.
     * @param colonia Nombre de la colonia.
     * @param ciudad Nombre de la ciudad.
     * @param estado Nombre del estado.
     * @param codigoPostal Código postal de la zona.
     */
    public Domicilio(Integer id, String calle, String numero, String colonia, String ciudad, String estado, String codigoPostal) {
        this.id = id;
        this.calle = calle;
        this.numero = numero;
        this.colonia = colonia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.codigoPostal = codigoPostal;
    }

    /**
     * Obtiene el identificador único del domicilio.
     *
     * @return El ID del domicilio.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Asigna el identificador único del domicilio.
     *
     * @param id El ID a establecer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la calle.
     *
     * @return Cadena con la calle.
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Establece el nombre de la calle.
     *
     * @param calle La calle a asignar.
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     * Obtiene el número del domicilio.
     *
     * @return Cadena con el número.
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Establece el número del domicilio.
     *
     * @param numero El número a asignar.
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Obtiene la colonia.
     *
     * @return Cadena con la colonia.
     */
    public String getColonia() {
        return colonia;
    }

    /**
     * Establece la colonia.
     *
     * @param colonia La colonia a asignar.
     */
    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    /**
     * Obtiene la ciudad de residencia.
     *
     * @return Cadena con la ciudad.
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad de residencia.
     *
     * @param ciudad La ciudad a asignar.
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Obtiene el estado o entidad federativa.
     *
     * @return Cadena con el estado.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado.
     *
     * @param estado El estado a asignar.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el código postal.
     *
     * @return Cadena con el código postal.
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }

    /**
     * Establece el código postal.
     *
     * @param codigoPostal El código postal a asignar.
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

}
