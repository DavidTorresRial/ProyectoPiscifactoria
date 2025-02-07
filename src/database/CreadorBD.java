package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase que se encarga de crear las tablas de la base de datos.
 * Representa un sistema de pedidos de peces.
 */
public class CreadorBD {

    /**
     * Crea la tabla Cliente si no existe.
     */
    public static void crearTablaCliente() {
        Connection conn = null;
        Statement stm = null;
        try {
            conn = Conexion.getConnection();
            String query = "CREATE TABLE IF NOT EXISTS Cliente (" +
                    "id INT AUTO_INCREMENT," +
                    "nombre VARCHAR(100) NOT NULL," +
                    "nif VARCHAR(20) UNIQUE NOT NULL," +
                    "telefono VARCHAR(15) NOT NULL," +
                    "PRIMARY KEY(id)" +
                    ")";
            stm = conn.createStatement();
            stm.executeUpdate(query);
            System.out.println("Tabla Cliente creada o ya existe");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stm != null) { stm.close(); } } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    /**
     * Crea la tabla Pez si no existe.
     */
    public static void crearTablaPez() {
        Connection conn = null;
        Statement stm = null;
        try {
            conn = Conexion.getConnection();
            String query = "CREATE TABLE IF NOT EXISTS Pez (" +
                    "id INT AUTO_INCREMENT," +
                    "nombre VARCHAR(50) NOT NULL," +
                    "nombre_cientifico VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY(id)" +
                    ")";
            stm = conn.createStatement();
            stm.executeUpdate(query);
            System.out.println("Tabla Pez creada o ya existe");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stm != null) { stm.close(); } } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    /**
     * Crea la tabla Pedido si no existe.
     */
    public static void crearTablaPedido() {
        Connection conn = null;
        Statement stm = null;
        try {
            conn = Conexion.getConnection();
            String query = "CREATE TABLE IF NOT EXISTS Pedido (" +
                    "id INT AUTO_INCREMENT," +
                    "numero_referencia VARCHAR(50) UNIQUE NOT NULL," +
                    "id_cliente INT NOT NULL," +
                    "id_pez INT NOT NULL," +
                    "cantidad INT NOT NULL," +
                    "cantidad_enviada INT NOT NULL DEFAULT 0," +
                    "PRIMARY KEY(id)," +
                    "FOREIGN KEY(id_cliente) REFERENCES Cliente(id) ON DELETE CASCADE," +
                    "FOREIGN KEY(id_pez) REFERENCES Pez(id) ON DELETE CASCADE" +
                    ")";
            stm = conn.createStatement();
            stm.executeUpdate(query);
            System.out.println("Tabla Pedido creada o ya existe");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (stm != null) { stm.close(); } } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    public static void agregarClientes() {
        Connection conn = null;
        PreparedStatement pstm = null;

        String[] nombres = {
            "Juan Pérez", "María García", "Carlos López", "Ana Fernández", "Pedro Sánchez",
            "Lucía Martínez", "José Ramírez", "Carmen Gómez", "David Herrera", "Laura Díaz"
        };

        String[] nifs = {
            "12345678A", "23456789B", "34567890C", "45678901D", "56789012E",
            "67890123F", "78901234G", "89012345H", "90123456J", "01234567K"
        };

        String[] telefonos = {
            "600123456", "611234567", "622345678", "633456789", "644567890",
            "655678901", "666789012", "677890123", "688901234", "699012345"
        };

        try {
            
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement("INSERT INTO clientes (nombre, nif, telefono) VALUES (?, ?, ?)");

            for (int i = 0; i < nombres.length; i++) {
                pstm.setString(1, nombres[i]);
                pstm.setString(2, nifs[i]);
                pstm.setString(3, telefonos[i]);
                pstm.addBatch();
            }

            pstm.executeBatch();
            System.out.println("10 clientes insertados correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                }
            }
            Conexion.closeConnection();
        }
    }
}
