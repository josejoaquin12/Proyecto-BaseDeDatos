/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto01;

import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.CuentasBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.ITransferenciasBO;
import org.itson.proyecto01.negocio.TransferenciasBO;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.ITransferenciasDAO;
import org.itson.proyecto01.persistencia.TransferenciasDAO;
import org.itson.proyecto01.presentacion.TransferenciaForm;

/**
 *
 * @author elgps
 */
public class Proyecto01 {

    public static void main(String[] args) {

            // Crear DAO
            ICuentasDAO cuentasDAO = new CuentasDAO() {};
            ITransferenciasDAO TransferenciaDAO = new TransferenciasDAO(cuentasDAO) {};
            IClientesDAO clientesDAO = new ClientesDAO() {};
            // Crear BO
            ICuentasBO cuentasBO = new CuentasBO(cuentasDAO);           
            ITransferenciasBO TransferenciaBO = new TransferenciasBO(TransferenciaDAO,cuentasDAO);
            IClientesBO clientesBO = new ClientesBO(clientesDAO);
            
            // Crear y mostrar Frame
            TransferenciaForm frame = new TransferenciaForm(cuentasBO,TransferenciaBO,clientesBO);

            frame.setVisible(true);

    }
}
