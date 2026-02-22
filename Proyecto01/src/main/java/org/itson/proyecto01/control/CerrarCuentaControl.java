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
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.presentacion.CerrarCuentaForm;

/**
 *
 * @author joset
 */
public class CerrarCuentaControl {

    private final CerrarCuentaForm cerrarForm;
    private final ICuentasBO cuentasBO;
    private final IClientesBO clientesBO;
    private final int idCliente;
    private static final Logger LOGGER = Logger.getLogger(CerrarCuentaControl.class.getName());

    public CerrarCuentaControl(CerrarCuentaForm cerrarForm, ICuentasBO cuentasBO, IClientesBO clientesBO, int idCliente) {
        this.cerrarForm = cerrarForm;
        this.cuentasBO = cuentasBO;
        this.clientesBO = clientesBO;
        this.idCliente = idCliente;

        cerrarForm.btnConfirmarCerrarCuenta.addActionListener(e -> confirmarCancelacion());

        cargarCuentasCliente();
    }

    private void cargarCuentasCliente() {
        try {
            List<Cuenta> cuentas = cuentasBO.consultarCuentasClienteActivas(idCliente);

            DefaultComboBoxModel<Cuenta> modelo = new DefaultComboBoxModel<>();
            modelo.addElement(new Cuenta(0, "Seleccione una cuenta...", null, 0.0, null, 0));

            for (Cuenta c : cuentas) {
                modelo.addElement(c);
            }

            cerrarForm.cboCuentas.setModel(modelo);

        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
            JOptionPane.showMessageDialog(cerrarForm, "Error al cargar cuentas activas del cliente");
        }
    }

    private void confirmarCancelacion() {
        Cuenta cuentaSeleccionada = (Cuenta) cerrarForm.cboCuentas.getSelectedItem();

        if (cuentaSeleccionada == null || cuentaSeleccionada.getId() == 0) {
            JOptionPane.showMessageDialog(cerrarForm, "Debe seleccionar una cuenta válida.");
            return;
        }

        // Mostrar cuadro de confirmación
        int opcion = JOptionPane.showConfirmDialog(
                cerrarForm,
                "¿Seguro que deseas cancelar la cuenta " + cuentaSeleccionada.getNumeroCuenta() + "?\n"
                + "Una vez cancelada ya no puede activarse de nuevo.",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            try {
                Cuenta cuentaCancelada = cuentasBO.cancelarCuenta(cuentaSeleccionada.getNumeroCuenta());

                JOptionPane.showMessageDialog(cerrarForm,
                        "Cuenta cancelada exitosamente.\nNúmero de cuenta: " + cuentaCancelada.getNumeroCuenta(),
                        "Cancelación de Cuenta",
                        JOptionPane.INFORMATION_MESSAGE);

                cargarCuentasCliente();

            } catch (NegocioException ex) {
                LOGGER.severe(ex.getMessage());
                JOptionPane.showMessageDialog(cerrarForm, "Error al cancelar la cuenta: " + ex.getMessage());
            }
        }
    }

}
