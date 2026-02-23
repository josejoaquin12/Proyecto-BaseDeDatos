/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

import java.time.LocalDateTime;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 * Clase que representa una operación financiera genérica en el sistema.
 * <p>
 * Esta entidad funciona como un registro histórico de cualquier movimiento
 * monetario realizado (ej. Transferencias o Retiros). Permite centralizar los
 * datos comunes de las transacciones para facilitar la generación de estados de
 * cuenta y consultas de historial.
 * </p>
 *
 * @author Jesus Omar
 */
public class Operacion {

    private int idOperacion;
    private LocalDateTime fechaHoraOperacion;
    private TipoOperacion tipoOperacion;
    private double monto;
    private String numeroCuenta;

    /**
     * Constructor por defecto para la creación de instancias vacías.
     */
    public Operacion() {
    }

    /**
     * Constructor completo para inicializar todos los atributos de una
     * operación.
     *
     * @param idOperacion Identificador único de la transacción.
     * @param fechaHoraOperacion Marca de tiempo de la ejecución.
     * @param tipoOperacion Tipo de movimiento (Transferencia, Retiro, etc.).
     * @param monto Monto total de la operación.
     * @param numeroCuenta Cuenta asociada al registro.
     */
    public Operacion(int idOperacion, LocalDateTime fechaHoraOperacion,
            TipoOperacion tipoOperacion, double monto, String numeroCuenta) {
        this.idOperacion = idOperacion;
        this.fechaHoraOperacion = fechaHoraOperacion;
        this.tipoOperacion = tipoOperacion;
        this.monto = monto;
        this.numeroCuenta = numeroCuenta;
    }

    /**
     * Obtiene el identificador único de la operación.
     *
     * @return El ID de la operación.
     */
    public int getIdOperacion() {
        return idOperacion;
    }

    /**
     * Establece el identificador único de la operación.
     *
     * @param idOperacion El ID a asignar.
     */
    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    /**
     * Obtiene la fecha y hora en la que se realizó la operación.
     *
     * @return Objeto {@link LocalDateTime} con el momento de la transacción.
     */
    public LocalDateTime getFechaHoraOperacion() {
        return fechaHoraOperacion;
    }

    /**
     * Define la fecha y hora de la operación.
     *
     * @param fechaHoraOperacion La marca de tiempo a asignar.
     */
    public void setFechaHoraOperacion(LocalDateTime fechaHoraOperacion) {
        this.fechaHoraOperacion = fechaHoraOperacion;
    }

    /**
     * Obtiene el tipo de operación realizada.
     *
     * @return El {@link TipoOperacion} correspondiente.
     */
    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Establece el tipo de operación.
     *
     * @param tipoOperacion El tipo de movimiento a asignar.
     */
    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * Obtiene el monto total de la transacción.
     *
     * @return Valor numérico del monto.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Establece el monto de la operación.
     *
     * @param monto El monto a asignar.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Obtiene el número de cuenta vinculado a la operación.
     *
     * @return Cadena con el número de cuenta.
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * Establece el número de cuenta vinculado a la operación.
     *
     * @param numeroCuenta El número de cuenta a asignar.
     */
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

}
