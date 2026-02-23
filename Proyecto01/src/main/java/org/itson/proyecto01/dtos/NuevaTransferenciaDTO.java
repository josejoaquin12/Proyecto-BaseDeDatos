/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.dtos;

import java.time.LocalDateTime;
import org.itson.proyecto01.enums.TipoOperacion;

/**
 * DTO (Data Transfer Object) que representa una nueva transferencia bancaria.
 * Extiende {@link NuevaOperacionDTO} para incluir información adicional
 * específica de las transferencias: cuenta de origen, cuenta de destino y monto.
 *
 * <p>Se utiliza para encapsular los datos de una transferencia y
 * transferirlos entre las capas de la aplicación sin lógica adicional.</p>
 */
public class NuevaTransferenciaDTO extends NuevaOperacionDTO {

    /**
     * Número de cuenta desde la cual se origina la transferencia.
     */
   private String cuentaOrigen;
   
   /**
     * Número de cuenta a la cual se destina la transferencia.
     */
    private String cuentaDestino;
    
    /**
     * Monto de la transferencia.
     */
    private double monto;

    /**
     * Constructor que inicializa una nueva transferencia con sus datos principales.
     *
     * @param fechaHoraOperacion fecha y hora de la operación
     * @param tipoOperacion tipo de operación (debe ser transferencia)
     * @param cuentaOrigen cuenta desde la cual se origina la transferencia
     * @param cuentaDestino cuenta a la cual se destina la transferencia
     * @param monto monto de la transferencia
     */
    public NuevaTransferenciaDTO( LocalDateTime fechaHoraOperacion, TipoOperacion tipoOperacion, String cuentaOrigen, String cuentaDestino, double monto) {
        super( fechaHoraOperacion, tipoOperacion, monto);
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
                      
    }

     /**
     * Obtiene la cuenta de origen de la transferencia.
     *
     * @return cuenta de origen
     */
    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    /**
     * Obtiene la cuenta de destino de la transferencia.
     *
     * @return cuenta de destino
     */
    public String getCuentaDestino() {
        return cuentaDestino;
    }

    /**
     * Obtiene el monto de la transferencia.
     *
     * @return monto de la transferencia
     */
    public double getMonto() {
        return monto;
    }
    
    
    
}
