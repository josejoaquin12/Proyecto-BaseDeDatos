/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.entidades;

import java.time.LocalDateTime;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 * Clase que representa una transferencia bancaria entre dos cuentas.
 * <p>
 * Esta entidad extiende de {@link Operacion} y especializa el comportamiento
 * para transacciones que involucran el movimiento de fondos desde una
 * {@code cuentaOrigen} hacia una {@code cuentaDestino}.
 * </p>
 * <p>
 * Al ser una extensión de Operacion, el tipo de operación se establece
 * automáticamente como {@link TipoOperacion#TRANSFERENCIA}.
 * </p> * * @author Jesus Omar
 */
public class Transferencia extends Operacion {

    private Integer idTransaccion;
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
    private double monto;

    /**
     * Constructor por defecto.
     * <p>
     * Invoca al constructor de la superclase e inicializa el tipo de operación
     * como {@code TRANSFERENCIA} de forma automática.
     * </p>
     */
    public Transferencia() {
        super();
        this.setTipoOperacion(TipoOperacion.TRANSFERENCIA);
    }

    /**
     * Constructor completo para inicializar una transferencia con todos sus
     * datos.
     * <p>
     * Los datos compartidos como la fecha, el monto y el número de cuenta
     * origen se envían a la superclase {@link Operacion} mediante
     * {@code super()}.
     * </p>
     *
     * * @param idTransaccion Identificador único de la transferencia.
     * @param cuentaOrigen Cuenta que envía el dinero.
     * @param cuentaDestino Cuenta que recibe el dinero.
     * @param monto Monto total a transferir.
     * @param fechaHoraOperacion Marca de tiempo del movimiento.
     * @param tipoOperacion Objeto de tipo de operación (se ignora internamente
     * para forzar TRANSFERENCIA).
     */
    public Transferencia(Integer idTransaccion, Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto, LocalDateTime fechaHoraOperacion, Object tipoOperacion) {
        super(idTransaccion, fechaHoraOperacion, TipoOperacion.TRANSFERENCIA, monto, cuentaOrigen.getNumeroCuenta());
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
    }

    /**
     * Obtiene la cuenta que emite los fondos.
     *
     * @return Objeto {@link Cuenta} de origen.
     */
    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    /**
     * Establece la cuenta que emite los fondos.
     *
     * @param cuentaOrigen Objeto {@link Cuenta} de origen.
     */
    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    /**
     * Obtiene la cuenta que recibe los fondos.
     *
     * @return Objeto {@link Cuenta} de destino.
     */
    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    /**
     * Establece la cuenta que recibe los fondos.
     *
     * @param cuentaDestino Objeto {@link Cuenta} de destino.
     */
    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    /**
     * Obtiene el monto de la transferencia.
     *
     * @return Valor numérico del monto.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Establece el monto de la transferencia.
     *
     * @param monto Cantidad a transferir.
     */
    public void setMonto(float monto) {
        this.monto = monto;
    }

}
