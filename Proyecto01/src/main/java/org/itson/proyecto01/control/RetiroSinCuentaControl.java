package org.itson.proyecto01.control;

import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.entidades.Retiro;
import org.itson.proyecto01.negocio.ClientesBO;
import org.itson.proyecto01.negocio.CuentasBO;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.IRetiroBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.negocio.RetiroBO;
import org.itson.proyecto01.persistencia.ClientesDAO;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.IClientesDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.presentacion.LoginForm;
import org.itson.proyecto01.presentacion.RetiroSinCuentaForm;

/**
 *
 * @author joset
 */
public class RetiroSinCuentaControl {

    private final RetiroSinCuentaForm retiroSForm;

    private final ICuentasBO cuentasBO;
    private final ICuentasDAO cuentasDAO;

    private final IClientesBO clientesBO;
    private final IClientesDAO clientesDAO;

    private static final Logger LOGGER = Logger.getLogger(RetiroSinCuentaControl.class.getName());

    private  String numeroFolio;
    private  String contrasenia;
    private  Retiro retiro;

    public RetiroSinCuentaControl(RetiroSinCuentaForm retiroSForm) {
        this.cuentasDAO = new CuentasDAO();
        this.cuentasBO = new CuentasBO(cuentasDAO);

        this.clientesDAO = new ClientesDAO();
        this.clientesBO = new ClientesBO(clientesDAO);

        this.retiroSForm = retiroSForm;

        inicializarEventos();
    }

    private void inicializarEventos() {
        retiroSForm.getBtnRetirar().addActionListener(e -> cobrarRetiro());
        retiroSForm.getBtnCancelar().addActionListener(e -> abrirLogin());
        retiroSForm.getTxtContrasenia().addActionListener(e -> actualizarLabels());
        retiroSForm.getTxtFolio().addActionListener(e -> actualizarLabels());
        retiroSForm.getTxtContrasenia().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                actualizarLabels();
            }
        });
        

    }

    private void actualizarLabels() {
        if (this.retiro != null) {
            return;
        }
        try {

            if (!(retiroSForm.getTxtContrasenia().getText().isEmpty() && retiroSForm.getTxtFolio().getText().isEmpty())) {
                
                this.numeroFolio = retiroSForm.getTxtFolio().getText().trim();
                this.contrasenia = retiroSForm.getTxtContrasenia().getText().trim();
                
                this.retiro = validarRetiro(numeroFolio, contrasenia);               
                
                int idClienteCuentaRetiro = (cuentasBO.obtenerCuentaporNumeroCuenta(retiro.getNumeroCuentaOrigen().trim())).getIdCliente();
                String nombresCliente = (clientesBO.obtenerClientePorId(idClienteCuentaRetiro)).getNombres();
                String apellidoMCliente = (clientesBO.obtenerClientePorId(idClienteCuentaRetiro)).getApellidoM();
                String apellidoPCliente = (clientesBO.obtenerClientePorId(idClienteCuentaRetiro)).getApellidoP();
                retiroSForm.getLblNombreCuenta().setText(nombresCliente + " " + apellidoPCliente + " " + apellidoMCliente);
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String fechaFormateada = retiro.getFechaExpiracion().format(formatter);
                retiroSForm.getLblFechaExpiracion().setText(fechaFormateada);
                
                retiroSForm.getLblMontoRetiro().setText(String.format("$%,.2f", retiro.getMonto()));
            } else {

                retiroSForm.getLblNombreCuenta().setText("No encontrado");
                retiroSForm.getLblFechaExpiracion().setText("././.");
                retiroSForm.getLblMontoRetiro().setText("$ 0.00");
            }
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(retiroSForm,
                    "Datos de incompletos o incorrectos " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        }
    }

    public Retiro validarRetiro(String numFolio, String contrasenia) throws NegocioException {

        IRetiroBO retiroBO = new RetiroBO();
        return retiroBO.compararRetiro(numFolio, contrasenia);

    }
    
    
    public void cobrarRetiro() {
        try {
            this.numeroFolio = retiroSForm.getTxtFolio().getText().trim();
            this.contrasenia = retiroSForm.getTxtContrasenia().getText().trim();
            
            if (numeroFolio.isEmpty() || contrasenia.isEmpty()) {
                JOptionPane.showMessageDialog(retiroSForm,
                        "Ingrese folio y contraseña",
                        "Datos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (this.retiro == null) {
                JOptionPane.showMessageDialog(retiroSForm,
                        "Retiro no válido o datos incorrectos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            IRetiroBO retiroBO = new RetiroBO();
            retiroBO.cobrarRetiro(this.retiro);

            JOptionPane.showMessageDialog(retiroSForm,
                    "Retiro exitoso",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(retiroSForm,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirLogin() {
        LoginForm login = new LoginForm();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
        retiroSForm.dispose();
    }
}
