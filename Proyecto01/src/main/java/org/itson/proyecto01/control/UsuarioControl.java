//package org.itson.proyecto01.control;
//
//import java.time.DateTimeException;
//import java.time.LocalDate;
//import java.time.format.DateTimeParseException;
//import javax.swing.JOptionPane;
//import org.itson.proyecto01.dtos.NuevoClienteDTO;
//import org.itson.proyecto01.dtos.NuevoDomicilioDTO;
//import org.itson.proyecto01.entidades.Cliente;
//import org.itson.proyecto01.entidades.Domicilio;
//import org.itson.proyecto01.negocio.ClientesBO;
//import org.itson.proyecto01.negocio.DomiciliosBO;
//import org.itson.proyecto01.negocio.IClientesBO;
//import org.itson.proyecto01.negocio.IDomiciliosBO;
//import org.itson.proyecto01.negocio.NegocioException;
//import org.itson.proyecto01.persistencia.ClientesDAO;
//import org.itson.proyecto01.persistencia.DomiciliosDAO;
//import org.itson.proyecto01.persistencia.IClientesDAO;
//import org.itson.proyecto01.persistencia.IDomiciliosDAO;
//import org.itson.proyecto01.presentacion.MenuPrincipalForm;
//import org.itson.proyecto01.presentacion.UsuarioForm;
//
///**
// * Control para mostrar y actualizar los datos de un usuario
// */
//public class UsuarioControl {
//
//    private final IClientesBO clientesBO;
//    private final IDomiciliosBO domiciliosBO;
//    private UsuarioForm usuarioForm;
//    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();
//    private Cliente cliente;
//
//    public UsuarioControl(UsuarioForm usuarioForm) throws NegocioException {
//        this.usuarioForm = usuarioForm;
//
//        IClientesDAO clientesDAO = new ClientesDAO();
//        this.clientesBO = new ClientesBO(clientesDAO);
//
//        IDomiciliosDAO domiciliosDAO = new DomiciliosDAO();
//        this.domiciliosBO = new DomiciliosBO(domiciliosDAO);
//        Cliente cliente = clientesBO.obtenerClientePorId(idCliente);
//
//        inicializarEventos();
//        cargarDatosUsuario();
//    }
//
//    private void cargarDatosUsuario() throws NegocioException {
//        usuarioForm.getTxtNombre().setText(cliente.getNombres());
//        usuarioForm.getTxtApellidoP().setText(cliente.getApellidoP());
//        usuarioForm.getTxtApellidoM().setText(cliente.getApellidoM());
//        usuarioForm.getTxtFechaNacimiento().setText(cliente.getFechaNacimiento().toString());
//
//        Domicilio dom = domiciliosBO.obtenerDomicilioPorID(idCliente);
//
//        if (dom != null) {
//            usuarioForm.getTxtCalle().setText(dom.getCalle());
//            usuarioForm.getTxtNumero().setText(dom.getNumero());
//            usuarioForm.getTxtColonia().setText(dom.getColonia());
//            usuarioForm.getTxtCiudad().setText(dom.getCiudad());
//            usuarioForm.getTxtEstado().setText(dom.getEstado());
//            usuarioForm.getTxtCodigoPostal().setText(dom.getCodigoPostal());
//        }
//    }
//
//    private void inicializarEventos() {
//        usuarioForm.getBtnActualizarDatos().addActionListener(e -> actualizarUsuario());
//        usuarioForm.getBtnVolverMenuPrincipal().addActionListener(e -> abrirMenuPrincipal());
//    }
//
//    private void abrirMenuPrincipal() {
//        MenuPrincipalForm menu = new MenuPrincipalForm();
//        menu.setLocationRelativeTo(null);
//        menu.setVisible(true);
//        usuarioForm.dispose();
//    }
//
//    private void actualizarUsuario() {
//        try {
//            String nombres = usuarioForm.getTxtNombre().getText().trim();
//            String apellidoP = usuarioForm.getTxtApellidoP().getText().trim();
//            String apellidoM = usuarioForm.getTxtApellidoM().getText().trim();
//            String fechaTexto = usuarioForm.getTxtFechaNacimiento().getText().trim();
//
//            if (nombres.isEmpty() || apellidoP.isEmpty()) {
//                throw new NegocioException("Nombre y apellido paterno son obligatorios", null);
//            }
//
//            LocalDate fechaNacimiento = LocalDate.parse(fechaTexto);
//            Domicilio actual = domiciliosBO.obtenerDomicilioPorID(idCliente);
//            NuevoClienteDTO clienteActualizado = new NuevoClienteDTO(nombres,apellidoP,apellidoM,fechaNacimiento,null,cliente.getFechaRegistro(),cliente.getEdad(),
//                    actual != null ? cliente.getDomicilio().getId() : null
//            );
//
//            // Domicilio
//            String calle = usuarioForm.getTxtCalle().getText().trim();
//            String numero = usuarioForm.getTxtNumero().getText().trim();
//            String colonia = usuarioForm.getTxtColonia().getText().trim();
//            String ciudad = usuarioForm.getTxtCiudad().getText().trim();
//            String estado = usuarioForm.getTxtEstado().getText().trim();
//            String codigoPostal = usuarioForm.getTxtCodigoPostal().getText().trim();
//
//            NuevoDomicilioDTO domicilioDTO = new NuevoDomicilioDTO(
//                    calle, numero, colonia, ciudad, estado, codigoPostal
//            );
//
//            Domicilio domicilioActualizado;
//            domicilioActualizado = domiciliosBO.actualizarDomicilio(cliente.getDomicilio().getId(), domicilioDTO);
//
//
//            // Actualizamos cliente
//            Cliente cliente = clientesBO.actualizarCliente(cliente.getId(), clienteActualizado, domicilioActualizado.getId());
//
//            JOptionPane.showMessageDialog(
//                    usuarioForm,
//                    "✅ Datos actualizados correctamente",
//                    "Éxito",
//                    JOptionPane.INFORMATION_MESSAGE
//            );
//
//            // Actualizamos referencia local
//            this.clienteActual = cliente;
//
//        } catch (NegocioException | DateTimeParseException ex) {
//            JOptionPane.showMessageDialog(
//                    usuarioForm,
//                    "Error al actualizar usuario: " + ex.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE
//            );
//        }
//    }
//}
