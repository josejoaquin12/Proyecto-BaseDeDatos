/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.control;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.itson.proyecto01.entidades.Operacion;
import org.itson.proyecto01.enums.TipoOperacion;
import org.itson.proyecto01.negocio.IOperacionesBO;
import org.itson.proyecto01.negocio.NegocioException;
import org.itson.proyecto01.negocio.OperacionesBO;
import org.itson.proyecto01.persistencia.IOperacionesDAO;
import org.itson.proyecto01.persistencia.OperacionesDAO;
import org.itson.proyecto01.presentacion.ConsultarOperacionesForm;
import org.itson.proyecto01.presentacion.MenuPrincipalForm;
import org.itson.proyecto01.presentacion.RetiroConCuentaForm;
import org.itson.proyecto01.presentacion.TransferenciaForm;

/**
 *
 * @author Jesus Omar
 */
public class OperacionControl {

    private final ConsultarOperacionesForm consultarOperacionesForm;
    private final IOperacionesBO operacionesBO;
    private final int idCliente;
    private static final Logger LOGGER = Logger.getLogger(OperacionControl.class.getName());

    public OperacionControl(ConsultarOperacionesForm consultarOperacionesForm, int idCliente) {
        this.consultarOperacionesForm = consultarOperacionesForm;
        IOperacionesDAO operacionesDAO = new OperacionesDAO();
        this.operacionesBO = new OperacionesBO(operacionesDAO);
        this.idCliente = idCliente;
        this.configurarFiltros();
        this.cargarOperaciones();
        
        consultarOperacionesForm.getBtnUsuario().addActionListener(e->{abrirPantallaUsuario();});
        consultarOperacionesForm.getBtnVerCuentas().addActionListener(e->{abrirMenuPrincipal();});
        consultarOperacionesForm.getBtnRetiroSinCuenta1().addActionListener(e->{abrirRetiroConCuenta();});
        consultarOperacionesForm.getBtnRealizarTransferencia().addActionListener(e->{abrirPantallaTransferencia();});
    }

    // Metodo para cargar todas las operaciones del cliente a la tabla
    public void cargarOperaciones() {
        try {
            // Valores de los comboBox
            String seleccionTipo = (String) consultarOperacionesForm.getTipoOperacionComboBox().getSelectedItem();
            String seleccionFecha = (String) consultarOperacionesForm.getRangoFechasComboBox().getSelectedItem();

            boolean filtroTipoActivo = !seleccionTipo.equals("TODAS");
            boolean filtroFechaActivo = !seleccionFecha.equals("TODAS");

            List<Operacion> listaOperaciones;

            if (filtroTipoActivo && filtroFechaActivo) {
                // En caso de que los dos filtros esten seleccionados
                TipoOperacion tipo = TipoOperacion.valueOf(seleccionTipo);
                // calcularFechaInicio es un metodo auxiliar esta mas abajo
                LocalDateTime fechaInicio = calcularFechaInicio(seleccionFecha);
                listaOperaciones = operacionesBO.operacionesPorFechaTipo(idCliente, tipo, fechaInicio, LocalDateTime.now());
            } else if (filtroTipoActivo) {
                TipoOperacion tipo = TipoOperacion.valueOf(seleccionTipo);
                listaOperaciones = operacionesBO.operacionesPorTipo(idCliente, tipo);
            } else if (filtroFechaActivo) {
                LocalDateTime fechaInicio = calcularFechaInicio(seleccionFecha);
                listaOperaciones = operacionesBO.operacionesPorFecha(idCliente, fechaInicio, LocalDateTime.now());
            } else {
                listaOperaciones = operacionesBO.obtenerOperaciones(idCliente);
            }
            // Llenar tabla es otro metodo que hice tambien mas abajo
            llenarTabla(listaOperaciones);

            if (listaOperaciones.isEmpty()) {
                JOptionPane.showMessageDialog(consultarOperacionesForm, "No hay operaciones registradas");
            }

        } catch (NegocioException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    // Llenar tabla
    private void llenarTabla(List<Operacion> listaOperaciones) {
        DefaultTableModel modelo = (DefaultTableModel) consultarOperacionesForm.getTablaOperaciones().getModel();
        modelo.setRowCount(0);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Operacion operacion : listaOperaciones) {
            String fechaFormateada = operacion.getFechaHoraOperacion().format(formato);
            Object[] fila = {
                operacion.getIdOperacion(),
                operacion.getTipoOperacion(),
                fechaFormateada,
                operacion.getMonto(),
                operacion.getNumeroCuenta()
            };
            modelo.addRow(fila);
        }
    }

    // calcular las fechas inicio
    private LocalDateTime calcularFechaInicio(String seleccion) {
        switch (seleccion) {
            case "Hoy":
                return LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
            case "Ultima semana":
                return LocalDateTime.now().minusDays(7);
            case "Mes actual":
                return LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
            default:
                return null;
        }
    }

    private void configurarFiltros() {
        // Carga las opciones del tipo de operacion
        DefaultComboBoxModel<String> modeloTipo = new DefaultComboBoxModel<>();
        modeloTipo.addElement("TODAS");
        for (TipoOperacion tipo : TipoOperacion.values()) {
            modeloTipo.addElement(tipo.name());
        }
        consultarOperacionesForm.setModelTipoOperacion(modeloTipo);

        // Carga las opciones de fechas
        DefaultComboBoxModel<String> modeloFechas = new DefaultComboBoxModel<>();
        modeloFechas.addElement("TODAS");
        modeloFechas.addElement("Hoy");
        modeloFechas.addElement("Ultima semana");
        modeloFechas.addElement("Mes actual");
        consultarOperacionesForm.setModelRangoFechas(modeloFechas);
        
        // Listeners de los comboBox
        consultarOperacionesForm.getTipoOperacionComboBox().addActionListener(e-> {
            cargarOperaciones();
        });
        consultarOperacionesForm.getRangoFechasComboBox().addActionListener(e->{
            cargarOperaciones();
        });
    }

    private void abrirMenuPrincipal(){
        MenuPrincipalForm menuPrincipal = new MenuPrincipalForm(idCliente);
        menuPrincipal.setLocationRelativeTo(null);
        menuPrincipal.setVisible(true);
        consultarOperacionesForm.dispose();
    }
    
    private void abrirPantallaUsuario(){
        PerfilUsuarioForm usuarioForm = new PerfilUsuarioForm();
        usuarioForm.setLocationRelativeTo(null);
        usuarioForm.setVisible(true);
        consultarOperacionesForm.dispose();
    }
    
    private void abrirRetiroConCuenta(){
        RetiroConCuentaForm pantallaRetiro = new RetiroConCuentaForm();
        pantallaRetiro.setLocationRelativeTo(null);
        pantallaRetiro.setVisible(true);
        consultarOperacionesForm.dispose();
    }
    
    private void abrirPantallaTransferencia(){
        TransferenciaForm pantallaTransferencia = new TransferenciaForm();
        pantallaTransferencia.setLocationRelativeTo(null);
        pantallaTransferencia.setVisible(true);
        consultarOperacionesForm.dispose();
    }
    
}
