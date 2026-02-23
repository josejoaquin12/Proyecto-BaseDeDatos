/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.negocio;

import java.time.LocalDateTime;
import org.itson.proyecto01.entidades.Cuenta;
import org.itson.proyecto01.entidades.Retiro;
import org.itson.proyecto01.persistencia.CuentasDAO;
import org.itson.proyecto01.persistencia.ICuentasDAO;
import org.itson.proyecto01.persistencia.RetiroConCuentaDAO;
import org.itson.proyecto01.persistencia.PersistenciaException;
import org.itson.proyecto01.persistencia.IRetiroConCuentaDAO;

/**
 * <p>
 * Objeto de negocio (BO) para la funcionalidad de <b>retiro sin cuenta</b>.
 * </p>
 *
 * <p>
 * Este BO se encarga de:
 * </p>
 * <ul>
 *   <li>Validar reglas de negocio antes de generar un retiro (cuenta válida, monto válido, saldo suficiente).</li>
 *   <li>Generar folio y contraseña numéricos para el retiro.</li>
 *   <li>Delegar el registro del retiro a la capa de persistencia mediante {@link IRetiroConCuentaDAO}.</li>
 *   <li>Validar un retiro por folio y contraseña (existencia y expiración).</li>
 *   <li>Cobrar un retiro validando su estado actual.</li>
 * </ul>
 *
 * <p>
 * Nota: Esta clase crea internamente sus dependencias (<code>CuentasDAO</code> y
 * <code>RetiroConCuentaDAO</code>) en el constructor sin parámetros.
 * </p>
 *
 * @author joset
 */
public class RetiroBO implements IRetiroBO {

    /**
     * DAO de cuentas utilizado para consultar información de la cuenta origen.
     */
    private ICuentasDAO cuentaDAO;

    /**
     * DAO encargado de registrar/buscar/cobrar retiros sin cuenta asociados a una cuenta.
     */
    private IRetiroConCuentaDAO RetiroConCuentaDAO;

    /**
     * <p>
     * Construye el BO e inicializa sus dependencias:
     * </p>
     * <ul>
     *   <li><code>cuentaDAO = new CuentasDAO()</code></li>
     *   <li><code>RetiroConCuentaDAO = new RetiroConCuentaDAO(cuentaDAO)</code></li>
     * </ul>
     */
    public RetiroBO() {
        cuentaDAO = new CuentasDAO();
        RetiroConCuentaDAO = new RetiroConCuentaDAO(cuentaDAO);
    }

    /**
     * <p>
     * Genera un código numérico aleatorio con la longitud indicada.
     * </p>
     *
     * <p>
     * Se utiliza para crear:
     * </p>
     * <ul>
     *   <li>Contraseña del retiro (por ejemplo, 8 dígitos).</li>
     *   <li>Folio del retiro (por ejemplo, 18 dígitos).</li>
     * </ul>
     *
     * @param longitud cantidad de dígitos del código.
     * @return cadena numérica aleatoria de la longitud solicitada.
     */
    private String generarCodigoNumerico(int longitud) {

        StringBuilder codigo = new StringBuilder();

        for (int i = 0; i < longitud; i++) {
            int digito = (int) (Math.random() * 10);
            codigo.append(digito);
        }

        return codigo.toString();
    }

