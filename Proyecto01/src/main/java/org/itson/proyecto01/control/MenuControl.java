package org.itson.proyecto01.control;

import org.itson.proyecto01.presentacion.*;
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
    private  UtileriasControl utilerias;
    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();

    /**
     * Constructor que inicializa el controlador del menú.
     * <p>
     * Configura las dependencias BO, inicializa los DAOs necesarios y establece
     * los escuchadores de eventos para los componentes de la vista.
     * </p>
     *
     *  @param menuForm Instancia de la vista {@link MenuPrincipalForm} a
     * administrar.
     */
    public MenuControl(MenuPrincipalForm menuForm) {
        this.utilerias = new UtileriasControl();
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
        menuForm.getBtnUsuario().addActionListener(e -> utilerias.abrirPantallaUsuario(menuForm));

//         Evento para btnRealizarTransferencia
        menuForm.getBtnMostrarTransferencias().addActionListener(e -> utilerias.abrirPantallaTransferencia(menuForm));

//
//         Evento para btnConsultarOperaciones
        menuForm.getBtnConsultarOperaciones().addActionListener(e -> utilerias.abrirConsultarOperacionesForm(menuForm));
//
//         Evento para btnCerrarSesion
        menuForm.getBtnCerrarSesion().addActionListener(e -> utilerias.cerrarSesion(menuForm));

        // Evento para btnCancelarCuenta
        menuForm.getBtnCancelarCuenta().addActionListener(e -> utilerias.abrirCerrarCuentaForm(menuForm));

        // Evento para btnAltaCuenta
        menuForm.getBtnAltaCuenta().addActionListener(e -> utilerias.abrirAltaCuentaForm(menuForm));

        //Evento para btnRetiroConCuenta
        menuForm.getBtnMostrarRetiroSinCuenta().addActionListener(e -> utilerias.abrirRetiroConCuenta(menuForm));
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
}
