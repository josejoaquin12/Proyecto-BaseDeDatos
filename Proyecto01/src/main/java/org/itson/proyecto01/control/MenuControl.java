package org.itson.proyecto01.control;

import org.itson.proyecto01.presentacion.*;
import org.itson.proyecto01.presentacion.AltaCuentaForm;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.CuentasBO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import java.util.List;
import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;

/**
 * Clase controladora que gestiona la lógica del Menú Principal de la
 * aplicación.
 * <p>
 * Este controlador actúa como el punto central de navegación del usuario,
 * encargándose de:
 * <ul>
 * <li>Cargar y mostrar las cuentas bancarias asociadas al cliente en
 * sesión.</li>
 * <li>Mostrar el saludo personalizado con el nombre completo del cliente.</li>
 * <li>Gestionar la redirección hacia los módulos de transferencias, retiros,
 * alta/baja de cuentas y consulta de movimientos.</li>
 * <li>Administrar la finalización de la sesión del usuario.</li>
 * </ul>
 * </p>
 *
 * * @author Jesus Omar
 */
public class MenuControl {

    private final ICuentasBO cuentasBO;
    private final IClientesBO clientesBO;
    private final MenuPrincipalForm menuForm;
    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();

    /**
     * Constructor que inicializa el controlador del menú.
     * <p>
     * Configura las dependencias BO, inicializa los DAOs necesarios y establece
     * los escuchadores de eventos para los componentes de la vista.
     * </p>
     *
     * * @param menuForm Instancia de la vista {@link MenuPrincipalForm} a
     * administrar.
     */
    public MenuControl(MenuPrincipalForm menuForm) {
        this.menuForm = menuForm;

        // Inicializar BO 
        ICuentasDAO cuentasDAO = new CuentasDAO();
        IClientesDAO clientesDAO = new ClientesDAO();

        //InicializarBO
        this.clientesBO = new ClientesBO(clientesDAO);
        this.cuentasBO = new CuentasBO(cuentasDAO);

        inicializarEventos();
    }

    /**
     * Recupera las cuentas bancarias del cliente desde la base de datos y las
     * renderiza en la vista.
     * <p>
     * Por cada cuenta encontrada, solicita al {@code menuForm} la creación
     * dinámica de un panel con el número de cuenta, estado y saldo actual.
     * </p>
     */
    public void cargarCuentasCliente() {
        try {
            List<Cuenta> cuentas = cuentasBO.consultarCuentasCliente(idCliente); // obtiene datos
            for (Cuenta c : cuentas) {
                menuForm.agregarPanelCuenta(c.getNumeroCuenta(), c.getEstado(), c.getSaldo());
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(menuForm, "Error al cargar cuentas: " + ex.getMessage());
        }
    }

    /**
     * Configura los disparadores de eventos para todos los botones de la
     * interfaz. Centraliza la navegación y las acciones de cierre de sesión.
     */
    private void inicializarEventos() {
//         Evento para btnUsuario
        menuForm.getBtnUsuario().addActionListener(e -> abrirPantallaUsuario());

//         Evento para btnRealizarTransferencia
        menuForm.getBtnMostrarTransferencias().addActionListener(e -> abrirTransferenciaForm());

//
//         Evento para btnConsultarOperaciones
        menuForm.getBtnConsultarOperaciones().addActionListener(e -> abrirConsultarOperacionesForm());
//
//         Evento para btnCerrarSesion
        menuForm.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());

        // Evento para btnCancelarCuenta
        menuForm.getBtnCancelarCuenta().addActionListener(e -> abrirCerrarCuentaForm());

        // Evento para btnAltaCuenta
        menuForm.getBtnAltaCuenta().addActionListener(e -> abrirAltaCuentaForm());

        //Evento para btnRetiroConCuenta
        menuForm.getBtnMostrarRetiroSinCuenta().addActionListener(e -> abrirRetiroConCuenta());
    }

    /**
     * Realiza la transición hacia la pantalla de perfil de usuario.
     * <p>
     * Cierra el menú actual y libera recursos tras instanciar el nuevo
     * formulario.
     * </p>
     */
    private void abrirPantallaUsuario() {
        try {
            UsuarioForm usuarioForm = new UsuarioForm();
            UsuarioControl usuarioControl = new UsuarioControl(usuarioForm);
            usuarioForm.setVisible(true);
            menuForm.dispose();
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(menuForm, "Error al cargar nombre del cliente: " + ex.getMessage());
        }
    }

    /**
     * Consulta el nombre completo del cliente (Nombres + Apellidos) y actualiza
     * la etiqueta de bienvenida en la interfaz gráfica.
     */
    public void cargarNombreCliente() {
        try {
            String nombreCompleto = clientesBO.obtenerClientePorId(idCliente).getNombres() + " " + clientesBO.obtenerClientePorId(idCliente).getApellidoP() + " " + clientesBO.obtenerClientePorId(idCliente).getApellidoM();
            menuForm.actualizarBienvenida(nombreCompleto);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(menuForm, "Error al cargar nombre del cliente: " + ex.getMessage());
        }
    }

    /**
     * Gestiona la transición hacia el formulario de Transferencias.
     */
    private void abrirTransferenciaForm() {

        TransferenciaForm form = new TransferenciaForm();
        TransferenciaControl TransferenciaControl = new TransferenciaControl(form);
        form.setVisible(true);
        menuForm.dispose();

    }

    /**
     * Gestiona la transición hacia el formulario de Retiros con Cuenta.
     */
    private void abrirRetiroConCuenta() {
        RetiroConCuentaForm RetiroConCuenta = new RetiroConCuentaForm();
        RetiroConCuentaControl abrirRetiroConCuentaControl = new RetiroConCuentaControl(RetiroConCuenta);
        RetiroConCuenta.setVisible(true);
        menuForm.dispose();
    }

    /**
     * Gestiona la transición hacia el formulario para dar de alta una nueva
     * cuenta.
     */
    private void abrirAltaCuentaForm() {
        AltaCuentaForm altaCuentaForm = new AltaCuentaForm();
        AltaCuentaControl altaCuentaControl = new AltaCuentaControl(altaCuentaForm, cuentasBO, clientesBO);
        altaCuentaForm.setVisible(true);
        menuForm.dispose();
    }

    /**
     * Gestiona la transición hacia el formulario para cancelar o cerrar una
     * cuenta.
     */
    private void abrirCerrarCuentaForm() {
        CerrarCuentaForm cerrarCuentaForm = new CerrarCuentaForm();
        CerrarCuentaControl cerrarCuentaControl = new CerrarCuentaControl(cerrarCuentaForm, cuentasBO, clientesBO);
        cerrarCuentaForm.setVisible(true);
        menuForm.dispose();
    }

    /**
     * Gestiona la transición hacia el formulario de consulta de operaciones
     * históricas. Configura la ventana para aparecer centrada en la pantalla.
     */
    private void abrirConsultarOperacionesForm() {
        ConsultarOperacionesForm operacionesForm = new ConsultarOperacionesForm();
        operacionesForm.setLocationRelativeTo(null);
        OperacionControl operacionesControl = new OperacionControl(operacionesForm);
        operacionesForm.setVisible(true);
        menuForm.dispose();
    }

    /**
     * Finaliza la sesión actual del usuario.
     * <p>
     * Destruye la ventana del menú principal y redirige al usuario de vuelta a
     * la pantalla de Login.
     * </p>
     */
    private void cerrarSesion() {
        menuForm.dispose(); // cierra menu principal
        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
    }
}
