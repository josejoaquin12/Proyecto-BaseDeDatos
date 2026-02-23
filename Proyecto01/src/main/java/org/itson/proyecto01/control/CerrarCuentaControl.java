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
import org.itson.proyecto01.presentacion.MenuPrincipalForm;

/**
 * Clase controladora encargada de gestionar el proceso de cancelación de
 * cuentas bancarias.
 * <p>
 * Este controlador vincula la interfaz {@link CerrarCuentaForm} con la lógica
 * de negocio de cuentas. Permite al usuario seleccionar una de sus cuentas
 * activas, solicita una confirmación explícita mediante cuadros de diálogo y
 * coordina la actualización del estado de la cuenta en la base de datos a
 * través de la capa BO.
 * </p>
 *
 * * @author joset
 */
public class CerrarCuentaControl {

    private final CerrarCuentaForm cerrarForm;
    private final ICuentasBO cuentasBO;
    private final IClientesBO clientesBO;
    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();
    private static final Logger LOGGER = Logger.getLogger(CerrarCuentaControl.class.getName());

    /**
     * Constructor que inicializa el controlador para cerrar cuentas.
     * <p>
     * Establece las dependencias de negocio, configura los escuchadores
     * (listeners) para los botones de cancelar y confirmar, e inicia la carga
     * de las cuentas activas del cliente en el combo box.
     * </p>
     *
     * * @param cerrarForm Instancia de la interfaz gráfica
     * {@link CerrarCuentaForm}.
     * @param cuentasBO Lógica de negocio para operaciones con cuentas.
     * @param clientesBO Lógica de negocio para operaciones con clientes.
     */
    public CerrarCuentaControl(CerrarCuentaForm cerrarForm, ICuentasBO cuentasBO, IClientesBO clientesBO) {
        this.cerrarForm = cerrarForm;
        this.cuentasBO = cuentasBO;
        this.clientesBO = clientesBO;

        cerrarForm.btnCancelar.addActionListener(e -> abrirMenuPrincipal());

        cerrarForm.btnConfirmarCerrarCuenta.addActionListener(e -> confirmarCancelacion());

        cargarCuentasCliente();
    }

    /**
     * Obtiene y despliega las cuentas activas del cliente en la interfaz.
     * <p>
     * Consulta a la capa de negocio por las cuentas con estado activo asociadas
     * al cliente en sesión. Agrega una opción por defecto para guiar al usuario
     * en la selección.
     * </p>
     */
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

    /**
     * Valida y ejecuta el proceso de cancelación de la cuenta seleccionada.
     * <p>
     * El flujo incluye:
     * <ol>
     * <li>Validar que se haya seleccionado una cuenta real.</li>
     * <li>Mostrar un {@code JOptionPane.showConfirmDialog} advirtiendo que la
     * acción es irreversible.</li>
     * <li>Si el usuario confirma, invoca a
     * {@code cuentasBO.cancelarCuenta()}.</li>
     * <li>Notifica el éxito de la operación y recarga la lista de cuentas.</li>
     * </ol>
     * </p>
     */
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

    /**
     * Gestiona la transición hacia el Menú Principal de la aplicación.
     * <p>
     * Centra la ventana del menú principal y libera los recursos de la ventana
     * actual mediante {@code dispose()}.
     * </p>
     */
    private void abrirMenuPrincipal() {
        MenuPrincipalForm menu = new MenuPrincipalForm();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        cerrarForm.dispose();
    }

}
