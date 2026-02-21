/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.control;

import org.itson.proyecto01.entidades.Cliente;

/**
 *
 * @author elgps
 */
public class SesionControl {
    
    private static SesionControl sesion;
    private Cliente clienteIniciado;
    
    private SesionControl(){} // Este constructor privado no permite que se cree otra instancia en otro lado
    
    public static SesionControl getSesion(){
        if(sesion == null){
            sesion = new SesionControl(); // Si no hay una sesion iniciada crea una
        }
        return sesion;
    }
    
    public void guardarSesion(Cliente cliente){
        this.clienteIniciado = cliente; // Guarda al cliente que inicio sesion en el form
    }
    
    public Cliente getCliente(){
        return clienteIniciado; // Regresa al cliente que inicio sesion
    }
    
    public void cerrarSesion(){
        this.clienteIniciado = null; // Cierra sesion eliminando al cliente que estaba iniciado
    }
    
}
