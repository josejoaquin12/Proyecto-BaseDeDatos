/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import java.util.logging.Logger;
import org.itson.proyecto01.entidades.Transferencia;

/**
 *
 * @author calo2
 */
public interface ITransferenciaDAO {
    
    
    public Transferencia realizarTransferencia() throws PersistenciaException;

}
