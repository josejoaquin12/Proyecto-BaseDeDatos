/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import org.itson.proyecto01.dtos.NuevoDomicilioDTO;
import org.itson.proyecto01.entidades.Domicilio;

/**
 * <p>
 * Implementación del DAO para la entidad <b>Domicilio</b>. Esta clase concentra
 * las operaciones de persistencia relacionadas con la tabla
 * <code>domicilios</code>.
 * </p>
 *
 * <p>
 * Funcionalidades principales:
 * </p>
 * <ul>
 *   <li>Registrar un nuevo domicilio.</li>
 *   <li>Consultar un domicilio por su identificador.</li>
 *   <li>Actualizar los datos de un domicilio existente.</li>
 * </ul>
 *
 * <p>
 * Nota: La clase obtiene el ID del cliente de la sesión mediante
 * <code>SesionControl</code>. (Actualmente se declara como campo, aunque en este
 * DAO no se utiliza dentro de los métodos.)
 * </p>
 *
 * @author elgps
 */
public class DomiciliosDAO implements IDomiciliosDAO {

    private static final Logger LOGGER = Logger.getLogger(DomiciliosDAO.class.getName());

    /**
     * <p>
     * Registra un domicilio en la base de datos.
     * </p>
     *
     * <p>
     * Inserta un registro en la tabla <code>domicilios</code> y recupera el
     * identificador generado (<code>id_domicilio</code>) usando
     * <code>RETURN_GENERATED_KEYS</code>.
     * </p>
     *
     * @param nuevoDomicilio DTO con la información del domicilio a registrar.
     * @return un objeto {@link Domicilio} con el ID generado si el registro fue exitoso;
     *         en caso contrario retorna <code>null</code>.
     * @throws PersistenciaException si ocurre un error al insertar en base de datos.
     */
    @Override
    public Domicilio registrarDomicilio(NuevoDomicilioDTO nuevoDomicilio) throws PersistenciaException {
        try {
            String comandoSQL = """
                                insert into domicilios (calle, numero, colonia, ciudad, estado, codigo_postal)
                                values(?,?,?,?,?, ?);
                                """;
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(comandoSQL, Statement.RETURN_GENERATED_KEYS);
            comando.setString(1, nuevoDomicilio.getCalle());
            comando.setString(2, nuevoDomicilio.getNumero());
            comando.setString(3, nuevoDomicilio.getColonia());
            comando.setString(4, nuevoDomicilio.getCiudad());
            comando.setString(5, nuevoDomicilio.getEstado());
            comando.setString(6, nuevoDomicilio.getCodigoPostal());

            boolean resultado = comando.execute();

            LOGGER.fine("Se ha registrado el domicilio correctamente");
            ResultSet keys = comando.getGeneratedKeys();

            if (keys.next()) {
                int idGenerado = keys.getInt(1);
                return new Domicilio(
                        idGenerado,
                        nuevoDomicilio.getCalle(),
                        nuevoDomicilio.getNumero(),
                        nuevoDomicilio.getColonia(),
                        nuevoDomicilio.getCiudad(),
                        nuevoDomicilio.getEstado(),
                        nuevoDomicilio.getCodigoPostal());
            } else {
                conexion.close();
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo registrar el domicilio", null);
        }
    }

    /**
     * <p>
     * Obtiene un domicilio a partir de su identificador.
     * </p>
     *
     * <p>
     * Realiza una consulta a la tabla <code>domicilios</code> con
     * <code>id_domicilio = ?</code> y, si existe el registro, construye un objeto
     * {@link Domicilio}.
     * </p>
     *
     * @param idDomicilio identificador del domicilio a consultar.
     * @return el {@link Domicilio} encontrado; si no existe, retorna <code>null</code>.
     * @throws PersistenciaException si ocurre un error al consultar en base de datos.
     */
    @Override
    public Domicilio obtenerDomicilioID(Integer idDomicilio) throws PersistenciaException {
        try {
            String comandoSQL = """
                                select id_domicilio,calle, numero, colonia, ciudad, estado, codigo_postal  
                                from domicilios 
                                where id_domicilio = ?;
                                """;
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(comandoSQL);
            comando.setInt(1, idDomicilio);

            ResultSet resultados = comando.executeQuery();

            if (resultados.next()) {
                Integer id = resultados.getInt("id_domicilio");
                String calle = resultados.getString("calle");
                String numero = resultados.getString("numero");
                String colonia = resultados.getString("colonia");
                String ciudad = resultados.getString("ciudad");
                String estado = resultados.getString("ciudad");
                String codigoPostal = resultados.getString("codigo_postal");
                conexion.close();
                return new Domicilio(id, calle, numero, colonia, ciudad, estado, codigoPostal);
            } else {
                conexion.close();
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo obtener el domicilio", null);
        }
    }

    /**
     * <p>
     * Actualiza la información de un domicilio existente.
     * </p>
     *
     * <p>
     * Ejecuta un <code>UPDATE</code> sobre la tabla <code>domicilios</code> para el
     * registro identificado por <code>id_domicilio</code>.
     * </p>
     *
     * @param nuevoDomicilio DTO con los nuevos datos del domicilio.
     * @param idDomicilio identificador del domicilio a actualizar.
     * @return un {@link Domicilio} con los datos actualizados si se modificó al menos una fila;
     *         en caso contrario retorna <code>null</code>.
     * @throws PersistenciaException si ocurre un error al actualizar en base de datos.
     */
    @Override
    public Domicilio actualizarDomicilio(NuevoDomicilioDTO nuevoDomicilio, Integer idDomicilio) throws PersistenciaException {
        try {
            String comandoSQL = """
                                Update domicilios 
                                Set calle=?, numero=?, colonia=?, ciudad=?, estado=?, codigo_postal=?  

                                where id_domicilio = ?;
                                """;
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(comandoSQL);
            comando.setString(1, nuevoDomicilio.getCalle());
            comando.setString(2, nuevoDomicilio.getNumero());
            comando.setString(3, nuevoDomicilio.getColonia());
            comando.setString(4, nuevoDomicilio.getCiudad());
            comando.setString(5, nuevoDomicilio.getEstado());
            comando.setString(6, nuevoDomicilio.getCodigoPostal());
            comando.setInt(7, idDomicilio);

            int resultados = comando.executeUpdate();

            if (resultados > 0) {
                String calle = nuevoDomicilio.getCalle();
                String numero = nuevoDomicilio.getNumero();
                String colonia = nuevoDomicilio.getColonia();
                String ciudad = nuevoDomicilio.getCiudad();
                String estado = nuevoDomicilio.getEstado();
                String codigoPostal = nuevoDomicilio.getCodigoPostal();
                conexion.close();
                return new Domicilio(idDomicilio, calle, numero, colonia, ciudad, estado, codigoPostal);
            } else {
                conexion.close();
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo obtener el domicilio", null);
        }
    }

}