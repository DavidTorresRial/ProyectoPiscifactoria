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

    private static final String QUERY_AGREGAR_CLIENTES = 
            "INSERT INTO Cliente (nombre, nif, telefono) " +
            "SELECT ?, ?, ? " +
            "WHERE NOT EXISTS (SELECT 1 FROM Cliente WHERE nif = ?)";
    
    private static final String QUERY_AGREGAR_PEZ = 
            "INSERT INTO Pez (nombre, nombre_cientifico) " +
            "SELECT ?, ? " +
            "WHERE NOT EXISTS (SELECT 1 FROM Pez WHERE nombre = ?)";
    
    private Connection connection;
    private PreparedStatement agregarClientes;
    private PreparedStatement agregarPeces;
    
    public GeneradorBD() {
        try {
            connection = Conexion.getConnection();
            agregarClientes = connection.prepareStatement(QUERY_AGREGAR_CLIENTES);
            agregarPeces = connection.prepareStatement(QUERY_AGREGAR_PEZ);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Crea la tabla Cliente si no existe.
     */
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
            System.out.println("Tabla Cliente creada o ya existe");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (stm != null) { 
                    stm.close(); 
                }
            } catch (SQLException e) {

            }
        }
    }
    
    /**
     * Crea la tabla Pez si no existe.
     */
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
            System.out.println("Tabla Pez creada o ya existe");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (stm != null) {
                    stm.close(); 
                }
            } catch (SQLException e) {
                // Manejo del error al cerrar el Statement (opcional)
            }
        }
    }
    
    /**
     * Crea la tabla Pedido si no existe.
     * Se utiliza el campo numero_referencia como clave primaria.
     */
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
            System.out.println("Tabla Pedido creada o ya existe");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (stm != null) { 
                    stm.close(); 
                }
            } catch (SQLException e) {

            }
        }
    }
    
    /**
     * Agrega clientes a la base de datos verificando si ya existen.
     */
    public void agregarClientes() {
        try {
            String[] nombres = { "Juan Pérez", "María García", "Carlos López", "Ana Fernández", "Pedro Sánchez",
                    "Lucía Martínez", "José Ramírez", "Carmen Gómez", "David Herrera", "Laura Díaz" };
            String[] nifs = { "12345678A", "23456789B", "34567890C", "45678901D", "56789012E", "67890123F",
                    "78901234G", "89012345H", "90123456J", "01234567K" };
            String[] telefonos = { "600123456", "611234567", "622345678", "633456789", "644567890", "655678901",
                    "666789012", "677890123", "688901234", "699012345" };
            
            for (int i = 0; i < nombres.length; i++) {
                agregarClientes.setString(1, nombres[i]);
                agregarClientes.setString(2, nifs[i]);
                agregarClientes.setString(3, telefonos[i]);
                agregarClientes.setString(4, nifs[i]);
                agregarClientes.addBatch();
            }
            agregarClientes.executeBatch();
            System.out.println("Clientes insertados correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (agregarClientes != null) {
                try {
                    agregarClientes.close();
                } catch (SQLException e) {
                    Simulador.registro.registroLogError("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Agrega peces a la base de datos verificando si ya existen.
     */
    public void agregarPeces() {
        try {
            for (String pez : Simulador.pecesImplementados) {
                agregarPeces.setString(1, pez);
                agregarPeces.setString(2, AlmacenPropiedades.getPropByName(pez).getCientifico());
                agregarPeces.setString(3, pez);
                agregarPeces.addBatch();
            }
            agregarPeces.executeBatch();
            System.out.println("Peces registrados correctamente en la tabla Pez.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (agregarPeces != null) {
                try {
                    agregarPeces.close();
                } catch (SQLException e) {
                    Simulador.registro.registroLogError("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Cierra la conexión y todos los PreparedStatement abiertos.
     */
    public void close() {
        try {
            if (agregarClientes != null) agregarClientes.close();
            if (agregarPeces != null) agregarPeces.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Método que crea todas las tablas y agrega los datos iniciales.
     */
    public void crearTablas() {
        crearTablaCliente();
        crearTablaPez();
        crearTablaPedido();
        agregarClientes();
        agregarPeces();
    }
}
