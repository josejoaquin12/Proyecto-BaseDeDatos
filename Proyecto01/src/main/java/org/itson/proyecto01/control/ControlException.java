/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.control;

/**
 * Excepción personalizada para la gestión de errores en la capa de control.
 * <p>
 * Esta clase se utiliza para encapsular y señalizar errores específicos que 
 * ocurren durante la validación de interfaces, la lógica de navegación o la 
 * preparación de datos antes de ser enviados a la capa de negocio.
 * </p>
 * <p>
 * Al extender de {@link Exception}, es una excepción de tipo "checked", lo que 
 * obliga a los métodos que la lanzan a declararla en su firma, asegurando un 
 * manejo robusto de errores en el flujo de la interfaz de usuario.
 * </p>
 * * 
 * * @author joset
 */
public class ControlException extends Exception{
    public ControlException(String message, Throwable cause) {
        super(message, cause);
    }
}
