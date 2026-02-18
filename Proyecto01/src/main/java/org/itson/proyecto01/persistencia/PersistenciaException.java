package org.itson.proyecto01.persistencia;
/**
 * Representa un error ocurrido en la capa de persistencia
 * @author LABORATORIOS
 */
public class PersistenciaException extends Exception{

    public PersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
