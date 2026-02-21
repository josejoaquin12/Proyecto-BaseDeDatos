/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import org.itson.proyecto01.entidades.RetiroSinCuenta;

/**
 *
 * @author joset
 */
public interface IRetiroSinCuentaDAO {
  public void realizarRetiro(RetiroSinCuenta retiro) throws PersistenciaException;
}
