/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.control;

import org.itson.proyecto01.entidades.Cliente;

/**
 * Clase que gestiona la sesión activa del usuario en la aplicación.
 * <p>
 * Implementa el patrón de diseño <b>Singleton</b> para garantizar que exista
 * una única instancia de la sesión en todo el ciclo de vida de la ejecución.
 * Permite almacenar y recuperar la información del {@link Cliente} autenticado
 * desde cualquier controlador del sistema sin necesidad de pasar referencias
 * constantes.
 * </p>
 *
 * * @author Jesus Omar
 */
public class SesionControl {

    private static SesionControl sesion;
    private Cliente clienteIniciado;

    /**
     * * Constructor privado para evitar la instanciación externa.
     * <p>
     * Fuerza el uso del método {@link #getSesion()} para obtener la instancia
     * única.
     * </p>
     */
    private SesionControl() {
    } // Este constructor privado no permite que se cree otra instancia en otro lado

    /**
     * Proporciona acceso a la instancia única de la sesión.
     * <p>
     * Utiliza la técnica de inicialización bajo demanda (Lazy Initialization):
     * si la instancia no existe, la crea; de lo contrario, devuelve la
     * existente.
     * </p>
     *
     * * @return La instancia única de {@link SesionControl}.
     * @return 
     */
    public static SesionControl getSesion() {
        if (sesion == null) {
            sesion = new SesionControl(); // Si no hay una sesion iniciada crea una
        }
        return sesion;
    }

    /**
     * Almacena el objeto cliente tras una autenticación exitosa.
     *
     * @param cliente El objeto {@link Cliente} que representa al usuario
     * actual.
     */
    public void guardarSesion(Cliente cliente) {
        this.clienteIniciado = cliente; // Guarda al cliente que inicio sesion en el form
    }

    /**
     * Obtiene el objeto cliente asociado a la sesión actual.
     *
     * @return El {@link Cliente} autenticado, o {@code null} si no hay una
     * sesión activa.
     */
    public Cliente getCliente() {
        return clienteIniciado; // Regresa al cliente que inicio sesion
    }

    /**
     * Finaliza la sesión actual del usuario.
     * <p>
     * Elimina la referencia al cliente iniciado, dejando el sistema listo para
     * una nueva autenticación.
     * </p>
     */
    public void cerrarSesion() {
        this.clienteIniciado = null; // Cierra sesion eliminando al cliente que estaba iniciado
    }

}
