package org.itson.proyecto01.control;

import java.util.List;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.CuentasBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.ITransferenciasBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.negocio.TransferenciasBO;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.ITransferenciasDAO;
import org.itson.proyecto01.persistencia.TransferenciasDAO;
import org.itson.proyecto01.presentacion.TransferenciaForm;
import org.itson.proyecto01.presentacion.ConfirmarTransferenciaForm;
import org.itson.proyecto01.presentacion.ConsultarOperacionesForm;
import org.itson.proyecto01.presentacion.LoginForm;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.UsuarioForm;
import org.itson.proyecto01.presentacion.RetiroConCuentaForm;

/**
 * Clase controladora que gestiona la lógica para realizar transferencias entre
 * cuentas.
 * <p>
 * Este controlador vincula la interfaz {@link TransferenciaForm} con los
 * servicios de negocio de cuentas, clientes y transferencias. Se encarga de:
 * <ul>
 * <li>Cargar y administrar las cuentas activas del cliente emisor.</li>
 * <li>Buscar y validar en tiempo real la existencia de la cuenta destino.</li>
 * <li>Actualizar la disponibilidad de saldo según la cuenta seleccionada.</li>
 * <li>Validar que el monto de la transferencia sea numéricamente válido y no
 * exceda el saldo.</li>
 * <li>Redirigir al usuario hacia la confirmación final de la transacción.</li>
 * </ul>
 * </p>
 *
 * * @author joset
 */
public class TransferenciaControl {

    private final TransferenciaForm transfrom;
    private final ICuentasBO cuentasBO;
    private final ITransferenciasBO transferenciasBO;
    private final IClientesBO clientesBO;
    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();
    private static final Logger LOGGER = Logger.getLogger(TransferenciaControl.class.getName());

    /**
     * Constructor que inicializa el controlador de transferencias.
     * <p>
     * Configura manualmente la jerarquía de persistencia (DAO) y negocio (BO),
     * establece los escuchadores de eventos para la interfaz y dispara la carga
     * inicial de las cuentas del cliente.
     * </p>
     *
     * * @param transfrom Instancia de la interfaz gráfica
     * {@link TransferenciaForm}.
     */
    public TransferenciaControl(TransferenciaForm transfrom) {
        // Inicializar BO 
        ICuentasDAO cuentasDAO = new CuentasDAO();
        IClientesDAO clientesDAO = new ClientesDAO();
        ITransferenciasDAO transferenciasDAO = new TransferenciasDAO(cuentasDAO);

        //InicializarBO
        this.clientesBO = new ClientesBO(clientesDAO);
        this.cuentasBO = new CuentasBO(cuentasDAO);
        this.transferenciasBO = new TransferenciasBO(transferenciasDAO, cuentasDAO);

        this.transfrom = transfrom;

        transfrom.getBtnContinuarTransferencia().addActionListener(e -> continuar());
        transfrom.getTxtNumeroCuentaDestino().addActionListener(e -> buscarCuentaDestino());
        transfrom.getCboCuentasCliente().addActionListener(e -> actualizarSaldo());
        transfrom.getBtnMostrarRetiroSinCuenta().addActionListener(e -> abrirRetiroConCuenta());
        transfrom.getBtnMostrarMenu().addActionListener(e -> abrirMenuPrincipal(idCliente));
        transfrom.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        transfrom.getBtnConsultarOperaciones().addActionListener(e -> abrirConsultarOperacionesForm());
        transfrom.getBtnUsuario().addActionListener(e -> abrirMenuPrincipal(idCliente));
        cargarCuentasCliente();
    }

    /**
     * Gestiona la transición hacia el Menú Principal.
     *
     * * @param idCliente Identificador del cliente para contextualizar el
     * menú.
     */
    private void abrirMenuPrincipal(int idCliente) {
        MenuPrincipalForm menu = new MenuPrincipalForm();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        transfrom.dispose();
    }

    private void abrirPantallaUsuario() {
        UsuarioForm usuarioForm = new UsuarioForm();
        usuarioForm.setVisible(true);
        transfrom.dispose();
    }

    private void abrirRetiroConCuenta() {
        RetiroConCuentaForm RetiroConCuenta = new RetiroConCuentaForm();
        RetiroConCuentaControl abrirRetiroConCuentaControl = new RetiroConCuentaControl(RetiroConCuenta);
        RetiroConCuenta.setVisible(true);
        transfrom.dispose();
    }

    private void abrirConsultarOperacionesForm() {
        ConsultarOperacionesForm operacionesForm = new ConsultarOperacionesForm();
        //OperacionControl operacionesControl = new OperacionControl();
        operacionesForm.setLocationRelativeTo(null);
        operacionesForm.setVisible(true);
        transfrom.dispose();
    }

    /**
     * Finaliza la sesión del usuario y redirige a la pantalla de Login.
     */
    private void cerrarSesion() {
        transfrom.dispose(); // cierra menu principal
        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
    }

