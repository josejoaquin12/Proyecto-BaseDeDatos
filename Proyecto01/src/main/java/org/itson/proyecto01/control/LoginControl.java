package org.itson.proyecto01.control;

import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.presentacion.LoginForm;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.RegistroForm;
import org.itson.proyecto01.presentacion.RetiroSinCuentaForm;

/**
 * Clase controladora que gestiona el proceso de inicio de sesión y
 * autenticación.
 * <p>
 * Este controlador es el punto de entrada principal para los usuarios
 * registrados. Se encarga de validar las credenciales, gestionar la creación de
 * la sesión activa mediante {@link SesionControl} y redirigir al usuario al
 * Menú Principal o a los módulos de registro y retiros sin cuenta.
 * </p>
 *
 * * @author Jesus Omar
 */
public class LoginControl {

    private final IClientesBO clienteBO;
    private LoginForm loginForm;
    private UtileriasControl utilerias;

    /**
     * Constructor que inicializa el controlador de inicio de sesión.
     * <p>
     * Configura la dependencia de negocio {@link IClientesBO} y vincula los
     * eventos de la interfaz gráfica necesarios para la interacción inicial.
     * </p>
     *
     * * @param loginForm Instancia de la vista {@link LoginForm} a
     * administrar.
     */
    public LoginControl(LoginForm loginForm) {
        this.utilerias = new UtileriasControl();
        this.loginForm = loginForm;
        IClientesDAO clienteDAO = new ClientesDAO();
        this.clienteBO = new ClientesBO(clienteDAO);
        inicializarEventos();
    }

    /**
     * Realiza el proceso de autenticación del cliente en el sistema.
     * <p>
     * El flujo de este método incluye:
     * <ol>
     * <li>Validación de que los campos no estén vacíos o nulos.</li>
     * <li>Llamada a la capa de negocio para verificar las credenciales.</li>
     * <li>Recuperación del objeto {@link Cliente} completo tras una
     * autenticación exitosa.</li>
     * <li>Persistencia del cliente en el singleton de
     * {@link SesionControl}.</li>
     * <li>Transición automática hacia el Menú Principal.</li>
     * </ol>
     * </ol>
     * </p>
     *
     * * @param nombreCompleto Cadena con el nombre del usuario ingresado.
     * @param password Cadena con la contraseña ingresada.
     * @throws ControlException Si los campos están vacíos o las credenciales
     * son incorrectas.
     * @throws NegocioException Si ocurre un error inesperado en la capa de
     * negocio o base de datos.
     */
    public void iniciarSesion(String nombreCompleto, String password) throws ControlException, NegocioException {

        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new ControlException("El correo es obligatorio", null);
        }

        if (password == null || password.isBlank()) {
            throw new ControlException("La contraseña es obligatoria", null);
        }
        try {
            int idCliente = clienteBO.autenticarNombreCompletoPassword(nombreCompleto, password);
            if (idCliente <= 0) {
                throw new ControlException("Nombre o contraseña incorrectos", null);
            }
            // Obtiene el cliente que inicio sesion por su id
            Cliente cliente = clienteBO.obtenerClientePorId(idCliente);
            // Guarda la sesion
            SesionControl.getSesion().guardarSesion(cliente);
            // Manda al usuario al menu principal
            utilerias.abrirMenuPrincipal(loginForm);
        } catch (NegocioException | ControlException ex) {
            JOptionPane.showMessageDialog(loginForm, "Error, no se encontro el cliente " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Configura los disparadores de eventos para los botones de la vista.
     * <p>
     * Gestiona las acciones para el registro de nuevos usuarios y el acceso
     * rápido a retiros sin cuenta.
     * </p>
     */
    private void inicializarEventos() {
        loginForm.getBtnRegistrar().addActionListener(e -> utilerias.abrirPantallaRegistroForm(loginForm));
        loginForm.getBtnRetiroSinCuenta().addActionListener(e -> utilerias.abrirRetiroSinCuenta(loginForm));
    }
}
