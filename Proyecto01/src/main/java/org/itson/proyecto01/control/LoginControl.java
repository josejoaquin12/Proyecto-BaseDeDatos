package org.itson.proyecto01.control;

import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.presentacion.LoginForm;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;

public class LoginControl {

    private IClientesBO clienteBO;
    private LoginForm loginForm;

    public LoginControl(LoginForm loginForm) {
        this.loginForm = loginForm;
        IClientesDAO clienteDAO = new ClientesDAO();
        this.clienteBO = new ClientesBO(clienteDAO);
    }

    public void iniciarSesion(String nombreCompleto, String password) throws ControlException, NegocioException {

        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new ControlException("El correo es obligatorio", null);
        }

        if (password == null || password.isBlank()) {
            throw new ControlException("La contraseña es obligatoria", null);
        }

        int idCliente = clienteBO.autenticarNombreCompletoPassword(nombreCompleto, password);

        if (idCliente <= 0) {
            throw new ControlException("Correo o contraseña incorrectos", null);
        }
        abrirMenuPrincipal(idCliente);
    }
    private void abrirMenuPrincipal(int idCliente) {
        MenuPrincipalForm menu = new MenuPrincipalForm(idCliente);
        menu.setVisible(true);

        loginForm.dispose();
    }
}

