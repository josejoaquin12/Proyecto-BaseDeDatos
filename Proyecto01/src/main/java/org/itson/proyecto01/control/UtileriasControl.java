/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.control;

import javax.swing.JFrame;
import org.itson.proyecto01.presentacion.AltaCuentaForm;
import org.itson.proyecto01.presentacion.CerrarCuentaForm;
import org.itson.proyecto01.presentacion.ConsultarOperacionesForm;
import org.itson.proyecto01.presentacion.LoginForm;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.RegistroForm;
import org.itson.proyecto01.presentacion.RetiroConCuentaForm;
import org.itson.proyecto01.presentacion.RetiroSinCuentaForm;
import org.itson.proyecto01.presentacion.TransferenciaForm;
import org.itson.proyecto01.presentacion.UsuarioForm;

/**
 *
 * @author joset
 */
public class UtileriasControl {
    /**
     * Realiza la transición hacia la pantalla del Menú Principal. Cierra la
     * ventana actual y libera sus recursos.
     */
    public void abrirMenuPrincipal(JFrame form) {
        MenuPrincipalForm menuPrincipal = new MenuPrincipalForm();
        MenuControl menuPrinciCont = new MenuControl(menuPrincipal);
        menuPrincipal.setLocationRelativeTo(null);
        menuPrincipal.setVisible(true);
       form.dispose();
    }

    /**
     * Realiza la transición hacia la pantalla de gestión de Usuario.* 
     *
     * @param form
     */
    public void abrirPantallaUsuario(JFrame form) {

        UsuarioForm usuarioForm = new UsuarioForm();
        UsuarioControl usControl = new UsuarioControl(usuarioForm);
        usuarioForm.setLocationRelativeTo(null);
        usuarioForm.setVisible(true);
        form.dispose();
    }

    /**
     * Realiza la transición hacia la pantalla de Retiro con Cuenta.
     * @param form
     */
    public void abrirRetiroConCuenta(JFrame form) {
        RetiroConCuentaForm RetiroConCuenta = new RetiroConCuentaForm();
        RetiroConCuentaControl abrirRetiroConCuentaControl = new RetiroConCuentaControl(RetiroConCuenta);
        RetiroConCuenta.setVisible(true);
        form.dispose();
    }

    /**
     * Realiza la transición hacia la pantalla de realizar Transferencias.
     * @param form
     */
    public void abrirPantallaTransferencia(JFrame form) {

        TransferenciaForm Transferenciaform = new TransferenciaForm();
        TransferenciaControl TransferenciaControl = new TransferenciaControl(Transferenciaform);
        Transferenciaform.setVisible(true);
        form.dispose();

    }
    /**
     * Gestiona la transición hacia el formulario de Registro de Clientes.
     * <p>
     * Instancia el nuevo controlador {@link RegistroControl} y destruye la
     * vista actual.
     * </p>
     * @param form
     */
    public void abrirPantallaRegistroForm(JFrame form) {

        RegistroForm RegistroForm = new RegistroForm();
        RegistroControl registorControl = new RegistroControl(RegistroForm);
        RegistroForm.setLocationRelativeTo(null);
        RegistroForm.setVisible(true);
        form.dispose();
    }

    /**
     * Gestiona la transición hacia el módulo de Retiros sin Cuenta.
     * <p>
     * Permite al usuario acceder a esta funcionalidad específica sin haber
     * iniciado sesión previamente.
     * </p>
     * @param form
     */
    public void abrirRetiroSinCuenta(JFrame form) {

        RetiroSinCuentaForm retiroSinCuenta = new RetiroSinCuentaForm();
        RetiroSinCuentaControl retiroSinCuentaControl = new RetiroSinCuentaControl(retiroSinCuenta);
        retiroSinCuenta.setLocationRelativeTo(null);
        retiroSinCuenta.setVisible(true);
        form.dispose();
    }
    /**
     * Gestiona la transición hacia el formulario para dar de alta una nueva
     * cuenta.
     * @param form
     */
    public void abrirAltaCuentaForm(JFrame form) {
        AltaCuentaForm altaCuentaForm = new AltaCuentaForm();
        AltaCuentaControl altaCuentaControl = new AltaCuentaControl(altaCuentaForm);
        altaCuentaForm.setVisible(true);
        form.dispose();
    }
    /**
     * Gestiona la transición hacia el formulario de Transferencias.
     */
    public void abrirTransferenciaForm(JFrame form) {

        TransferenciaForm transform = new TransferenciaForm();
        TransferenciaControl TransferenciaControl = new TransferenciaControl(transform);
        form.setVisible(true);
        form.dispose();

    }
    /**
     * Gestiona la transición hacia el formulario para cancelar o cerrar una
     * cuenta.
     */
    public void abrirCerrarCuentaForm(JFrame form) {
        CerrarCuentaForm cerrarCuentaForm = new CerrarCuentaForm();
        CerrarCuentaControl cerrarCuentaControl = new CerrarCuentaControl(cerrarCuentaForm);
        cerrarCuentaForm.setVisible(true);
        form.dispose();
    }
    /**
     * Gestiona la transición hacia el formulario de consulta de operaciones
     * históricas. Configura la ventana para aparecer centrada en la pantalla.
     */
    public void abrirConsultarOperacionesForm(JFrame form) {
        ConsultarOperacionesForm operacionesForm = new ConsultarOperacionesForm();
        operacionesForm.setLocationRelativeTo(null);
        OperacionControl operacionesControl = new OperacionControl(operacionesForm);
        operacionesForm.setVisible(true);
        form.dispose();
    }
        /**
     * Finaliza la sesión actual del usuario.
     * <p>
     * Destruye la ventana del menú principal y redirige al usuario de vuelta a
     * la pantalla de Login.
     * </p>
     */
    public void cerrarSesion(JFrame form) {
        form.dispose(); // cierra menu principal
        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
    }
     /**
     * Gestiona el retorno a la pantalla de Login.
     * <p>
     * Centra la ventana de inicio de sesión y libera los recursos del formulario actual.
     * </p>
     */
    public void abrirLogin(JFrame form) {
        LoginForm login = new LoginForm();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        form.dispose();
    }
}
