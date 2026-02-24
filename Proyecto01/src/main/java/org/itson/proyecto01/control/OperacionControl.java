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

/**
 * Clase controladora para la gestión y consulta de operaciones financieras.
 * <p>
 * Esta clase actúa como el mediador (Controller) en el patrón MVC para la
 * pantalla de consulta de operaciones. Se encarga de gestionar la lógica de
 * filtrado (por tipo de operación y rango de fecha), coordinar la actualización
 * de la interfaz gráfica (Swing) y manejar la navegación hacia otros módulos
 * del sistema.
 * </p>
 *
 * * @author Jesus Omar
 * @version 1.0
 */
public class OperacionControl {

    private final ConsultarOperacionesForm consultarOperacionesForm;
    private final IOperacionesBO operacionesBO;
    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();
    private static final Logger LOGGER = Logger.getLogger(OperacionControl.class.getName());
    private UtileriasControl utilerias ;

    /**
     * Constructor que inicializa el controlador de operaciones.
     * <p>
     * Realiza la inyección de dependencias de la vista, inicializa la capa de
     * negocio, configura los modelos de los filtros y establece los
     * escuchadores (Listeners) para los botones de navegación.
     * </p>
     *
     * @param consultarOperacionesForm La instancia de la interfaz gráfica a
     * controlar.
     */
    public OperacionControl(ConsultarOperacionesForm consultarOperacionesForm) {
        this.utilerias = new UtileriasControl();
        this.consultarOperacionesForm = consultarOperacionesForm;
        IOperacionesDAO operacionesDAO = new OperacionesDAO();
        this.operacionesBO = new OperacionesBO(operacionesDAO);
        this.configurarFiltros();
        this.cargarOperaciones();

        consultarOperacionesForm.getBtnUsuario().addActionListener(e -> {
            utilerias.abrirPantallaUsuario(consultarOperacionesForm);
        });
        consultarOperacionesForm.getBtnVerCuentas().addActionListener(e -> {
            utilerias.abrirMenuPrincipal(consultarOperacionesForm);
        });
        consultarOperacionesForm.getBtnRetiroSinCuenta1().addActionListener(e -> {
            utilerias.abrirRetiroConCuenta(consultarOperacionesForm);
        });
        consultarOperacionesForm.getBtnRealizarTransferencia().addActionListener(e -> {
            utilerias.abrirPantallaTransferencia(consultarOperacionesForm);
        });
    }

    /**
     * Recupera y filtra las operaciones del cliente para mostrarlas en la
     * tabla.
     * <p>
     * Consulta los valores seleccionados en los ComboBox de la vista para
     * aplicar filtros dinámicos. Dependiendo de la selección, invoca diferentes
     * métodos de {@link IOperacionesBO}. Si no se encuentran registros,
     * notifica al usuario.
     * </p>
     */
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

    /**
     * Actualiza el contenido visual de la tabla en la interfaz.
     * <p>
     * Limpia el modelo de la tabla actual y formatea los datos de cada objeto
     * {@link Operacion} (ID, Tipo, Fecha/Hora, Monto y Cuenta) para insertarlos
     * como filas nuevas.
     * </p>
     *
     * @param listaOperaciones Lista de operaciones obtenidas desde la base de
     * datos.
     */
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

    /**
     * Determina el objeto de fecha y hora inicial basado en la selección del
     * usuario.
     *
     * * @param seleccion El texto descriptivo del rango (ej. "Hoy", "Ultima
     * semana").
     * @return {@link LocalDateTime} configurado con el inicio del rango, o
     * {@code null} si se deben mostrar todas las fechas.
     */
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

    /**
     * Inicializa los modelos de los ComboBox de filtrado y sus eventos.
     * <p>
     * Carga dinámicamente los valores del enum {@link TipoOperacion} y los
     * rangos de fecha predefinidos. Configura la actualización automática de la
     * tabla al cambiar cualquier filtro.
     * </p>
     */
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
        consultarOperacionesForm.getTipoOperacionComboBox().addActionListener(e -> {
            cargarOperaciones();
        });
        consultarOperacionesForm.getRangoFechasComboBox().addActionListener(e -> {
            cargarOperaciones();
        });
    }
}
