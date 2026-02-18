
package org.itson.proyecto01.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author joset
 * 
 */

//TODO
public class ConexionBD {
    private static final String CADENA_CONEXION = "jdbc:mysql://localhost:3306/BancoDB";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "admin1234";
    
    public static Connection crearConexion() throws SQLException{
        Connection conexion = DriverManager.getConnection(
                ConexionBD.CADENA_CONEXION,
                ConexionBD.USUARIO,
                ConexionBD.CONTRASEÑA
        );
        return conexion;
    }
}