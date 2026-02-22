/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.time.LocalDateTime;
import java.util.List;
import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.Retiro;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.RetiroConCuentaDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;
import org.itson.proyecto01.persistencia.IRetiroConCuentaDAO;

/**
 *
 * @author joset
 */
public class RetiroBO implements IRetiroBO{

    private ICuentasDAO cuentaDAO;
    private IRetiroConCuentaDAO RetiroConCuentaDAO;

    public RetiroBO() {
        cuentaDAO = new CuentasDAO();
        RetiroConCuentaDAO = new RetiroConCuentaDAO(cuentaDAO);
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
    public Retiro generarRetiro(Cuenta cuenta, double monto)throws NegocioException {

        if (cuenta == null) {
            throw new NegocioException(" :Cuenta no válida",null);
        }

        if (monto <= 0) {
            throw new NegocioException(" :Monto inválido",null);
        }

        if (cuenta.getSaldo() < monto) {
            throw new NegocioException(" :Saldo insuficiente",null);
        }

        try {
            String numeroCuenta = cuenta.getNumeroCuenta();
            LocalDateTime fechaHora = LocalDateTime.now();
            String contrasena = generarCodigoNumerico(10);
            String folio = generarCodigoNumerico(18);
            
            Retiro retiro = new Retiro(numeroCuenta, monto, contrasena, fechaHora, "RETIRO_SIN_CUENTA", folio, "PENDIENTE");

            RetiroConCuentaDAO.realizarRetiro(retiro);
            
            return retiro;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al generar retiro: " + e.getMessage(),e);
        }
    }
    @Override
    public Retiro compararRetiro(String folio, String contrasenia) throws NegocioException {
        
        if (folio == null || contrasenia == null || folio.isEmpty()|| contrasenia.isEmpty()) {
            throw new NegocioException(" :Folio o contraseña vacíos", null);
        }

        try {
            Retiro retiro = RetiroConCuentaDAO.buscarPorFolioYContrasena(folio, contrasenia);

            if (retiro == null) {
                throw new NegocioException(" :Folio o contraseña incorrectos", null);
            }
            if (retiro.getFechaExpiracion().isBefore(LocalDateTime.now())) {
                throw new NegocioException(" :El retiro ya expiró", null);
            }
            return retiro;

        } catch (PersistenciaException ex) {
            throw new NegocioException(" :Error al validar retiro", ex);
        }
    }
    
    
    
    @Override
    public void cobrarRetiro(Retiro retiro) throws NegocioException{
        
        try {     
            if (retiro.getEstado() == null) {
                throw new NegocioException(" :Estado del retiro desconocido",null);
                
            } else if ("COBRADO".equals(retiro.getEstado())) {
                throw new NegocioException(" :El retiro ya fue cobrado",null);

            } else if ("NO_COBRADO".equals(retiro.getEstado())) {
                throw new NegocioException(" :El retiro ha expirado y no se puede cobrar",null);
                
            } else {
                RetiroConCuentaDAO.cobrarRetiro(retiro);
            }         
        }catch (PersistenciaException ex) {          
            throw new NegocioException(" :Error al cobrar retiro: " + ex.getMessage(),ex);

        }
    }
}
