/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.RetiroSinCuenta;

/**
 *
 * @author joset
 */
public interface IRetiroBO {
  public RetiroSinCuenta generarRetiro(Cuenta cuenta, double monto);
}
