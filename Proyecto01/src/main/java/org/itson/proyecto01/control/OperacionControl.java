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
import org.itson.proyecto01.presentacion.UsuarioForm;

/**
 * Controlador principal para la gestión y consulta de operaciones bancarias.
 * <p>
 * Esta clase actúa como el mediador (Controller) en el patrón MVC, encargándose
 * de:
 * <ul>
 * <li>Gestionar la interacción entre la vista de consultas y la lógica de
 * negocio.</li>
 * <li>Filtrar transacciones por tipo y rangos de fecha dinámicos.</li>
 * <li>Orquestar la navegación hacia otros módulos como retiros, transferencias
 * y perfil de usuario.</li>
 * </ul>
 * </p>
 * * @author Jesus Omar
 * @version 1.0
 * @see OperacionesBO
 * @see ConsultarOperacionesForm
 */
public class OperacionControl {

    private final ConsultarOperacionesForm consultarOperacionesForm;
    private final IOperacionesBO operacionesBO;
    private final Integer idCliente = SesionControl.getSesion().getCliente().getId();
    private static final Logger LOGGER = Logger.getLogger(OperacionControl.class.getName());

    /**
     * Constructor de la clase controladora para la gestión de operaciones.
     * <p>
     * Este constructor inicializa el flujo de trabajo de la interfaz de
     * consultas realizando:
     * <ul>
     * <li><b>Inyección de dependencias:</b> Vincula la vista y configura las
     * capas de acceso a datos (DAO) y lógica de negocio (BO).</li>
     * <li><b>Preparación de la interfaz:</b> Ejecuta
     * {@link #configurarFiltros()} y realiza la carga inicial de datos con
     * {@link #cargarOperaciones()}.</li>
     * <li><b>Registro de eventos:</b> Configura los {@code ActionListeners}
     * para todos los botones de navegación (Usuario, Cuentas, Retiros y
     * Transferencias).</li>
     * </ul>
     * </p>
     *
     * @param consultarOperacionesForm Instancia de la vista (JFrame) que este
     * controlador debe administrar.
     * @see #configurarFiltros()
     * @see #cargarOperaciones()
     */
    public OperacionControl(ConsultarOperacionesForm consultarOperacionesForm) {
        this.consultarOperacionesForm = consultarOperacionesForm;
        IOperacionesDAO operacionesDAO = new OperacionesDAO();
        this.operacionesBO = new OperacionesBO(operacionesDAO);
        this.configurarFiltros();
        this.cargarOperaciones();

        consultarOperacionesForm.getBtnUsuario().addActionListener(e -> {
            abrirPantallaUsuario();
        });
        consultarOperacionesForm.getBtnVerCuentas().addActionListener(e -> {
            abrirMenuPrincipal();
        });
        consultarOperacionesForm.getBtnRetiroSinCuenta1().addActionListener(e -> {
            abrirRetiroConCuenta();
        });
        consultarOperacionesForm.getBtnRealizarTransferencia().addActionListener(e -> {
            abrirPantallaTransferencia();
        });
    }

