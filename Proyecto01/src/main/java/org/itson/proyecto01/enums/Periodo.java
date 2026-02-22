/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.itson.proyecto01.enums;

/**
 *
 * @author Jesus Omar
 */
public enum Periodo {
    TODAS("Todas"),
    HOY("Hoy"),
    ULTIMA_SEMANA("Ultima semana"),
    MES_ACTUAL("Mes actual");
    
    private final String nombre;
    
    Periodo(String nombre){
        this.nombre = nombre;
    }
    
    @Override
    public String toString(){
        return nombre;
    }
}
