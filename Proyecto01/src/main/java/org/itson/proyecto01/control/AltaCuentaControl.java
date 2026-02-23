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
 * Clase controladora encargada de gestionar el proceso de apertura (alta) de
 * nuevas cuentas bancarias.
 * <p>
 * Este controlador vincula la interfaz {@link AltaCuentaForm} con las capas de
 * negocio de clientes y cuentas. Sus responsabilidades incluyen:
 * <ul>
 * <li>Visualizar la información del cliente y la fecha actual en el
 * formulario.</li>
 * <li>Validar la aceptación de términos y condiciones por parte del
 * usuario.</li>
 * <li>Coordinar la creación de una nueva cuenta vinculada al cliente en
 * sesión.</li>
 * <li>Gestionar la navegación de retorno al menú principal tras el éxito o
 * cancelación.</li>
 * </ul>
 * </p>
 *
 * * @author joset
 */
public class AltaCuentaControl {

    private final AltaCuentaForm altaForm;
    private final ICuentasBO cuentasBO;
    private final IClientesBO clientesBO;
    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();
    ;
    private static final Logger LOGGER = Logger.getLogger(AltaCuentaControl.class.getName());

    /**
     * Constructor que inicializa el controlador de alta de cuenta.
     * <p>
     * Configura las dependencias BO, establece los escuchadores de eventos para
     * confirmar la operación o volver al menú, y dispara la carga inicial de
     * datos informativos en la vista.
     * </p>
     *
     * * @param altaForm Instancia de la interfaz gráfica
     * {@link AltaCuentaForm}.
     * @param cuentasBO Lógica de negocio para operaciones con cuentas.
     * @param clientesBO Lógica de negocio para operaciones con clientes.
     */
    public AltaCuentaControl(AltaCuentaForm altaForm, ICuentasBO cuentasBO, IClientesBO clientesBO) {
        this.altaForm = altaForm;
        this.cuentasBO = cuentasBO;
        this.clientesBO = clientesBO;

        altaForm.BtnConfirmarAlta.addActionListener(e -> confirmarAlta());

        altaForm.BtnMostrarMenu.addActionListener(e -> abrirMenuPrincipal());

        cargarDatosCliente();
    }

    /**
     * Recupera y muestra los datos del cliente y la fecha actual en la
     * interfaz.
     * <p>
     * Este método consulta el nombre completo y ID del cliente para
     * personalizar el formulario, proporcionando un formato de fecha
     * "dd/MM/yyyy HH:mm".
     * </p>
     */
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

    /**
     * Valida los requisitos y ejecuta la creación de una nueva cuenta.
     * <p>
     * El proceso sigue estos pasos:
     * <ol>
     * <li>Verifica que el CheckBox de términos y condiciones esté
     * seleccionado.</li>
     * <li>Construye un objeto {@link NuevaCuentaDTO} con la marca de tiempo
     * actual.</li>
     * <li>Solicita a la capa de negocio la generación de la nueva cuenta.</li>
     * <li>Muestra un mensaje de éxito con el número de cuenta generado.</li>
     * <li>Redirige al usuario al menú principal.</li>
     * </ol>
     * </ol>
     * </p>
     */
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

    /**
     * Gestiona la transición hacia el Menú Principal de la aplicación.
     * <p>
     * Cierra la ventana de alta de cuenta y libera los recursos asociados.
     * </p>
     */
    private void abrirMenuPrincipal() {
        MenuPrincipalForm menu = new MenuPrincipalForm();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        altaForm.dispose();
    }
}
