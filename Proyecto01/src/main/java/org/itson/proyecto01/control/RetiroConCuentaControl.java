/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.control;

import java.util.List;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.Retiro;
import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.CuentasBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.negocio.RetiroBO;
import org.itson.proyecto01.negocio.TransferenciasBO;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.ITransferenciasDAO;
import org.itson.proyecto01.persistencia.TransferenciasDAO;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.RetiroConCuentaForm;

/**
 *
 * @author joset
 */
public class RetiroConCuentaControl {

    private final RetiroConCuentaForm retiroCForm;
    private final ICuentasBO cuentasBO;
    private final IClientesBO clientesBO;
    private final int idCliente;
    private static final Logger LOGGER = Logger.getLogger(TransferenciaControl.class.getName());

    public RetiroConCuentaControl(RetiroConCuentaForm retiroCForm, int idCliente) {
        this.retiroCForm = retiroCForm;
        
        // Inicializar BO 
        ICuentasDAO cuentasDAO = new CuentasDAO();
        IClientesDAO clientesDAO = new ClientesDAO();
        
        //InicializarBO
        this.clientesBO = new ClientesBO(clientesDAO);
        this.cuentasBO = new CuentasBO(cuentasDAO);
        this.idCliente = idCliente;
        cargarCuentasCliente();
        inicializarEventos();

    }

    private void actualizarSaldo() {
        Cuenta cuenta = (Cuenta) retiroCForm.getCboCuentasCliente().getSelectedItem();

        if (cuenta != null && cuenta.getId() != 0) {
            retiroCForm.getLblSaldoDisponible().setText(String.format("$%,.2f", cuenta.getSaldo()));
        } else {
            retiroCForm.getLblSaldoDisponible().setText("$ 0.00");
        }
    }

    private void cargarCuentasCliente() {

        try {
            List<Cuenta> cuentas = cuentasBO.consultarCuentasClienteActivas(idCliente);

            DefaultComboBoxModel<Cuenta> modelo = new DefaultComboBoxModel<>();
            modelo.addElement(new Cuenta(0, "Seleccione una cuenta...", null, 0.0, null, 0));

            for (Cuenta c : cuentas) {
                modelo.addElement(c);
            }

            retiroCForm.getCboCuentasCliente().setModel(modelo);

        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    private void inicializarEventos() {
        retiroCForm.getBtnCancelarRetiro().addActionListener(e -> abrirMenuPrincipal(idCliente));
        retiroCForm.getBtnGenerarRetiro().addActionListener(e -> generarRetiroConCuenta());
        retiroCForm.getCboCuentasCliente().addActionListener(e -> actualizarSaldo());
    }

    private boolean validarBotonGenerarRetiro() {
        Cuenta cuenta = (Cuenta) retiroCForm.getCboCuentasCliente().getSelectedItem();

        String textoMonto = retiroCForm.getTxtMonto().getText()
                .replace("$", "")
                .replace(",", "")
                .trim();

        String textoSaldo = retiroCForm.getLblSaldoDisponible().getText()
                .replace("$", "")
                .replace(",", "")
                .trim();
        
        
        try {
            if(cuenta == null || cuenta.getId() == 0 || textoMonto.isEmpty()) {
                retiroCForm.getBtnGenerarRetiro().setEnabled(false);
                retiroCForm.getBtnGenerarRetiro().setEnabled(true);
                JOptionPane.showMessageDialog(retiroCForm,"Error: cuenta o monto invalidos");
                return false;
                
            }
            double monto = Double.parseDouble(textoMonto);
            double saldoDisponible = Double.parseDouble(textoSaldo);

            if (monto > 0 && saldoDisponible > 0 && monto <= saldoDisponible) {
                retiroCForm.getBtnGenerarRetiro().setEnabled(true);
                return true;
            }

        } catch (NumberFormatException e) {
            retiroCForm.getBtnGenerarRetiro().setEnabled(false);
            JOptionPane.showMessageDialog(retiroCForm,"Error: cuenta o monto invalidos");
            return false;
        }
        return true;
    }

    private void abrirMenuPrincipal(int idCliente) {
        MenuPrincipalForm menu = new MenuPrincipalForm(idCliente);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        retiroCForm.dispose();
    }

    private void generarRetiroConCuenta() {
        try { 
            if (validarBotonGenerarRetiro()) {

                Cuenta cuentaSeleccionada = (Cuenta) retiroCForm.getCboCuentasCliente().getSelectedItem();

                String numeroCuenta = cuentaSeleccionada.getNumeroCuenta().trim();
                System.out.println(numeroCuenta);
                Cuenta cuentaNueva = cuentasBO.obtenerCuentaporNumeroCuenta(numeroCuenta);
                System.out.println(cuentaNueva);
                double monto = Double.parseDouble(retiroCForm.getTxtMonto().getText());
                
                RetiroBO retiroBO = new RetiroBO();
                Retiro retiroGenerado = retiroBO.generarRetiro(cuentaNueva, monto);
                
                JOptionPane.showMessageDialog(retiroCForm,
                        "Retiro generado correctamente\n\n"
                        + "Folio: " + retiroGenerado.getFolio() + "\n"
                        + "Contraseña: " + retiroGenerado.getContrasena(),
                        "Retiro sin cuenta",
                        JOptionPane.INFORMATION_MESSAGE
                );
                retiroCForm.getTxtMonto().setText("");
            }
        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(retiroCForm, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
}
