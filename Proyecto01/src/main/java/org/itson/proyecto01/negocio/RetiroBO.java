/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.time.LocalDateTime;
import org.itson.proyecto01.dtos.NuevaOperacionDTO;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.RetiroSinCuenta;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.RetiroSinCuentaDAO;
import org.itson.proyecto01.persistencia.IRetiroSinCuentaDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;

/**
 *
 * @author joset
 */
public class RetiroBO implements IRetiroBO{

    private ICuentasDAO cuentaDAO;
    private IRetiroSinCuentaDAO RetiroSinCuentaDAO;

    public RetiroBO() {
        cuentaDAO = new CuentasDAO();
        RetiroSinCuentaDAO = new RetiroSinCuentaDAO(cuentaDAO);
    }
    
    private String generarCodigoNumerico(int longitud) {

        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            int digito = (int) (Math.random() * 10);
            codigo.append(digito);
        }

        return codigo.toString();
    }
    
    @Override
    public RetiroSinCuenta generarRetiro(Cuenta cuenta, double monto) {

        if (cuenta == null) {
            throw new IllegalArgumentException("Cuenta no válida");
        }

        if (monto <= 0) {
            throw new IllegalArgumentException("Monto inválido");
        }

        if (cuenta.getSaldo() < monto) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        try {

            double nuevoSaldo = cuenta.getSaldo() - monto;
            cuenta.setSaldo(nuevoSaldo);


            cuentaDAO.actualizarSaldo(cuenta.getId(),monto);

            Integer id = cuenta.getId();
            LocalDateTime fechaHora = LocalDateTime.now();
            String contrasena = generarCodigoNumerico(10);
            String folio = generarCodigoNumerico(18);
            
            RetiroSinCuenta retiro = new RetiroSinCuenta(id, monto, contrasena, fechaHora, "RETIRO_SIN_CUENTA", folio, "PENDIENTE");

            RetiroSinCuentaDAO.realizarRetiro(retiro);
            
            return retiro;

        } catch (PersistenciaException e) {
            throw new RuntimeException("Error al generar retiro: " + e.getMessage());
        }
    }
}
