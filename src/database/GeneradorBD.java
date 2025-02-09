package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import commons.Simulador;
import propiedades.AlmacenPropiedades;

/**
 * Clase que se encarga de crear las tablas de la base de datos.
 * Representa un sistema de pedidos de peces.
 */
public class GeneradorBD {

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

    /**
     * Agrega clientes a la base de datos verificando si ya existen.
     */
    public static void agregarClientes() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement("INSERT INTO Cliente (nombre, nif, telefono) SELECT ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM Cliente WHERE nif = ?)");
            
            String[] nombres = { "Juan Pérez", "María García", "Carlos López", "Ana Fernández", "Pedro Sánchez",
                    "Lucía Martínez", "José Ramírez", "Carmen Gómez", "David Herrera", "Laura Díaz" };
            
            String[] nifs = { "12345678A", "23456789B", "34567890C", "45678901D", "56789012E", "67890123F",
                    "78901234G", "89012345H", "90123456J", "01234567K" };
            
            String[] telefonos = { "600123456", "611234567", "622345678", "633456789", "644567890", "655678901",
                    "666789012", "677890123", "688901234", "699012345" };

            for (int i = 0; i < nombres.length; i++) {
                pstm.setString(1, nombres[i]);
                pstm.setString(2, nifs[i]);
                pstm.setString(3, telefonos[i]);
                pstm.setString(4, nifs[i]);
                pstm.addBatch();
            }
            pstm.executeBatch();
            System.out.println("Clientes insertados correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    Simulador.registro.registroLogError("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Agrega peces a la base de datos verificando si ya existen.
     */
    public static void agregarPeces() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement("INSERT INTO Pez (nombre, nombre_cientifico) SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM Pez WHERE nombre = ?)");
            
            for (String pez : Simulador.pecesImplementados) {
                pstm.setString(1, pez);
                pstm.setString(2, AlmacenPropiedades.getPropByName(pez).getCientifico());
                pstm.setString(3, pez);
                pstm.addBatch();
            }
            pstm.executeBatch();
            System.out.println("Peces registrados correctamente en la tabla Pez.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    Simulador.registro.registroLogError("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
        }
    }
}
