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

public class LoginControl {

    private final IClientesBO clienteBO;
    private LoginForm loginForm;

    public LoginControl(LoginForm loginForm) {
        this.loginForm = loginForm;
        IClientesDAO clienteDAO = new ClientesDAO();
        this.clienteBO = new ClientesBO(clienteDAO);
        inicializarEventos();
    }

    public void iniciarSesion(String nombreCompleto, String password) throws ControlException, NegocioException {

        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new ControlException("El correo es obligatorio", null);
        }

        if (password == null || password.isBlank()) {
            throw new ControlException("La contraseña es obligatoria", null);
        }
        try{
            int idCliente = clienteBO.autenticarNombreCompletoPassword(nombreCompleto, password);
            if (idCliente <= 0) {
                throw new ControlException("Nombre o contraseña incorrectos", null);
            }
            // Obtiene el cliente que inicio sesion por su id
            Cliente cliente = clienteBO.obtenerClientePorId(idCliente);
            // Guarda la sesion
            SesionControl.getSesion().guardarSesion(cliente);
            // Manda al usuario al menu principal
            abrirMenuPrincipal();
        } catch (NegocioException | ControlException  ex) {
            JOptionPane.showMessageDialog(loginForm,"Error, no se encontro el cliente "+ex.getMessage(), "Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void inicializarEventos() {
        loginForm.getBtnRegistrar().addActionListener(e -> abrirPantallaRegistroForm());
        loginForm.getBtnRetiroSinCuenta().addActionListener(e -> abrirRetiroSinCuenta());    
    }
    
    
    
    private void abrirMenuPrincipal() {
        MenuPrincipalForm menu = new MenuPrincipalForm();
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        loginForm.dispose();
    }
    private void abrirPantallaRegistroForm() {
        
        RegistroForm RegistroForm = new RegistroForm();
        RegistroControl registorControl = new RegistroControl(RegistroForm);
        RegistroForm.setLocationRelativeTo(null);
        RegistroForm.setVisible(true);
        loginForm.dispose(); 
    }
    private void abrirRetiroSinCuenta() {
        
        RetiroSinCuentaForm retiroSinCuenta = new RetiroSinCuentaForm();
        RetiroSinCuentaControl retiroSinCuentaControl = new RetiroSinCuentaControl(retiroSinCuenta);
        retiroSinCuenta.setLocationRelativeTo(null);
        retiroSinCuenta.setVisible(true);
        loginForm.dispose(); 
    }
}

