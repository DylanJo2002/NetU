/**
 * El objetivo de la clase es servir de conexión a la base de datos
 */

package Modelo;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBD {
    
    public static Connection coneccion;
    static final String driver = "com.mysql.cj.jdbc.Driver";
    static final String user = "root";
    static final String password = "MySQL2021";
    static final String url = "jdbc:mysql://localhost:3306/sistemanetu?serverTimezone=UTC";

    /**
     * Permite crear el objeto Connection hacia la BD.
     */
    public static void Conectar() {

        try {
            coneccion = null;

            Class.forName(driver);

            try {
                coneccion = DriverManager.getConnection(url, user, password);

            } catch (SQLException ex) {
                System.out.println("SQL EX Ocurrió este error: " + ex.getMessage());
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("CNF Ocurrió este error: " + ex.getMessage());
        }

    }

}
