package org.itson.proyecto01.control;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.itson.proyecto01.presentacion.RegistroForm;

/**
 * Clase controladora encargada de gestionar el proceso de registro de nuevos
 * clientes.
 * <p>
 * Esta clase actúa como intermediaria entre la interfaz de usuario
 * {@link RegistroForm} y las capas de negocio para clientes y domicilios. Se
 * encarga de capturar los datos de la vista, validarlos, coordinar la
 * persistencia de la información y gestionar la navegación tras un registro
 * exitoso.
 * </p>
 *
 * * @author Jesus Omar
 */
public class RegistroControl {

    private final IClientesBO clientesBO;
    private RegistroForm registroForm;
    private final IDomiciliosBO domiciliosBO;

    /**
     * Constructor que inicializa el controlador de registro.
     * <p>
     * Configura las dependencias necesarias de las capas de negocio (BO) y
     * datos (DAO), vincula la vista y dispara la inicialización de eventos de
     * la interfaz.
     * </p>
     *
     * * @param registroForm La instancia de la interfaz gráfica de registro a
     * controlar.
     */
    public RegistroControl(RegistroForm registroForm) {
        this.registroForm = registroForm;
        IClientesDAO clientesDAO = new ClientesDAO();
        this.clientesBO = new ClientesBO(clientesDAO);

        IDomiciliosDAO domiciliosDAO = new DomiciliosDAO();
        this.domiciliosBO = new DomiciliosBO(domiciliosDAO);

        inicializarEventos();
    }

    /**
     * Configura los escuchadores (listeners) de eventos para los componentes de
     * la vista.
     * <p>
     * Asigna la acción de registro al botón correspondiente de la interfaz
     * gráfica.
     * </p>
     */
    private void inicializarEventos() {
        registroForm.getBtnRegistrarse().addActionListener(e -> registrarCliente());
    }

    /**
     * Orquesta el proceso completo de registro de un nuevo cliente y su
     * domicilio.
     * <p>
     * El flujo del método consiste en:
     * <ol>
     * <li>Extraer y limpiar (trim) los datos de los campos de texto de la
     * vista.</li>
     * <li>Validar que los campos obligatorios no estén vacíos.</li>
     * <li>Verificar la coincidencia de las contraseñas.</li>
     * <li>Transformar los datos en objetos de transferencia (DTO).</li>
     * <li>Persistir primero el domicilio para obtener su identificador.</li>
     * <li>Crear el cliente vinculándolo al domicilio registrado.</li>
     * <li>Notificar el éxito al usuario y redirigir al Menú Principal.</li>
     * </ol>
     * </ol>
     * </p>
     *
     * * @throws ControlException Si las validaciones de entrada fallan (campos
     * vacíos o contraseñas distintas).
     * @throws NegocioException Si ocurre un error en las reglas de negocio o
     * persistencia.
     * @throws DateTimeParseException Si el formato de la fecha de nacimiento es
     * inválido.
     */
    private void registrarCliente() {
        try {

            String nombres = registroForm.getTxtNombre().getText().trim();
            String apellidoP = registroForm.getTxtApellidoP().getText().trim();
            String apellidoM = registroForm.getTxtApellidoM().getText().trim();
            String fechaTexto = registroForm.getTxtFechaNacimiento().getText().trim();

            String password = new String(registroForm.getTxtContrasenia().getPassword());
            String confirmar = new String(registroForm.getTxtConfirmarContrasenia().getPassword());

            if (nombres.isEmpty() || apellidoP.isEmpty() || password.isEmpty()) {
                throw new ControlException("Todos los campos obligatorios deben llenarse", null);
            }

            if (!password.equals(confirmar)) {
                throw new ControlException("Las contraseñas no coinciden", null);
            }

            LocalDate fechaNacimiento = LocalDate.parse(fechaTexto);
            int edad = 0;//valor temporal, edad se calcula en la DAO antes de hacer el insert

            NuevoClienteDTO nuevoCliente = new NuevoClienteDTO(
                    nombres,
                    apellidoP,
                    apellidoM,
                    fechaNacimiento,
                    password,
                    LocalDateTime.now(),
                    edad,
                    null
            );

            String calle = registroForm.getTxtCalle().getText().trim();
            String numero = registroForm.getTxtNumero().getText().trim();
            String colonia = registroForm.getTxtColonia().getText().trim();
            String ciudad = registroForm.getTxtCiudad().getText().trim();

            String estado = registroForm.getTxtEstado().getText().trim();
            String codigoPostal = registroForm.getTxtCodigoPostal().getText().trim();

            NuevoDomicilioDTO nuevoDomicilio = new NuevoDomicilioDTO(
                    calle,
                    numero,
                    colonia,
                    ciudad,
                    estado,
                    codigoPostal
            );
            Domicilio Domicilio = domiciliosBO.registrarDomicilio(nuevoDomicilio);
            Cliente clie = clientesBO.crearCliente(nuevoCliente, Domicilio.getId());

            JOptionPane.showMessageDialog(
                    registroForm,
                    "🎉🎉🎉 Registro exitoso 🎉🎉🎉",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );
            MenuPrincipalForm menu = new MenuPrincipalForm();
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
            registroForm.dispose();

        } catch (NegocioException | ControlException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    registroForm,
                    "Error al registrar cliente: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

}
