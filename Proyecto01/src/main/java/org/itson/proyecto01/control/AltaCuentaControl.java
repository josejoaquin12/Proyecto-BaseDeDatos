/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.control;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.itson.proyecto01.dtos.NuevaCuentaDTO;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.AltaCuentaForm;

/**
 *
 * @author joset
 */
public class AltaCuentaControl {

    private final AltaCuentaForm altaForm;
    private final ICuentasBO cuentasBO;
    private final IClientesBO clientesBO;
    private final int idCliente;
    private static final Logger LOGGER = Logger.getLogger(AltaCuentaControl.class.getName());

    public AltaCuentaControl(AltaCuentaForm altaForm, ICuentasBO cuentasBO, IClientesBO clientesBO, int idCliente) {
        this.altaForm = altaForm;
        this.cuentasBO = cuentasBO;
        this.clientesBO = clientesBO;
        this.idCliente = idCliente;

        altaForm.BtnConfirmarAlta.addActionListener(e -> confirmarAlta());

        altaForm.BtnMostrarMenu.addActionListener(e -> abrirMenuPrincipal());

        cargarDatosCliente();
    }

    private void cargarDatosCliente() {
        try {
            Cliente cliente = clientesBO.obtenerClientePorId(idCliente);
            altaForm.txtNombreCliente.setText(cliente.getNombres() + " " + cliente.getApellidoP() + " " + cliente.getApellidoM());
            altaForm.txtNumeroCliente.setText(String.valueOf(cliente.getId()));
            LocalDateTime fechaActual = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            altaForm.txtFecha.setText(fechaActual.format(formato));
        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(altaForm, "Error al cargar datos del cliente");
        }
    }

    private void confirmarAlta() {
        if (!altaForm.chbTyC.isSelected()) {
            JOptionPane.showMessageDialog(altaForm, "Debe aceptar los términos y condiciones.");
            return;
        }

        try {
            NuevaCuentaDTO nuevaCuenta = new NuevaCuentaDTO(LocalDateTime.now(), idCliente);
            Cuenta cuentaCreada = cuentasBO.altaCuenta();

            JOptionPane.showMessageDialog(altaForm,
                    "Cuenta creada exitosamente.\nNúmero de cuenta: " + cuentaCreada.getNumeroCuenta(),
                    "Alta de Cuenta",
                    JOptionPane.INFORMATION_MESSAGE);

            abrirMenuPrincipal();

        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(altaForm, "Error al crear la cuenta: " + ex.getMessage());
        }
    }

    private void abrirMenuPrincipal() {
        MenuPrincipalForm menu = new MenuPrincipalForm(idCliente);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        altaForm.dispose();
    }
}
