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
 *
 * @author elgps
 */
public class DomiciliosDAO implements IDomiciliosDAO {

    private static final Logger LOGGER = Logger.getLogger(DomiciliosDAO.class.getName());
    
    @Override
    public Domicilio registrarDomicilio(NuevoDomicilioDTO nuevoDomicilio) throws PersistenciaException {
        try{
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
            
            if(keys.next()){
                int idGenerado = keys.getInt(1);
                return new Domicilio(
                        idGenerado,
                        nuevoDomicilio.getCalle(),
                        nuevoDomicilio.getNumero(),
                        nuevoDomicilio.getColonia(),
                        nuevoDomicilio.getCiudad(),
                        nuevoDomicilio.getEstado(),
                        nuevoDomicilio.getCodigoPostal());
            }else{
                conexion.close(); 
                return null;
            }
        }catch(SQLException ex){
            LOGGER.severe(ex.getMessage());
            throw new PersistenciaException("No se pudo registrar el domicilio", null);
        }
    }
    
}
