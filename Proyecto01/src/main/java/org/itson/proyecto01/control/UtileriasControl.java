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
 * Clase de utilerías encargada de gestionar la navegación entre formularios
 * de la aplicación. Cada método se encarga de abrir una nueva vista,
 * inicializar su controlador correspondiente y cerrar el formulario actual.
 */
public class UtileriasControl {

    /**
     * Abre la pantalla del Menú Principal.
     * <p>
     * Inicializa el formulario {@link MenuPrincipalForm}, asigna su controlador
     * y cierra la ventana actual.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirMenuPrincipal(JFrame form) {
        MenuPrincipalForm menuPrincipal = new MenuPrincipalForm();
        MenuControl menuPrinciCont = new MenuControl(menuPrincipal);
        menuPrincipal.setLocationRelativeTo(null);
        menuPrincipal.setVisible(true);
        form.dispose();
    }

    /**
     * Abre la pantalla de gestión de usuarios.
     * <p>
     * Inicializa el formulario {@link UsuarioForm}, crea su controlador y
     * finaliza la vista actual.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirPantallaUsuario(JFrame form) {
        UsuarioForm usuarioForm = new UsuarioForm();
        UsuarioControl usControl = new UsuarioControl(usuarioForm);
        usuarioForm.setLocationRelativeTo(null);
        usuarioForm.setVisible(true);
        form.dispose();
    }

    /**
     * Abre la pantalla de retiro con cuenta.
     * <p>
     * Inicializa el formulario {@link RetiroConCuentaForm} y su controlador
     * correspondiente.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirRetiroConCuenta(JFrame form) {
        RetiroConCuentaForm retiroConCuenta = new RetiroConCuentaForm();
        RetiroConCuentaControl retiroConCuentaControl =new RetiroConCuentaControl(retiroConCuenta);
        retiroConCuenta.setVisible(true);
        form.dispose();
    }

    /**
     * Abre la pantalla para realizar transferencias.
     * <p>
     * Inicializa el formulario {@link TransferenciaForm} y asigna su controlador.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirPantallaTransferencia(JFrame form) {
        TransferenciaForm transferenciaForm = new TransferenciaForm();
        TransferenciaControl transferenciaControl =
                new TransferenciaControl(transferenciaForm);
        transferenciaForm.setVisible(true);
        form.dispose();
    }

    /**
     * Abre la pantalla de registro de clientes.
     * <p>
     * Inicializa el formulario {@link RegistroForm} junto con su controlador.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirPantallaRegistroForm(JFrame form) {
        RegistroForm registroForm = new RegistroForm();
        RegistroControl registroControl = new RegistroControl(registroForm);
        registroForm.setLocationRelativeTo(null);
        registroForm.setVisible(true);
        form.dispose();
    }

    /**
     * Abre la pantalla de retiro sin cuenta.
     * <p>
     * Permite acceder a esta funcionalidad sin necesidad de iniciar sesión.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirRetiroSinCuenta(JFrame form) {
        RetiroSinCuentaForm retiroSinCuenta = new RetiroSinCuentaForm();
        RetiroSinCuentaControl retiroSinCuentaControl = new RetiroSinCuentaControl(retiroSinCuenta);
        retiroSinCuenta.setLocationRelativeTo(null);
        retiroSinCuenta.setVisible(true);
        form.dispose();
    }

    /**
     * Abre la pantalla para dar de alta una nueva cuenta bancaria.
     * <p>
     * Inicializa el formulario {@link AltaCuentaForm} y su controlador.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirAltaCuentaForm(JFrame form) {
        AltaCuentaForm altaCuentaForm = new AltaCuentaForm();
        AltaCuentaControl altaCuentaControl =
                new AltaCuentaControl(altaCuentaForm);
        altaCuentaForm.setVisible(true);
        form.dispose();
    }

    /**
     * Abre la pantalla para cerrar o cancelar una cuenta.
     * <p>
     * Inicializa el formulario {@link CerrarCuentaForm} y su controlador.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirCerrarCuentaForm(JFrame form) {
        CerrarCuentaForm cerrarCuentaForm = new CerrarCuentaForm();
        CerrarCuentaControl cerrarCuentaControl =
                new CerrarCuentaControl(cerrarCuentaForm);
        cerrarCuentaForm.setVisible(true);
        form.dispose();
    }

    /**
     * Abre la pantalla de consulta de operaciones.
     * <p>
     * Inicializa el formulario {@link ConsultarOperacionesForm}, lo centra
     * en pantalla y asigna su controlador.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirConsultarOperacionesForm(JFrame form) {
        ConsultarOperacionesForm operacionesForm =
                new ConsultarOperacionesForm();
        operacionesForm.setLocationRelativeTo(null);
        OperacionControl operacionesControl =
                new OperacionControl(operacionesForm);
        operacionesForm.setVisible(true);
        form.dispose();
    }

    /**
     * Cierra la sesión del usuario actual.
     * <p>
     * Destruye la ventana activa y redirige al formulario de inicio de sesión.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void cerrarSesion(JFrame form) {
        form.dispose();
        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
    }

    /**
     * Abre la pantalla de inicio de sesión.
     * <p>
     * Inicializa el formulario {@link LoginForm} y cierra la vista actual.
     * </p>
     *
     * @param form formulario actual a cerrar
     */
    public void abrirLogin(JFrame form) {
        LoginForm login = new LoginForm();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        form.dispose();
    }
}