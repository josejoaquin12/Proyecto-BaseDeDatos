package org.itson.proyecto01.entidades;

import java.time.LocalDateTime;

/**
 * Clase que representa la entidad Retiro en el sistema.
 * <p>
 * Esta clase se utiliza principalmente para gestionar los retiros sin cuenta,
 * almacenando la información necesaria para que un usuario pueda cobrar dinero
 * mediante un folio y una contraseña temporal.
 * </p>
 * <p>
 * Incluye lógica de negocio implícita al calcular automáticamente la
 * <b>fecha de expiración</b> (10 minutos después de la creación) en su
 * constructor principal.
 * </p>
 *
 * * @author Jesus Omar
 */
public class Retiro {

    private String numeroCuentaOrigen;
    private double monto;
    private LocalDateTime fechaHora;
    private String tipoOperacion;
    private String folio;
    private String estado;
    private String contrasena;
    private LocalDateTime fechaExpiracion;

    /**
     * Constructor para inicializar un retiro con todos sus detalles.
     * <p>
     * Al utilizar este constructor, la <b>fecha de expiración</b> se establece
     * automáticamente sumando 10 minutos a la {@code fechaHora} proporcionada.
     * </p>
     *
     * * @param numeroCuentaOrigen Cuenta que emite los fondos.
     * @param monto Cantidad a retirar.
     * @param contrasena Clave de seguridad.
     * @param fechaHora Momento de creación.
     * @param tipoOperacion Etiqueta de la operación.
     * @param folio Folio único generado.
     * @param estado Estado inicial del retiro.
     */
    public Retiro(String numeroCuentaOrigen, double monto, String contrasena, LocalDateTime fechaHora, String tipoOperacion, String folio, String estado) {
        this.numeroCuentaOrigen = numeroCuentaOrigen;
        this.contrasena = contrasena;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.tipoOperacion = tipoOperacion;
        this.folio = folio;
        this.estado = estado;
        this.fechaExpiracion = this.fechaHora.plusMinutes(10);
    }

    /**
     * Constructor simplificado utilizado para procesos de validación.
     *
     * * @param contrasena Clave de seguridad a verificar.
     * @param folio Folio a buscar.
     */
    public Retiro(String contrasena, String folio) {
        this.contrasena = contrasena;
        this.folio = folio;
    }

    /**
     * Constructor por defecto.
     */
    public Retiro() {

    }

    /**
     * Obtiene el número de cuenta origen.
     *
     * @return {@code String} con la cuenta.
     */
    public String getNumeroCuentaOrigen() {
        return numeroCuentaOrigen;
    }

    /**
     * Asigna el número de cuenta origen.
     *
     * @param numeroCuentaOrigen Cuenta emisora.
     */
    public void setNumeroCuentaOrigen(String numeroCuentaOrigen) {
        this.numeroCuentaOrigen = numeroCuentaOrigen;
    }

    /**
     * Obtiene la contraseña del retiro.
     *
     * @return Clave de seguridad.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del retiro.
     *
     * @param contrasena Clave de seguridad.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el monto del retiro.
     *
     * @return Valor numérico del monto.
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Establece el monto del retiro.
     *
     * @param monto Cantidad de dinero.
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }

    /**
     * Obtiene la fecha y hora de creación.
     *
     * @return {@link LocalDateTime} de creación.
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora de creación.
     *
     * @param fechaHora Momento de creación.
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Obtiene el tipo de operación.
     *
     * @return Etiqueta del tipo de operación.
     */
    public String getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Establece el tipo de operación.
     *
     * @param tipoOperacion Etiqueta de la operación.
     */
    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    /**
     * Obtiene el folio único.
     *
     * @return El folio generado.
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Establece el folio único.
     *
     * @param folio Folio identificador.
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * Obtiene el estado del retiro.
     *
     * @return Estado (Pendiente/Cobrado/Expirado).
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del retiro.
     *
     * @param estado Nuevo estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha de expiración.
     *
     * @return {@link LocalDateTime} con el límite de tiempo.
     */
    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    /**
     * Establece la fecha de expiración.
     *
     * @param fechaExpiracion Límite de tiempo.
     */
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

}
