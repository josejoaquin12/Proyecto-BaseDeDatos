package org.itson.proyecto01.control;

import java.util.List;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.itson.proyecto01.entidades.Cliente;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.negocio.IClientesBO;
import org.itson.proyecto01.negocio.ICuentasBO;
import org.itson.proyecto01.negocio.ITransferenciasBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.presentacion.TransferenciaForm;
import org.itson.proyecto01.presentacion.ConfirmarTransferenciaForm;

public class TransferenciaControl {

    private final TransferenciaForm transfrom;
    private final ICuentasBO cuentasBO;
    private final ITransferenciasBO transferenciasBO;
    private final IClientesBO clientesBO;
    private final int idCliente;
    private static final Logger LOGGER = Logger.getLogger(TransferenciaControl.class.getName());
    
    public TransferenciaControl(TransferenciaForm transfrom,ICuentasBO cuentasBO,ITransferenciasBO transferenciasBO,IClientesBO clientesBO,int idCliente) {
        this.transfrom = transfrom;
        this.cuentasBO = cuentasBO;
        this.transferenciasBO = transferenciasBO;
        this.clientesBO = clientesBO;
        this.idCliente = idCliente;

        transfrom.getBtnContinuarTransferencia().addActionListener(e -> continuar());
        transfrom.getTxtNumeroCuentaDestino().addActionListener(e -> buscarCuentaDestino());
        transfrom.getCboCuentasCliente().addActionListener(e -> actualizarSaldo());
        transfrom.getTxtMonto().addActionListener(e -> validarBotonContinuar());
        cargarCuentasCliente();
        validarBotonContinuar();
    }

    private void cargarCuentasCliente() {
        try {
            List<Cuenta> cuentas = cuentasBO.consultarCuentasCliente(idCliente);

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

        validarBotonContinuar();
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

        validarBotonContinuar();
    }

    private void validarBotonContinuar() {

        Cuenta cuenta = (Cuenta) transfrom.getCboCuentasCliente().getSelectedItem();
        String destino = transfrom.getTxtNumeroCuentaDestino().getText().trim();
        String monto = transfrom.getTxtMonto().getText().trim();

        if(cuenta != null && cuenta.getId()!= 0 && !destino.isEmpty() && !monto.isEmpty()){
            transfrom.getBtnContinuarTransferencia().setEnabled(true);
        }else{
            transfrom.getBtnContinuarTransferencia().setEnabled(false);
        }
    }

    private void continuar() {

        Cuenta cuentaOrigen = (Cuenta) transfrom.getCboCuentasCliente().getSelectedItem();

        if (cuentaOrigen == null || cuentaOrigen.getId() == 0) {
            JOptionPane.showMessageDialog(transfrom,"Selecciona una cuenta origen");
            return;
        }

        String destino= transfrom.getTxtNumeroCuentaDestino().getText().trim();

        String montoTxt= transfrom.getTxtMonto().getText().trim();

        try {

            double monto = Double.parseDouble(montoTxt);

            if (monto <= 0) {
                JOptionPane.showMessageDialog(transfrom,"El monto debe ser mayor a 0");
                return;
            }

            ConfirmarTransferenciaForm confirmar= new ConfirmarTransferenciaForm(cuentaOrigen.getNumeroCuenta(),destino,monto,transfrom.getLblNombreCuentaDestino().getText(),transferenciasBO,cuentaOrigen.getSaldo());

            confirmar.setVisible(true);
            transfrom.dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(transfrom,"Monto inválido");
        }
    }
}
