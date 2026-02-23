/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.logging.Logger;
import org.itson.proyecto01.dtos.NuevoClienteDTO;
import org.itson.proyecto01.entidades.Cliente;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Jesus Omar
 */
public class ClientesDAO implements IClientesDAO {

    private static final Logger LOGGER = Logger.getLogger(ClientesDAO.class.getName());

    @Override
    public Cliente crearCliente(Integer idDomicilio, NuevoClienteDTO nuevoCliente, Integer idDomicilio1) throws PersistenciaException {

        try {
            String codigoSQL = """
                   insert into clientes(nombres, apellido_paterno, apellido_materno, fecha_nacimiento, contrasena, fecha_registro, edad, id_domicilio)
                   values(?,?,?,?,?,?,?,?);
                   """;
            Connection conexion = ConexionBD.crearConexion();

            PreparedStatement comando = conexion.prepareStatement(codigoSQL, PreparedStatement.RETURN_GENERATED_KEYS);

            LocalDateTime fechaRegistro = LocalDateTime.now(); 

            LocalDate fechaActual = LocalDate.now(); 
            Integer edadCliente = Period.between(nuevoCliente.getFechaNacimiento(), fechaActual).getYears(); 
            
            //HASH
            String passwordPlano = nuevoCliente.getContrasenia();
            String hashed = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
            
            comando.setString(1, nuevoCliente.getNombres()); 
            comando.setString(2, nuevoCliente.getApellidoP()); 
            comando.setString(3, nuevoCliente.getApelidoM()); 
            comando.setDate(4, Date.valueOf(nuevoCliente.getFechaNacimiento())); 
            comando.setString(5, hashed); 
            comando.setTimestamp(6, Timestamp.valueOf(fechaRegistro)); 
            comando.setInt(7, edadCliente); 
            comando.setInt(8, idDomicilio);

            comando.executeUpdate();

            ResultSet rs = comando.getGeneratedKeys();
            Integer idCliente = null;
            if (rs.next()) {
                idCliente = rs.getInt(1);
            }

            conexion.close();

            return new Cliente(
                    idCliente, 
                    nuevoCliente.getNombres(),
                    nuevoCliente.getApellidoP(),
                    nuevoCliente.getApelidoM(),
                    nuevoCliente.getFechaNacimiento(),
                    nuevoCliente.getContrasenia(),
                    LocalDateTime.now(),
                    edadCliente,
                    idDomicilio
            );

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo registrar al cliente", null);
        }

    }

    @Override
    public Cliente obtenerClientePorId(Integer idCliente) throws PersistenciaException {
        try {
            String codigoSQL = """
                           select id_cliente, nombres, apellido_paterno, apellido_materno, fecha_nacimiento, contrasena, fecha_registro, edad, id_domicilio 
                           from clientes
                           where id_cliente = ?
                           """;
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            comando.setInt(1, idCliente);
            ResultSet resultado = comando.executeQuery();
            if (resultado.next()) {
                Integer id = resultado.getInt("id_cliente");
                String nombres = resultado.getString("nombres");
                String apellidoP = resultado.getString("apellido_paterno");
                String apellidoM = resultado.getString("apellido_materno");
                LocalDate fechaNacimiento = resultado.getDate("fecha_nacimiento").toLocalDate();
                String contrasenia = resultado.getString("contrasena");
                LocalDateTime fechaRegistro = resultado.getTimestamp("fecha_registro").toLocalDateTime();
                Integer edad = resultado.getInt("edad");
                Integer idDomicilio = resultado.getInt("id_domicilio");

                Cliente cliente = new Cliente(id, nombres, apellidoP, apellidoM,
                        fechaNacimiento, contrasenia, fechaRegistro, edad, idDomicilio);

                conexion.close();
                return cliente;
            } else {
                return null;
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se encontro al cliente", null);
        }
    }

    @Override
    public int verificarCredenciales(String nombreCompleto) throws PersistenciaException {
        try {

            String codigoSQL = """
                    select id_cliente
                    from clientes
                    where concat(nombres, ' ', apellido_paterno, ' ', apellido_materno) = ?
                    """;

            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);

            comando.setString(1, nombreCompleto);

            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
                int id = resultado.getInt("id_cliente");
                return id;

            }else{
                return -1;
            }
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se encontro al cliente, contraseña o nombre incorrectos", null);
        }
    }
    @Override
    public String obtenerHashPorNombreCompleto(String nombreCompleto)throws PersistenciaException {

        try {
            String sql = """
                select contrasena
                from clientes
                where concat(nombres, ' ', apellido_paterno, ' ', apellido_materno) = ?
            """;

            Connection con = ConexionBD.crearConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreCompleto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("contrasena");
            }
            return null;

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al obtener contraseña", ex);
        } catch (IllegalArgumentException ex) {
            throw new PersistenciaException("Error al obtener contraseña", ex);
        }
    }
    @Override
    public Cliente actualizarCliente( Integer idCliente,NuevoClienteDTO clienteDTO, int idDomicilio) throws PersistenciaException {
        try {
            String comandoSQL = """
                UPDATE clientes
                SET nombres = ?, apellido_paterno = ?, apellido_materno = ?, fecha_nacimiento = ?,fecha_registro = ?,edad = ?, id_domicilio = ?
                WHERE id_cliente = ?;
            """;
            
            //Haseho de contraseña
            String passwordPlano = clienteDTO.getContrasenia();
            String hashed = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
            
            //Calculo de edad
            LocalDate fechaActual = LocalDate.now(); 
            Integer edadCliente = Period.between(clienteDTO.getFechaNacimiento(), fechaActual).getYears();
            
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(comandoSQL);
            comando.setString(1, clienteDTO.getNombres());
            comando.setString(2, clienteDTO.getApellidoP());
            comando.setString(3, clienteDTO.getApelidoM());
            comando.setDate(4, java.sql.Date.valueOf(clienteDTO.getFechaNacimiento()));
            comando.setTimestamp(5, java.sql.Timestamp.valueOf(clienteDTO.getFechaRegistro()));
            comando.setInt(6, edadCliente);
            comando.setInt(7, idDomicilio);
            comando.setInt(8, idCliente);

            int filasActualizadas = comando.executeUpdate();
            conexion.close();

            if (filasActualizadas > 0) {
                // Retornamos el cliente actualizado
                return new Cliente(
                        idCliente,
                        clienteDTO.getNombres(),
                        clienteDTO.getApellidoP(),
                        clienteDTO.getApelidoM(),
                        clienteDTO.getFechaNacimiento(),
                        hashed, 
                        clienteDTO.getFechaRegistro(),
                        clienteDTO.getEdad(),
                        idDomicilio
                );
            } else {
                return null;
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo actualizar el cliente", ex);
        }
    }
}
