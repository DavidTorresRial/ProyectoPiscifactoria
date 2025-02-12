package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import commons.Simulador;
import propiedades.AlmacenPropiedades;

/** Clase que se encarga de crear las tablas de la base de datos. */
public class GeneradorBD {

    /** Consulta SQL para insertar un cliente si su NIF no existe. */
    private static final String QUERY_AGREGAR_CLIENTES = 
        "INSERT INTO Cliente (nombre, nif, telefono) " +
        "SELECT ?, ?, ? " +
        "WHERE NOT EXISTS (SELECT 1 FROM Cliente WHERE nif = ?)";

    /** Consulta SQL para insertar un pez si su nombre no existe. */
    private static final String QUERY_AGREGAR_PEZ = 
        "INSERT INTO Pez (nombre, nombre_cientifico) " +
        "SELECT ?, ? " +
        "WHERE NOT EXISTS (SELECT 1 FROM Pez WHERE nombre = ?)";

    /** Conexión a la base de datos. */
    private Connection connection = Conexion.getConnection();

    /** Crea la tabla Cliente si no existe. */
    public void crearTablaCliente() {
        Statement stm = null;
        try {
            String query = "CREATE TABLE IF NOT EXISTS Cliente (" +
                    "id INT AUTO_INCREMENT," +
                    "nombre VARCHAR(100) NOT NULL," +
                    "nif VARCHAR(20) UNIQUE NOT NULL," +
                    "telefono VARCHAR(15) NOT NULL," +
                    "PRIMARY KEY(id)" +
                    ")";
            stm = connection.createStatement();
            stm.executeUpdate(query);
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al crear la tabla Cliente: " + e.getMessage());
        } finally {
            try { 
                if (stm != null) { 
                    stm.close(); 
                }
            } catch (SQLException e) {
                Simulador.registro.registroLogError("Error al cerrar Statement en crearTablaPez: " + e.getMessage());
            }
        }
    }

    /** Crea la tabla Pez si no existe. */
    public void crearTablaPez() {
        Statement stm = null;
        try {
            String query = "CREATE TABLE IF NOT EXISTS Pez (" +
                    "id INT AUTO_INCREMENT," +
                    "nombre VARCHAR(50) NOT NULL," +
                    "nombre_cientifico VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY(id)" +
                    ")";
            stm = connection.createStatement();
            stm.executeUpdate(query);
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al crear la tabla Pez: " + e.getMessage());
        } finally {
            try { 
                if (stm != null) {
                    stm.close(); 
                }
            } catch (SQLException e) {
                Simulador.registro.registroLogError("Error al cerrar Statement en crearTablaPez: " + e.getMessage());
            }
        }
    }

    /** Crea la tabla Pedido si no existe. */
    public void crearTablaPedido() {
        Statement stm = null;
        try {
            String query = "CREATE TABLE IF NOT EXISTS Pedido (" +
                    "numero_referencia VARCHAR(50) UNIQUE NOT NULL," +
                    "id_cliente INT NOT NULL," +
                    "id_pez INT NOT NULL," +
                    "cantidad INT NOT NULL," +
                    "cantidad_enviada INT NOT NULL DEFAULT 0," +
                    "PRIMARY KEY(numero_referencia)," +
                    "FOREIGN KEY(id_cliente) REFERENCES Cliente(id) ON DELETE CASCADE," +
                    "FOREIGN KEY(id_pez) REFERENCES Pez(id) ON DELETE CASCADE" +
                    ")";
            stm = connection.createStatement();
            stm.executeUpdate(query);
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al crear la tabla Pedido: " + e.getMessage());
        } finally {
            try { 
                if (stm != null) { 
                    stm.close(); 
                }
            } catch (SQLException e) {
                Simulador.registro.registroLogError("Error al cerrar Statement en crearTablaPedido: " + e.getMessage());
            }
        }
    }
    
    /** Agrega clientes a la base de datos verificando si ya existen. */
    public void agregarClientes() {
        try (PreparedStatement pstm = connection.prepareStatement(QUERY_AGREGAR_CLIENTES)) {

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
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al agregar clientes a la base de datos: " + e.getMessage());
        }
    }
    
    /** Agrega peces a la base de datos verificando si ya existen. */
    public void agregarPeces() {
        try (PreparedStatement pstm = connection.prepareStatement(QUERY_AGREGAR_PEZ)) {
            for (String pez : Simulador.pecesImplementados) {
                pstm.setString(1, pez);
                pstm.setString(2, AlmacenPropiedades.getPropByName(pez).getCientifico());
                pstm.setString(3, pez);
                pstm.addBatch();
            }
            pstm.executeBatch();
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al agregar peces a la base de datos: " + e.getMessage());
        }
    }
    
    /** Método que crea todas las tablas y agrega los datos iniciales. */
    public void crearTablas() {
        crearTablaCliente();
        crearTablaPez();
        crearTablaPedido();
        agregarClientes();
        agregarPeces();
    }
}