    /**
     * <p>
     * Genera y registra un retiro sin cuenta a partir de una cuenta origen y un monto.
     * </p>
     *
     * <p>
     * Validaciones de negocio:
     * </p>
     * <ul>
     *   <li>La cuenta no debe ser <code>null</code>.</li>
     *   <li>El monto debe ser mayor a cero.</li>
     *   <li>La cuenta debe tener saldo suficiente.</li>
     * </ul>
     *
     * <p>
     * Flujo:
     * </p>
     * <ul>
     *   <li>Genera contraseña de 8 dígitos y folio de 18 dígitos.</li>
     *   <li>Crea un objeto {@link Retiro} con estado inicial <code>PENDIENTE</code>.</li>
     *   <li>Registra el retiro mediante {@link IRetiroConCuentaDAO#realizarRetiro(Retiro)}.</li>
     * </ul>
     *
     * @param cuenta cuenta origen del retiro.
     * @param monto monto a retirar.
     * @return {@link Retiro} generado y registrado.
     * @throws NegocioException si alguna validación falla o si ocurre un error en persistencia.
     */
    @Override
    public Retiro generarRetiro(Cuenta cuenta, double monto) throws NegocioException {

        if (cuenta == null) {
            throw new NegocioException(" :Cuenta no válida", null);
        }

        if (monto <= 0) {
            throw new NegocioException(" :Monto inválido", null);
        }

        if (cuenta.getSaldo() < monto) {
            throw new NegocioException(" :Saldo insuficiente", null);
        }

        try {
            String numeroCuenta = cuenta.getNumeroCuenta();
            LocalDateTime fechaHora = LocalDateTime.now();
            String contrasena = generarCodigoNumerico(8);
            String folio = generarCodigoNumerico(18);

            Retiro retiro = new Retiro(numeroCuenta, monto, contrasena, fechaHora, "RETIRO_SIN_CUENTA", folio, "PENDIENTE");

            RetiroConCuentaDAO.realizarRetiro(retiro);

            return retiro;

        } catch (PersistenciaException e) {
            throw new NegocioException("Error al generar retiro: " + e.getMessage(), e);
        }
    }

    /**
     * <p>
     * Valida y obtiene un retiro a partir de su folio y contraseña.
     * </p>
     *
     * <p>
     * Validaciones:
     * </p>
     * <ul>
     *   <li><code>folio</code> y <code>contrasenia</code> no deben ser <code>null</code> ni vacíos.</li>
     *   <li>Debe existir un retiro con ese folio/contraseña.</li>
     *   <li>El retiro no debe estar expirado.</li>
     * </ul>
     *
     * @param folio folio del retiro.
     * @param contrasenia contraseña del retiro.
     * @return {@link Retiro} encontrado y válido (no expirado).
     * @throws NegocioException si el retiro no existe, las credenciales son inválidas o está expirado.
     */
    @Override
    public Retiro compararRetiro(String folio, String contrasenia) throws NegocioException {

        if (folio == null || contrasenia == null || folio.isEmpty() || contrasenia.isEmpty()) {
            throw new NegocioException(" :Folio o contraseña vacíos", null);
        }

        try {
            Retiro retiro = RetiroConCuentaDAO.buscarPorFolioYContrasena(folio, contrasenia);

            if (retiro == null) {
                throw new NegocioException(" :Folio o contraseña incorrectos", null);
            }
            if (retiro.getFechaExpiracion().isBefore(LocalDateTime.now())) {
                throw new NegocioException(" :El retiro ya expiró", null);
            }
            return retiro;

        } catch (PersistenciaException ex) {
            throw new NegocioException(" :Error al validar retiro", ex);
        }
    }

    /**
     * <p>
     * Cobra un retiro validando previamente su estado.
     * </p>
     *
     * <p>
     * Reglas según el estado:
     * </p>
     * <ul>
     *   <li>Si el estado es <code>null</code>, no se permite cobrar.</li>
     *   <li>Si el estado es <code>COBRADO</code>, no se permite cobrar nuevamente.</li>
     *   <li>Si el estado es <code>NO_COBRADO</code>, se considera expirado y no se cobra.</li>
     *   <li>En otros casos, se delega el cobro a {@link IRetiroConCuentaDAO#cobrarRetiro(Retiro)}.</li>
     * </ul>
     *
     * @param retiro retiro a cobrar.
     * @throws NegocioException si el estado no permite el cobro o si ocurre un error en persistencia.
     */
    @Override
    public void cobrarRetiro(Retiro retiro) throws NegocioException {

        try {
            if (retiro.getEstado() == null) {
                throw new NegocioException(" :Estado del retiro desconocido", null);

            } else if ("COBRADO".equals(retiro.getEstado())) {
                throw new NegocioException(" :El retiro ya fue cobrado", null);

            } else if ("NO_COBRADO".equals(retiro.getEstado())) {
                throw new NegocioException(" :El retiro ha expirado y no se puede cobrar", null);

            } else {
                RetiroConCuentaDAO.cobrarRetiro(retiro);
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException(" :Error al cobrar retiro: " + ex.getMessage(), ex);

        }
    }
}