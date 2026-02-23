package org.itson.proyecto01.control;

import java.util.List;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.CuentasBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.ITransferenciasBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.negocio.TransferenciasBO;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.ITransferenciasDAO;
import org.itson.proyecto01.persistencia.TransferenciasDAO;
import org.itson.proyecto01.presentacion.TransferenciaForm;
import org.itson.proyecto01.presentacion.ConfirmarTransferenciaForm;
import org.itson.proyecto01.presentacion.ConsultarOperacionesForm;
import org.itson.proyecto01.presentacion.LoginForm;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.UsuarioForm;
import org.itson.proyecto01.presentacion.RetiroConCuentaForm;

public class TransferenciaControl {

    private final TransferenciaForm transfrom;
    private final ICuentasBO cuentasBO;
    private final ITransferenciasBO transferenciasBO;
    private final IClientesBO clientesBO;
    private final int idCliente;
    private static final Logger LOGGER = Logger.getLogger(TransferenciaControl.class.getName());

    public TransferenciaControl(TransferenciaForm transfrom, int idCliente) {
         // Inicializar BO 
        ICuentasDAO cuentasDAO = new CuentasDAO();
        IClientesDAO clientesDAO = new ClientesDAO();
        ITransferenciasDAO transferenciasDAO = new TransferenciasDAO(cuentasDAO);
        
        //InicializarBO
        this.clientesBO = new ClientesBO(clientesDAO);
        this.cuentasBO = new CuentasBO(cuentasDAO);
        this.transferenciasBO = new TransferenciasBO(transferenciasDAO, cuentasDAO);
        
        this.transfrom = transfrom;
        this.idCliente = idCliente;

        transfrom.getBtnContinuarTransferencia().addActionListener(e -> continuar());
        transfrom.getTxtNumeroCuentaDestino().addActionListener(e -> buscarCuentaDestino());
        transfrom.getCboCuentasCliente().addActionListener(e -> actualizarSaldo());
        transfrom.getBtnMostrarRetiroSinCuenta().addActionListener(e -> abrirRetiroConCuenta());
        transfrom.getBtnMostrarMenu().addActionListener(e -> abrirMenuPrincipal(idCliente));
        transfrom.getBtnCerrarSesion().addActionListener(e -> cerrarSesion());
        transfrom.getBtnConsultarOperaciones().addActionListener(e -> abrirConsultarOperacionesForm());       
        transfrom.getBtnUsuario().addActionListener(e -> abrirMenuPrincipal(idCliente));
        cargarCuentasCliente();
    }

    private void abrirMenuPrincipal(int idCliente) {
        MenuPrincipalForm menu = new MenuPrincipalForm(idCliente);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        transfrom.dispose();
    }
    private void abrirPantallaUsuario() {
        UsuarioForm usuarioForm = new UsuarioForm();
        usuarioForm.setVisible(true);
        transfrom.dispose();
    }

    private void abrirRetiroConCuenta() {
        RetiroConCuentaForm RetiroConCuenta = new RetiroConCuentaForm();
        RetiroConCuentaControl abrirRetiroConCuentaControl = new RetiroConCuentaControl(RetiroConCuenta, idCliente);
        RetiroConCuenta.setVisible(true);
        transfrom.dispose();
    }
    
    private void abrirConsultarOperacionesForm() {
        ConsultarOperacionesForm operacionesForm = new ConsultarOperacionesForm();
        //OperacionControl operacionesControl = new OperacionControl();
        operacionesForm.setLocationRelativeTo(null);
        operacionesForm.setVisible(true);
        transfrom.dispose();
    }

    private void cerrarSesion() {
        transfrom.dispose(); // cierra menu principal
        LoginForm loginForm = new LoginForm();
        loginForm.setLocationRelativeTo(null);
        loginForm.setVisible(true);
    }

    private void cargarCuentasCliente() {
        try {
            List<Cuenta> cuentas = cuentasBO.consultarCuentasClienteActivas(idCliente);

            DefaultComboBoxModel<Cuenta> modelo = new DefaultComboBoxModel<>();
            modelo.addElement(new Cuenta(0, "Seleccione una cuenta...", null, 0.0, null, 0));

            for (Cuenta c : cuentas) {
                modelo.addElement(c);
            }

            transfrom.getCboCuentasCliente().setModel(modelo);

        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    private void actualizarSaldo() {
        Cuenta cuenta = (Cuenta) transfrom.getCboCuentasCliente().getSelectedItem();

        if (cuenta != null && cuenta.getId() != 0) {
            transfrom.getLblSaldoDisponible().setText(String.format("$%,.2f", cuenta.getSaldo()));
        } else {
            transfrom.getLblSaldoDisponible().setText("$ 0.00");
        }
    }

    private void buscarCuentaDestino() {

        String numero = transfrom.getTxtNumeroCuentaDestino().getText().trim();

        if (numero.isEmpty()) {
            transfrom.getLblNombreCuentaDestino().setText("");
            return;
        }

        try {
            Cuenta cuentaDestino = cuentasBO.obtenerCuentaporNumeroCuenta(numero);
            Cliente cliente = this.clientesBO.obtenerClientePorId(cuentaDestino.getIdCliente());
            String nombreCompleto = cliente.getNombres() + " " + cliente.getApellidoP() + " " + cliente.getApellidoM();
            if (cuentaDestino != null) {
                transfrom.getLblNombreCuentaDestino().setText(nombreCompleto);
            } else {
                transfrom.getLblNombreCuentaDestino().setText("Cuenta no encontrada");
            }

        } catch (NegocioException ex) {
            transfrom.getLblNombreCuentaDestino().setText("Error al buscar");
        }
    }

    private void validarBotonContinuar() {
            Cuenta cuenta = (Cuenta) transfrom.getCboCuentasCliente().getSelectedItem();
            String destino = transfrom.getTxtNumeroCuentaDestino().getText().trim();
            String monto = transfrom.getTxtMonto().getText().trim();
            String saldoDisponible = transfrom.getLblSaldoDisponible().getText().trim();

            String textosaldoDisponible = saldoDisponible
                    .replace("$", "")
                    .replace(",", "")
                    .trim();

            double montInt = Double.parseDouble(monto);
            double saldoDisponibleInt = Double.parseDouble(textosaldoDisponible);

            if (cuenta != null && cuenta.getId() != 0 && !destino.isEmpty() && !monto.isEmpty() && saldoDisponibleInt > 0 && saldoDisponibleInt <= montInt) {
                transfrom.getBtnContinuarTransferencia().setEnabled(true);
            } else {
                transfrom.getBtnContinuarTransferencia().setEnabled(false);
            }
    }

    private void continuar() {
        try {
            validarBotonContinuar();
            Cuenta cuentaOrigen = (Cuenta) transfrom.getCboCuentasCliente().getSelectedItem();

            String destino = transfrom.getTxtNumeroCuentaDestino().getText().trim();

            String montoTxt = transfrom.getTxtMonto().getText().trim();

            double monto = Double.parseDouble(montoTxt);

            ConfirmarTransferenciaForm confirmar = new ConfirmarTransferenciaForm(cuentaOrigen.getNumeroCuenta(), destino, monto, transfrom.getLblNombreCuentaDestino().getText(), transferenciasBO, cuentaOrigen.getSaldo());

            confirmar.setVisible(true);
            transfrom.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(transfrom, "Error monto invalido: "+ex.getMessage());
        }

    }
}
