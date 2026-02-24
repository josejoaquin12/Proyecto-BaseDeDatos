package org.itson.proyecto01.control;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import org.itson.proyecto01.dtos.NuevoClienteDTO;
import org.itson.proyecto01.dtos.NuevoDomicilioDTO;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.entidades.Domicilio;
import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.DomiciliosBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.IDomiciliosBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.DomiciliosDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.IDomiciliosDAO;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.UsuarioForm;

/**
 * Clase controladora encargada de la gestión del perfil del usuario.
 * <p>
 * Este controlador administra la interfaz {@link UsuarioForm} y permite:
 * <ul>
 * <li>Visualizar los datos personales y de domicilio del cliente actualmente
 * autenticado.</li>
 * <li>Validar y procesar la actualización de la información del perfil.</li>
 * <li>Coordinar con las capas de negocio de clientes y domicilios para
 * persistir cambios.</li>
 * </ul>
 * </p>
 *
 * * @author joset
 */
public class UsuarioControl {
    

    private final IClientesBO clientesBO;
    private final IDomiciliosBO domiciliosBO;
    private UsuarioForm usuarioForm;
    private UtileriasControl utilerias;
    private Integer idCliente = SesionControl.getSesion().getCliente().getId();
    private Cliente cliente = SesionControl.getSesion().getCliente();

    /**
     * Constructor que inicializa el controlador de usuario.
     * <p>
     * Configura los DAOs y BOs necesarios, recupera la información inicial del
     * cliente desde la base de datos y prepara la vista.
     * </p>
     *
     * @param usuarioForm Instancia de la vista {@link UsuarioForm} a
     * administrar.
     */
    public UsuarioControl(UsuarioForm usuarioForm) {
        this.utilerias = new UtileriasControl();
        this.usuarioForm = usuarioForm;

        IClientesDAO clientesDAO = new ClientesDAO();
        this.clientesBO = new ClientesBO(clientesDAO);

        IDomiciliosDAO domiciliosDAO = new DomiciliosDAO();
        this.domiciliosBO = new DomiciliosBO(domiciliosDAO);
        inicializarEventos();
        cargarDatosUsuario();
    }

    /**
     * Extrae la información del cliente y su domicilio para poblar los campos
     * de texto en la interfaz gráfica.
     *
     * * @throws NegocioException Si hay un error en la consulta a la base de
     * datos.
     */
    private void cargarDatosUsuario() {
        try {
            usuarioForm.getTxtNombre().setText(cliente.getNombres());
            usuarioForm.getTxtApellidoP().setText(cliente.getApellidoP());
            usuarioForm.getTxtApellidoM().setText(cliente.getApellidoM());
            usuarioForm.getTxtFechaNacimiento().setText(cliente.getFechaNacimiento().toString());

            Domicilio dom = domiciliosBO.obtenerDomicilioPorID(idCliente);

            if (dom != null) {
                usuarioForm.getTxtCalle().setText(dom.getCalle());
                usuarioForm.getTxtNumero().setText(dom.getNumero());
                usuarioForm.getTxtColonia().setText(dom.getColonia());
                usuarioForm.getTxtCiudad().setText(dom.getCiudad());
                usuarioForm.getTxtEstado().setText(dom.getEstado());
                usuarioForm.getTxtCodigoPostal().setText(dom.getCodigoPostal());
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(
                    usuarioForm,
                    "Error al los cargar datos del cliente: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Inicializa los escuchadores de eventos para los botones de actualizar y
     * navegación.
     */
    private void inicializarEventos() {
        usuarioForm.getBtnActualizarDatos().addActionListener(e -> actualizarUsuario());
        usuarioForm.getBtnVolverMenuPrincipal().addActionListener(e -> utilerias.abrirMenuPrincipal(usuarioForm));
    }

    /**
     * Procesa la solicitud de actualización de datos del perfil.
     * <p>
     * El flujo de actualización comprende:
     * <ol>
     * <li>Recolección y limpieza (trim) de los datos en el formulario.</li>
     * <li>Validación de campos obligatorios y formato de fecha de
     * nacimiento.</li>
     * <li>Creación de objetos DTO ({@link NuevoClienteDTO} y
     * {@link NuevoDomicilioDTO}) para el transporte de datos.</li>
     * <li>Actualización secuencial: primero el domicilio y posteriormente el
     * cliente.</li>
     * <li>Notificación del resultado al usuario mediante un cuadro de
     * diálogo.</li>
     * </ol>
     * </ol>
     * </p>
     */
    private void actualizarUsuario() {
        try {
            //Usuario
            String nombres = usuarioForm.getTxtNombre().getText().trim();
            String apellidoP = usuarioForm.getTxtApellidoP().getText().trim();
            String apellidoM = usuarioForm.getTxtApellidoM().getText().trim();
            String fechaTexto = usuarioForm.getTxtFechaNacimiento().getText().trim();

            if (nombres.isEmpty() || apellidoP.isEmpty()) {
                throw new ControlException("Nombre y apellido paterno son obligatorios", null);
            }

            LocalDate fechaNacimiento = LocalDate.parse(fechaTexto);
            Domicilio actual = domiciliosBO.obtenerDomicilioPorID(idCliente);

            NuevoClienteDTO clienteActualizado = new NuevoClienteDTO(nombres, apellidoP, apellidoM, fechaNacimiento, cliente.getFechaRegistro(), cliente.getEdad(), actual.getId());

            // Domicilio
            String calle = usuarioForm.getTxtCalle().getText().trim();
            String numero = usuarioForm.getTxtNumero().getText().trim();
            String colonia = usuarioForm.getTxtColonia().getText().trim();
            String ciudad = usuarioForm.getTxtCiudad().getText().trim();
            String estado = usuarioForm.getTxtEstado().getText().trim();
            String codigoPostal = usuarioForm.getTxtCodigoPostal().getText().trim();

            NuevoDomicilioDTO domicilioDTO = new NuevoDomicilioDTO(calle, numero, colonia, ciudad, estado, codigoPostal);

            Domicilio domicilioActualizado = domiciliosBO.actualizarDomicilio(domicilioDTO);

            Cliente cliente = clientesBO.actualizarCliente(idCliente, clienteActualizado, domicilioActualizado.getId());

            JOptionPane.showMessageDialog(
                    usuarioForm,
                    "Datos actualizados correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NegocioException | DateTimeParseException | ControlException ex) {
            JOptionPane.showMessageDialog(
                    usuarioForm,
                    "Error al actualizar usuario: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
