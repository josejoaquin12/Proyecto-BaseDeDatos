/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.proyecto01.persistencia;

import com.mysql.cj.protocol.Resultset;
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

/**
 *
 * @author Jesus Omar
 */
public class ClientesDAO implements IClientesDAO {

    private static final Logger LOGGER = Logger.getLogger(ClientesDAO.class.getName());

    @Override
    public Cliente crearCliente(NuevoClienteDTO nuevoCliente, Integer idDomicilio) throws PersistenciaException {

        try {
            String codigoSQL = """
                               insert into clientes(nombres, apellido_paterno, apellido_materno, fecha_nacimiento, contrasena, fecha_registro, edad, id_domicilio)
                               values(?,?,?,?,?,?,?,?);
                               """;
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            // Fecha en la que se registro el cliente
            LocalDateTime fechaRegistro = LocalDateTime.now();
            // Calculo de la edad del cliente
            LocalDate fechaActual = LocalDate.now();
            Integer edadCliente = Period.between(nuevoCliente.getFechaNacimiento(), fechaActual).getYears();
            // Agregar los datos del cliente
            comando.setString(1, nuevoCliente.getNombres());
            comando.setString(2, nuevoCliente.getApellidoP());
            comando.setString(3, nuevoCliente.getApelidoM());
            comando.setDate(4, Date.valueOf(nuevoCliente.getFechaNacimiento()));
            comando.setString(5, nuevoCliente.getContrasenia());
            comando.setTimestamp(6, Timestamp.valueOf(fechaRegistro));
            comando.setInt(7, edadCliente);
            comando.setInt(8, idDomicilio);

            boolean resultado = comando.execute();

            LOGGER.fine("Se ha registrado correctamente");
            conexion.close();

            return new Cliente(
                    null,
                    nuevoCliente.getNombres(),
                    nuevoCliente.getApellidoP(),
                    nuevoCliente.getApelidoM(),
                    nuevoCliente.getFechaNacimiento(),
                    nuevoCliente.getContrasenia(),
                    fechaRegistro,
                    edadCliente,
                    idDomicilio);

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
            }else{
                return null;
            }

        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se encontro al cliente", null);
        }
    }

}
