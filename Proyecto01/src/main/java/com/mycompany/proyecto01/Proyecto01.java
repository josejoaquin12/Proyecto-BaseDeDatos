/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto01;

import org.itson.proyecto01.negocio.CuentasBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.presentacion.TransferenciaForm;

/**
 *
 * @author elgps
 */
public class Proyecto01 {

    public static void main(String[] args) {
        try {

            // Crear DAO
            ICuentasDAO cuentasDAO = new CuentasDAO() {};

            // Crear BO
            ICuentasBO cuentasBO = new CuentasBO(cuentasDAO);

            // Crear y mostrar Frame
            TransferenciaForm frame = new TransferenciaForm(cuentasBO);

            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
