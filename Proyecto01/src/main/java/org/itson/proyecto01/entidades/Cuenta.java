/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.itson.proyecto01.enums.EstadoCuenta;

/**
 * Clase que representa la entidad Cuenta dentro del sistema bancario.
 * <p>
 * Esta clase actúa como un Objeto de Persistencia (Entity) que mapea la
 * información de las cuentas bancarias almacenadas en la base de datos. Incluye
 * detalles operativos como el saldo, el número de cuenta único y su estado
 * actual.
 * </p>
 * <p>
 * La clase sobrescribe el método {@code toString()} para facilitar su uso en
 * componentes de interfaz gráfica (como {@code JComboBox}), devolviendo el
 * número de cuenta como representación textual.
 * </p>
 * *  *
 * [Image of a bank account database schema]
 *
 * * @author Jesus Omar
 */
public class Cuenta {

    private Integer id;
    private String numeroCuenta;
    private LocalDateTime fechaApertura;
    private Double saldo;
    private EstadoCuenta estado;
    private Integer idCliente;

    /**
     * Constructor por defecto para la creación de instancias vacías.
     */
    public Cuenta() {

    }

    /**
     * Constructor completo para inicializar todos los atributos de la cuenta.
     *
     * * @param id Identificador único de la cuenta.
     * @param numeroCuenta Número de identificación bancaria.
     * @param fechaApertura Marca de tiempo de la creación.
     * @param saldo Saldo inicial de la cuenta.
     * @param estado Estado inicial (Activa, Cancelada, etc.).
     * @param idCliente Identificador del propietario de la cuenta.
     */
    public Cuenta(Integer id, String numeroCuenta, LocalDateTime fechaApertura, Double saldo, EstadoCuenta estado, Integer idCliente) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.fechaApertura = fechaApertura;
        this.saldo = saldo;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    /**
     * Obtiene el identificador único de la cuenta.
     *
     * @return El ID de la cuenta.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Asigna el identificador único de la cuenta.
     *
     * @param id El ID a establecer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el número de cuenta bancaria.
     *
     * @return Cadena con el número de cuenta.
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * Establece el número de cuenta bancaria.
     *
     * @param numeroCuenta El número de cuenta a asignar.
     */
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    /**
     * Obtiene la fecha y hora de apertura.
     *
     * @return Objeto {@link LocalDateTime} con la fecha de apertura.
     */
    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    /**
     * Establece la fecha y hora de apertura.
     *
     * @param fechaApertura La fecha a asignar.
     */
    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    /**
     * Obtiene el saldo disponible en la cuenta.
     *
     * @return El saldo actual.
     */
    public Double getSaldo() {
        return saldo;
    }

    /**
     * Actualiza el saldo disponible en la cuenta.
     *
     * @param saldo El nuevo monto de saldo.
     */
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    /**
     * Obtiene el estado operativo de la cuenta.
     *
     * @return El {@link EstadoCuenta} actual.
     */
    public EstadoCuenta getEstado() {
        return estado;
    }

    /**
     * Establece el estado operativo de la cuenta.
     *
     * @param estado El nuevo estado a asignar.
     */
    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el ID del cliente propietario.
     *
     * @return El identificador del cliente.
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * Asigna el ID del cliente propietario a la cuenta.
     *
     * @param idCliente El ID del cliente a vincular.
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Devuelve una representación en cadena de la cuenta.
     * <p>
     * Sobrescribe el comportamiento por defecto para devolver el número de
     * cuenta, lo cual permite que los componentes de la interfaz de usuario
     * muestren la cuenta de forma legible automáticamente.
     * </p>
     *
     * @return El número de cuenta como {@code String}.
     */
    @Override
    public String toString() {
        return numeroCuenta;
    }

}
