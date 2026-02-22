/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import org.itson.proyecto01.entidades.Retiro;

/**
 *
 * @author joset
 */
public interface IRetiroConCuentaDAO {
  public void realizarRetiro(Retiro retiro) throws PersistenciaException;
  public Retiro buscarPorFolioYContrasena(String folio, String contrasena)throws PersistenciaException;
  public void cobrarRetiro(Retiro retiro) throws PersistenciaException;
}
