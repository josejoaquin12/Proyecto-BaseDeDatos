/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.proyecto01.negocio;

import org.itson.proyecto01.dtos.NuevaTransferenciaDTO;
import org.itson.proyecto01.entidades.Transferencia;
<<<<<<< HEAD


=======
>>>>>>> 005f9590cb627d9fd6419077eef92d5bddb4abcb
/**
 *
 * @author calo2
 */
public interface ITransferenciasBO {

    
    
    public abstract Transferencia realizarTransferencia(
            NuevaTransferenciaDTO nuevaTransferencia
    )throws NegocioException;
        
    
}
