package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import commons.Simulador;

/** Administra la conexión a una base de datos MySQL. */
public class Conexion {

    /** Usuario de la base de datos. */
    private static final String USER = "proyecto-piscifactoria"; 

    /** Contraseña del usuario de la base de datos. */
    private static final String PASSWORD = "p80094Zn\"6#CLHo9£=#T";

    /** Dirección del servidor de la base de datos. */
    private static final String SERVER = "92.58.129.163";

    /** Puerto del servidor de la base de datos. */
    private static final String PORT = "3344";

    /** Nombre de la base de datos. */
    private static final String DATABASE = "piscifactoria";

    /** Objeto de conexión a la base de datos. */
    private static Connection connection;

    /**
     * Obtiene una conexión a la base de datos.
     * @return Objeto Connection.
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + SERVER + ":" + PORT + "/" + DATABASE + "?rewriteBatchedStatements=true", USER, PASSWORD);
            } catch (SQLException e) {
                Simulador.instance.registro.registroLogError("Error al conectar: " + e.getMessage());
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
            } catch (SQLException e) {
                Simulador.instance.registro.registroLogError("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}