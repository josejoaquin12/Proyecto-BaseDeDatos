/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.control;

import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.presentacion.RegistroForm;

/**
 *
 * @author Jesus Omar
 */
public class RegistroControl {
    
    private final IClientesBO clientesBO;
    private RegistroForm registroForm;
    
    public RegistroControl(RegistroForm registroForm){
        this.registroForm = registroForm;
        IClientesDAO clientesDAO = new ClientesDAO();
        this.clientesBO = new ClientesBO(clientesDAO);
    }
    
    
    
}
