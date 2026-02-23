/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import org.itson.proyecto01.dtos.NuevaTransferenciaDTO;
import org.itson.proyecto01.entidades.Transferencia;

/**
 *
 * @author calo2
 */
public interface ITransferenciasDAO {
    
    
    public Transferencia realizarTransferencia(Integer idCuentaOrigen,
            NuevaTransferenciaDTO nuevaTransferenciaDTO,Integer idCuentaDestino
    ) throws PersistenciaException;

}
