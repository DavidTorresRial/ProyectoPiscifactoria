package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import commons.Simulador;

/** Administra la conexión a una base de datos MySQL. */
public class MySQLConnection {

    /** Usuario de la base de datos. */
    private static final String USER = " "; 

    /** Contraseña del usuario de la base de datos. */
    private static final String PASSWORD = " ";

    /** Dirección del servidor de la base de datos. */
    private static final String SERVER = " .iescotarelo.es";

    /** Puerto del servidor de la base de datos. */
    private static final String PORT = " ";

    /** Nombre de la base de datos. */
    private static final String DATABASE = " ";

    /** Objeto de conexión a la base de datos. */
    private static Connection connection;

    /**
     * Obtiene una conexión a la base de datos.
     * @return Objeto Connection.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + SERVER + ":" + PORT + "/" + DATABASE, USER, PASSWORD);
                // System.out.println("Conexión exitosa.");
            } catch (SQLException e) {
                e.printStackTrace();
                Simulador.registro.registroLogError("Error al conectar: " + e.getMessage());
            }
        }
        return connection;
    }

    /** Cierra la conexión a la base de datos. */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                // System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                Simulador.registro.registroLogError("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    /*


    public static void main(String[] args) {
    connection = MySQLConnection.getConnection(); // TODO Pruebas
    MySQLConnection.closeConnection();
    }

    
    */
}