    /**
     * Carga y filtra las operaciones financieras del cliente para ser
     * visualizadas en la interfaz.
     * <p>
     * Este método recupera las selecciones de tipo de operación y rango de
     * fechas desde los ComboBox de la vista. Dependiendo de los filtros
     * activos, consulta a la capa de negocio ({@code operacionesBO}) para
     * obtener la lista de operaciones correspondiente. Finalmente, actualiza la
     * tabla de la interfaz y notifica al usuario si no se encontraron
     * resultados.
     * </p>
     *
     * @throws NegocioException Si ocurre un error al consultar las operaciones
     * en la base de datos o al procesar la lógica de negocio.
     * @see #llenarTabla(List)
     * @see #calcularFechaInicio(String)
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
     * Actualiza la tabla de operaciones en la interfaz gráfica con la lista
     * proporcionada.
     * <p>
     * El método limpia el modelo de la tabla actual, define un formato de fecha
     * ({@code dd/MM/yyyy HH:mm}) y recorre cada objeto {@code Operacion} para
     * extraer sus atributos (ID, Tipo, Fecha, Monto y Cuenta) y agregarlos como
     * una nueva fila en el componente visual.
     * </p>
     *
     * @param listaOperaciones Una lista de objetos {@link Operacion} que
     * contiene los datos a mostrar en la interfaz.
     * @see #cargarOperaciones()
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
     * Calcula la fecha y hora de inicio para el filtrado de operaciones
     * basándose en una selección textual.
     * <p>
     * El método evalúa la cadena recibida y devuelve un {@link LocalDateTime}
     * configurado:
     * <ul>
     * <li><b>"Hoy":</b> Las 00:00:00 horas del día actual.</li>
     * <li><b>"Ultima semana":</b> La fecha actual menos 7 días.</li>
     * <li><b>"Mes actual":</b> El primer día del mes en curso a las 00:00
     * horas.</li>
     * </ul>
     * </p>
     *
     * @param seleccion Texto proveniente del ComboBox de rangos de fecha.
     * @return Un objeto {@code LocalDateTime} que representa el punto de inicio
     * del filtro, o {@code null} si la selección no coincide con ningún caso o
     * es "TODAS".
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
     * Inicializa y configura los componentes de filtrado en la interfaz de
     * usuario.
     * <p>
     * Este método realiza tres tareas principales:
     * <ul>
     * <li><b>Configura el tipo de operación:</b> Carga las opciones del enum
     * {@code TipoOperacion} en el ComboBox correspondiente, incluyendo una
     * opción global ("TODAS").</li>
     * <li><b>Configura los rangos de fecha:</b> Define las opciones temporales
     * predeterminadas (Hoy, Última semana, Mes actual) para el filtrado
     * cronológico.</li>
     * <li><b>Establece Listeners:</b> Asigna manejadores de eventos
     * (ActionListeners) a ambos ComboBox para que la tabla se actualice
     * automáticamente mediante {@link #cargarOperaciones()} cada vez que el
     * usuario cambie una selección.</li>
     * </ul>
     * </p>
     *
     * * @see #cargarOperaciones()
     * @see TipoOperacion
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

    /**
     * Realiza la transición de la pantalla actual hacia el menú principal de la
     * aplicación.
     * <p>
     * Este método instancia la ventana {@code MenuPrincipalForm}, la posiciona
     * en el centro de la pantalla y la hace visible para el usuario. Acto
     * seguido, libera los recursos de la ventana de consulta actual
     * ({@code consultarOperacionesForm}) mediante el método {@code dispose()}.
     * </p>
     *
     * * @see MenuPrincipalForm
     * @see javax.swing.JFrame#dispose()
     */
    private void abrirMenuPrincipal() {
        MenuPrincipalForm menuPrincipal = new MenuPrincipalForm();
        menuPrincipal.setLocationRelativeTo(null);
        menuPrincipal.setVisible(true);
        consultarOperacionesForm.dispose();
    }

    /**
     * Gestiona la transición hacia la pantalla de perfil o detalles del
     * usuario.
     * <p>
     * Este método instancia un nuevo formulario {@code UsuarioForm}, lo centra
     * en la pantalla mediante {@code setLocationRelativeTo(null)} y lo
     * despliega visualmente. Finalmente, cierra y libera los recursos de la
     * ventana de consultas actual ({@code consultarOperacionesForm}).
     * </p>
     *
     * * @see UsuarioForm
     * @see javax.swing.JFrame#dispose()
     */
    private void abrirPantallaUsuario() {
        UsuarioForm usuarioForm = new UsuarioForm();
        usuarioForm.setLocationRelativeTo(null);
        usuarioForm.setVisible(true);
        consultarOperacionesForm.dispose();
    }

    /**
     * Gestiona la transición hacia la pantalla de retiros con cuenta bancaria.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     * <li>Instancia un nuevo formulario de tipo
     * {@code RetiroConCuentaForm}.</li>
     * <li>Centra la nueva ventana en la pantalla del usuario.</li>
     * <li>Hace visible la interfaz de retiro.</li>
     * <li>Cierra y libera los recursos de la ventana de consultas actual
     * ({@code consultarOperacionesForm}) para optimizar el rendimiento.</li>
     * </ul>
     * </p>
     *
     * @see RetiroConCuentaForm
     * @see javax.swing.JFrame#dispose()
     */
    private void abrirRetiroConCuenta() {
        RetiroConCuentaForm pantallaRetiro = new RetiroConCuentaForm();
        pantallaRetiro.setLocationRelativeTo(null);
        pantallaRetiro.setVisible(true);
        consultarOperacionesForm.dispose();
    }

    /**
     * Gestiona la transición hacia la pantalla de transferencias bancarias.
     * <p>
     * Este método realiza las siguientes acciones:
     * <ul>
     * <li>Instancia un nuevo formulario {@code TransferenciaForm}.</li>
     * <li>Ubica la nueva ventana en el centro de la pantalla del usuario.</li>
     * <li>Muestra la interfaz de transferencias para que el usuario pueda
     * interactuar.</li>
     * <li>Cierra y destruye la instancia actual de
     * {@code consultarOperacionesForm} para liberar memoria del sistema.</li>
     * </ul>
     * </p>
     *
     * * @see TransferenciaForm
     * @see javax.swing.JFrame#dispose()
     */
    private void abrirPantallaTransferencia() {
        TransferenciaForm pantallaTransferencia = new TransferenciaForm();
        pantallaTransferencia.setLocationRelativeTo(null);
        pantallaTransferencia.setVisible(true);
        consultarOperacionesForm.dispose();
    }

}