    /**
     * Recupera y despliega las cuentas con estado "Activo" del cliente en el
     * selector de la vista.
     */
    private void cargarCuentasCliente() {
        try {
            List<Cuenta> cuentas = cuentasBO.consultarCuentasClienteActivas(idCliente);

            DefaultComboBoxModel<Cuenta> modelo = new DefaultComboBoxModel<>();
            modelo.addElement(new Cuenta(0, "Seleccione una cuenta...", null, 0.0, null, 0));

            for (Cuenta c : cuentas) {
                modelo.addElement(c);
            }

            transfrom.getCboCuentasCliente().setModel(modelo);

        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    /**
     * Actualiza la etiqueta de saldo disponible en la interfaz cada vez que se
     * selecciona una cuenta distinta en el ComboBox.
     */
    private void actualizarSaldo() {
        Cuenta cuenta = (Cuenta) transfrom.getCboCuentasCliente().getSelectedItem();

        if (cuenta != null && cuenta.getId() != 0) {
            transfrom.getLblSaldoDisponible().setText(String.format("$%,.2f", cuenta.getSaldo()));
        } else {
            transfrom.getLblSaldoDisponible().setText("$ 0.00");
        }
    }

    /**
     * Busca la cuenta destino mediante su número de cuenta y recupera el nombre
     * completo del titular asociado.
     * <p>
     * Este método proporciona retroalimentación inmediata al usuario sobre
     * quién recibirá el dinero, ayudando a prevenir errores de transferencia.
     * </p>
     */
    private void buscarCuentaDestino() {

        String numero = transfrom.getTxtNumeroCuentaDestino().getText().trim();

        if (numero.isEmpty()) {
            transfrom.getLblNombreCuentaDestino().setText("");
            return;
        }

        try {
            Cuenta cuentaDestino = cuentasBO.obtenerCuentaporNumeroCuenta(numero);
            Cliente cliente = this.clientesBO.obtenerClientePorId(cuentaDestino.getIdCliente());
            String nombreCompleto = cliente.getNombres() + " " + cliente.getApellidoP() + " " + cliente.getApellidoM();
            if (cuentaDestino != null) {
                transfrom.getLblNombreCuentaDestino().setText(nombreCompleto);
            } else {
                transfrom.getLblNombreCuentaDestino().setText("Cuenta no encontrada");
            }

        } catch (NegocioException ex) {
            transfrom.getLblNombreCuentaDestino().setText("Error al buscar");
        }
    }

    /**
     * Realiza validaciones lógicas sobre los campos de entrada para habilitar o
     * deshabilitar el botón de continuar.
     * <p>
     * Verifica que el monto sea positivo, que la cuenta origen tenga fondos
     * suficientes y que se haya especificado un destino.
     * </p>
     */
    private void validarBotonContinuar() {
        Cuenta cuenta = (Cuenta) transfrom.getCboCuentasCliente().getSelectedItem();
        String destino = transfrom.getTxtNumeroCuentaDestino().getText().trim();
        String monto = transfrom.getTxtMonto().getText().trim();
        String saldoDisponible = transfrom.getLblSaldoDisponible().getText().trim();

        String textosaldoDisponible = saldoDisponible
                .replace("$", "")
                .replace(",", "")
                .trim();

        double montInt = Double.parseDouble(monto);
        double saldoDisponibleInt = Double.parseDouble(textosaldoDisponible);

        if (cuenta != null && cuenta.getId() != 0 && !destino.isEmpty() && !monto.isEmpty() && saldoDisponibleInt > 0 && saldoDisponibleInt <= montInt) {
            transfrom.getBtnContinuarTransferencia().setEnabled(true);
        } else {
            transfrom.getBtnContinuarTransferencia().setEnabled(false);
        }
    }

    /**
     * Procesa los datos recolectados y abre la pantalla de confirmación.
     * <p>
     * Realiza un último parseo de seguridad sobre el monto y transfiere la
     * responsabilidad de la ejecución a {@link ConfirmarTransferenciaForm}.
     * </p>
     *
     * * @throws NumberFormatException Si el formato del monto no es un número
     * válido.
     */
    private void continuar() {
        try {
            validarBotonContinuar();
            Cuenta cuentaOrigen = (Cuenta) transfrom.getCboCuentasCliente().getSelectedItem();

            String destino = transfrom.getTxtNumeroCuentaDestino().getText().trim();

            String montoTxt = transfrom.getTxtMonto().getText().trim();

            double monto = Double.parseDouble(montoTxt);

            ConfirmarTransferenciaForm confirmar = new ConfirmarTransferenciaForm(cuentaOrigen.getNumeroCuenta(), destino, monto, transfrom.getLblNombreCuentaDestino().getText(), transferenciasBO, cuentaOrigen.getSaldo());

            confirmar.setVisible(true);
            transfrom.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(transfrom, "Error monto invalido: " + ex.getMessage());
        }

    }
}
