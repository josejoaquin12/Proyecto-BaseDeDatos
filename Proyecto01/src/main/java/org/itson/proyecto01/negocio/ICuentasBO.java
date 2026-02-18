/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.util.List;
import org.itson.proyecto01.entidades.Cuenta;

/**
 *
 * @author joset
 */
public interface ICuentasBO {
    public abstract List<Cuenta> consultarCuentasCliente(Integer idCliente) throws NegocioException;
}
