/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.Retiro;

/**
 *
 * @author joset
 */
public interface IRetiroBO {
  public Retiro generarRetiro(Cuenta cuenta, double monto)throws NegocioException;
  public Retiro compararRetiro(String numeroFolio , String contrasenia) throws NegocioException;
  public void cobrarRetiro(Retiro retiro)throws NegocioException;
}
