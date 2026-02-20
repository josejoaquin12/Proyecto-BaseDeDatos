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
import org.itson.proyecto01.negocio.ITransferenciasBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.negocio.TransferenciasBO;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.ITransferenciasDAO;
import org.itson.proyecto01.persistencia.TransferenciasDAO;

public class MenuControl {

    private ICuentasBO cuentasBO;
    private IClientesBO clientesBO;
    private ITransferenciasBO transferenciasBO;
    private MenuPrincipalForm menuForm;
    private int idCliente;


    public MenuControl(MenuPrincipalForm menuForm, int idCliente) {
        this.menuForm = menuForm;
        this.idCliente = idCliente;

        // Inicializar BO 
        ICuentasDAO cuentasDAO = new CuentasDAO(); 
        IClientesDAO clientesDAO = new ClientesDAO();
        ITransferenciasDAO transferenciasDAO = new TransferenciasDAO(cuentasDAO);
        //InicializarBO
        this.clientesBO = new ClientesBO(clientesDAO);
        this.transferenciasBO = new TransferenciasBO(transferenciasDAO,cuentasDAO);
        this.cuentasBO = new CuentasBO(cuentasDAO);
        // Cargar cuentas del cliente en la pantalla
        cargarCuentasCliente();
        inicializarEventos();
    }

    public void cargarCuentasCliente() {
        try {
            List<Cuenta> cuentas = cuentasBO.consultarCuentasCliente(idCliente); // obtiene datos
            for (Cuenta c : cuentas) {
                menuForm.agregarPanelCuenta( c.getNumeroCuenta(), c.getEstado(), c.getSaldo());
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(menuForm, "Error al cargar cuentas: " + ex.getMessage());
        }
    }

    private void inicializarEventos() {
        // Evento para btnUsuario
//        menuForm.getBtnUsuario().addActionListener(e -> abrirPantallaUsuario());

        // Evento para btnRealizarTransferencia
        menuForm.getBtnMostrarTransferencias().addActionListener(e -> abrirTransferenciaForm());

        // Evento para btnRetiroSinCuenta
//        menuForm.getBtnRetiroSinCuenta().addActionListener(e -> abrirRetiroSinCuentaForm());

        // Evento para btnConsultarOperaciones
//        menuForm.getBtnConsultarOperaciones().addActionListener(e -> abrirConsultarOperacionesForm());

        // Evento para btnCerrarSesion
//        menuForm.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
    }

//    private void abrirPantallaUsuario() {
//        UsuarioForm usuarioForm = new UsuarioForm(idCliente);
//        usuarioForm.setVisible(true);
//        menuForm.dispose(); // opcional: cerrar el menu principal
//    }

    private void abrirTransferenciaForm() {
        
        TransferenciaForm transferenciaForm = new TransferenciaForm(cuentasBO,transferenciasBO,clientesBO,idCliente);
        transferenciaForm.setVisible(true);
        menuForm.dispose();
    }

//    private void abrirRetiroSinCuentaForm() {
//        RetiroSinCuentaForm retiroForm = new RetiroSinCuentaForm(idCliente);
//        retiroForm.setVisible(true);
//        menuForm.dispose();
//    }

//    private void abrirConsultarOperacionesForm() {
//        ConsultarOperacionesForm operacionesForm = new ConsultarOperacionesForm(idCliente);
//        operacionesForm.setVisible(true);
//        menuForm.dispose();
//    }

    private void cerrarSesion() {
        menuForm.dispose(); // cierra menu principal
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}