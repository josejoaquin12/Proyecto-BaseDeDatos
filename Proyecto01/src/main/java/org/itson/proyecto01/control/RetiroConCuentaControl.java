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
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.RetiroConCuentaForm;

/**
 * Clase controladora para la generación de retiros sin cuenta desde la sesión
 * del cliente.
 * <p>
 * Este controlador vincula la interfaz {@link RetiroConCuentaForm} con la
 * lógica de negocio de cuentas y retiros. Permite a un cliente autenticado
 * seleccionar una de sus cuentas activas para generar un folio y una contraseña
 * de retiro, validando en tiempo real que el monto solicitado no exceda el
 * saldo disponible.
 * </p>
 *
 * * @author joset
 */
public class RetiroConCuentaControl {

    private final RetiroConCuentaForm retiroCForm;
    private final ICuentasBO cuentasBO;
    private final IClientesBO clientesBO;
    private  UtileriasControl utilerias;
    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();
    private static final Logger LOGGER = Logger.getLogger(TransferenciaControl.class.getName());

    /**
     * Constructor que inicializa el controlador de retiros.
     * <p>
     * Instancia las capas de negocio (BO), carga las cuentas activas del
     * cliente en el selector de la interfaz y configura los eventos de los
     * componentes.
     * </p>
     *
     * * @param retiroCForm Instancia de la interfaz gráfica
     * {@link RetiroConCuentaForm}.
     */
    public RetiroConCuentaControl(RetiroConCuentaForm retiroCForm) {
        this.utilerias = new UtileriasControl();
        this.retiroCForm = retiroCForm;

        // Inicializar BO 
        ICuentasDAO cuentasDAO = new CuentasDAO();
        IClientesDAO clientesDAO = new ClientesDAO();

        //InicializarBO
        this.clientesBO = new ClientesBO(clientesDAO);
        this.cuentasBO = new CuentasBO(cuentasDAO);
        cargarCuentasCliente();
        inicializarEventos();

    }

    /**
     * Actualiza dinámicamente la etiqueta de saldo en la interfaz.
     * <p>
     * Se ejecuta cada vez que el usuario cambia la cuenta seleccionada en el
     * ComboBox, formateando el saldo en moneda local (ej. $1,000.00).
     * </p>
     */
    private void actualizarSaldo() {
        Cuenta cuenta = (Cuenta) retiroCForm.getCboCuentasCliente().getSelectedItem();

        if (cuenta != null && cuenta.getId() != 0) {
            retiroCForm.getLblSaldoDisponible().setText(String.format("$%,.2f", cuenta.getSaldo()));
        } else {
            retiroCForm.getLblSaldoDisponible().setText("$ 0.00");
        }
    }

    /**
     * Recupera y despliega las cuentas activas del cliente en el selector de la
     * vista.
     * <p>
     * Incluye una opción inicial por defecto para forzar la selección explícita
     * de una cuenta válida.
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

            retiroCForm.getCboCuentasCliente().setModel(modelo);

        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    /**
     * Inicializa los escuchadores de eventos para botones y componentes de
     * selección.
     */
    private void inicializarEventos() {
        retiroCForm.getBtnCancelarRetiro().addActionListener(e -> utilerias.abrirMenuPrincipal(retiroCForm));
        retiroCForm.getBtnGenerarRetiro().addActionListener(e -> generarRetiroConCuenta());
        retiroCForm.getCboCuentasCliente().addActionListener(e -> actualizarSaldo());
    }

    /**
     * Realiza validaciones de seguridad y formato antes de proceder con el
     * retiro.
     * <p>
     * Verifica que:
     * <ul>
     * <li>Se haya seleccionado una cuenta válida.</li>
     * <li>El monto ingresado sea un número válido.</li>
     * <li>El monto sea mayor a cero y no exceda el saldo disponible de la
     * cuenta.</li>
     * </ul>
     * </p>
     *
     * * @return {@code true} si todos los criterios son válidos, {@code false}
     * en caso contrario.
     */
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
            if (cuenta == null || cuenta.getId() == 0 || textoMonto.isEmpty()) {
                retiroCForm.getBtnGenerarRetiro().setEnabled(false);
                retiroCForm.getBtnGenerarRetiro().setEnabled(true);
                JOptionPane.showMessageDialog(retiroCForm, "Error: cuenta o monto invalidos");
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
            JOptionPane.showMessageDialog(retiroCForm, "Error: cuenta o monto invalidos");
            return false;
        }
        return true;
    }

    /**
     * Procesa la solicitud y genera las credenciales del retiro sin cuenta.
     * <p>
     * Tras validar los datos, utiliza {@link RetiroBO} para registrar la
     * operación y generar un folio y contraseña únicos. Al finalizar, muestra
     * estos datos al usuario mediante un mensaje informativo y limpia el
     * formulario.
     * </p>
     */
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
