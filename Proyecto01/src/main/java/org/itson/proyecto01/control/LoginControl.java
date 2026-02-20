package org.itson.proyecto01.control;

import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.presentacion.LoginForm;

public class LoginControl {

    private IClientesBO clienteBO;
    private LoginForm loginForm;

    public LoginControl(LoginForm loginForm) {
        IClientesDAO clienteDAO = new ClientesDAO();
        this.clienteBO = new ClientesBO(clienteDAO);
        this.loginForm = loginForm;
    }

    public void iniciarSesion(String correo, String password) throws ControlException, NegocioException {

        if (correo == null || correo.isBlank()) {
            throw new ControlException("El correo es obligatorio",null);
        }

        if (password == null || password.isBlank()) {
            throw new ControlException("La contraseña es obligatoria",null);
        }

        int idCliente = clienteBO.autenticarCorreoPassword(correo, password);

        if (idCliente <= 0) {
            throw new ControlException("Correo o contraseña incorrectos",null);
        }
    }
//    SeionControl.iniciarSesion(idCliente)
}

