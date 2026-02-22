package org.itson.proyecto01.control;

import org.itson.proyecto01.presentacion.*;
import org.itson.proyecto01.presentacion.AltaCuentaForm;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.CuentasBO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ITransferenciasBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.negocio.TransferenciasBO;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.ITransferenciasDAO;
import org.itson.proyecto01.persistencia.TransferenciasDAO;

public class MenuControl {

    private final ICuentasBO cuentasBO;
    private final IClientesBO clientesBO;
    private final ITransferenciasBO transferenciasBO;
    private final MenuPrincipalForm menuForm;
    private final int idCliente;

    public MenuControl(MenuPrincipalForm menuForm, int idCliente) {
        this.menuForm = menuForm;
        this.idCliente = idCliente;

        // Inicializar BO 
        ICuentasDAO cuentasDAO = new CuentasDAO();
        IClientesDAO clientesDAO = new ClientesDAO();
        ITransferenciasDAO transferenciasDAO = new TransferenciasDAO(cuentasDAO);

        //InicializarBO
        this.clientesBO = new ClientesBO(clientesDAO);
        this.transferenciasBO = new TransferenciasBO(transferenciasDAO, cuentasDAO);
        this.cuentasBO = new CuentasBO(cuentasDAO);

        inicializarEventos();
    }

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

    private void inicializarEventos() {
//         Evento para btnUsuario
        menuForm.getBtnUsuario().addActionListener(e -> abrirPantallaUsuario());

//         Evento para btnRealizarTransferencia
        menuForm.getBtnMostrarTransferencias().addActionListener(e -> {
            try {
                abrirTransferenciaForm();
            } catch (NegocioException ex) {
                Logger.getLogger(MenuControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

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

    private void abrirPantallaUsuario() {
        PerfilUsuarioForm usuarioForm = new PerfilUsuarioForm();
        usuarioForm.setVisible(true);
        menuForm.dispose();
    }

    public void cargarNombreCliente() {
        try {
            String nombreCompleto = clientesBO.obtenerClientePorId(idCliente).getNombres() + " " + clientesBO.obtenerClientePorId(idCliente).getApellidoP() + " " + clientesBO.obtenerClientePorId(idCliente).getApellidoM();
            menuForm.actualizarBienvenida(nombreCompleto);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(menuForm, "Error al cargar nombre del cliente: " + ex.getMessage());
        }
    }

    private void abrirTransferenciaForm() throws NegocioException {

        TransferenciaForm form = new TransferenciaForm();

        TransferenciaControl TransferenciaControl = new TransferenciaControl(form, cuentasBO, transferenciasBO, clientesBO, idCliente);
        form.setVisible(true);
        menuForm.dispose();
    }

    private void abrirRetiroConCuenta() {
        RetiroConCuentaForm RetiroConCuenta = new RetiroConCuentaForm();
        RetiroConCuentaControl abrirRetiroConCuentaControl = new RetiroConCuentaControl(RetiroConCuenta, cuentasBO, clientesBO, idCliente);
        RetiroConCuenta.setVisible(true);
        menuForm.dispose();
    }

    private void abrirAltaCuentaForm() {
        AltaCuentaForm altaCuentaForm = new AltaCuentaForm();
        AltaCuentaControl altaCuentaControl = new AltaCuentaControl(altaCuentaForm, cuentasBO, clientesBO, idCliente);
        altaCuentaForm.setVisible(true);
        menuForm.dispose();
    }

    private void abrirCerrarCuentaForm() {
        CerrarCuentaForm cerrarCuentaForm = new CerrarCuentaForm();
        CerrarCuentaControl cerrarCuentaControl = new CerrarCuentaControl(cerrarCuentaForm, cuentasBO,  clientesBO,  idCliente);
        cerrarCuentaForm.setVisible(true);
        menuForm.dispose();
    }

    private void abrirConsultarOperacionesForm() {
        ConsultarOperacionesForm operacionesForm = new ConsultarOperacionesForm();
        //OperacionControl operacionesControl = new OperacionControl();
        operacionesForm.setVisible(true);
        menuForm.dispose();
    }

    private void cerrarSesion() {
        menuForm.dispose(); // cierra menu principal
        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
    }
}
