package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** DAO para la tabla Pedido.
 * Provee métodos para generar, listar, enviar y borrar pedidos.
 */
public class DAOPedidos {

    // Constantes para la cantidad de peces en el pedido
    public static final int MIN_CANTIDAD_PEDIDO = 10;
    public static final int MAX_CANTIDAD_PEDIDO = 50;

    // Query para insertar un pedido
    public static final String QUERY_INSERT_PEDIDO = 
        "INSERT INTO Pedido (numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada) VALUES (?, ?, ?, ?, 0)";

    // Query para listar los pedidos completados (cantidad_enviada >= cantidad)
    public static final String QUERY_LISTAR_PEDIDOS_COMPLETADOS =
        "SELECT p.id, p.numero_referencia, c.nombre AS cliente, pe.nombre AS pez, p.cantidad, p.cantidad_enviada " +
        "FROM Pedido p " +
        "JOIN Cliente c ON p.id_cliente = c.id " +
        "JOIN Pez pe ON p.id_pez = pe.id " +
        "WHERE p.cantidad_enviada >= p.cantidad " +
        "ORDER BY p.id";

    // Query para listar los pedidos pendientes (cantidad_enviada < cantidad)
    public static final String QUERY_LISTAR_PEDIDOS_PENDIENTES =
        "SELECT p.numero_referencia, c.nombre AS cliente, pe.nombre AS pez, p.cantidad, p.cantidad_enviada " +
        "FROM Pedido p " +
        "JOIN Cliente c ON p.id_cliente = c.id " +
        "JOIN Pez pe ON p.id_pez = pe.id " +
        "WHERE p.cantidad_enviada < p.cantidad " +
        "ORDER BY pe.nombre";

    // Query para seleccionar un pedido según su referencia
    public static final String QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA =
        "SELECT id, cantidad, cantidad_enviada FROM Pedido WHERE numero_referencia = ?";

    // Query para actualizar la cantidad enviada de un pedido
    public static final String QUERY_ACTUALIZAR_PEDIDO =
        "UPDATE Pedido SET cantidad_enviada = ? WHERE id = ?";

    // Query para borrar todos los pedidos
    public static final String QUERY_BORRAR_PEDIDOS =
        "DELETE FROM Pedido";

    // Patrón de query para obtener un ID aleatorio de una tabla.
    // Se utiliza String.format con el nombre de la tabla (por ejemplo, "Cliente" o "Pez")
    public static final String QUERY_RANDOM_ID_PATTERN =
        "SELECT id FROM %s ORDER BY RAND() LIMIT 1";

    private Random random = new Random();

    /**
     * Genera un pedido de forma automática.
     * Selecciona un cliente y un pez aleatoriamente, asigna una cantidad (entre MIN y MAX)
     * y genera una referencia única para insertar el pedido en la base de datos.
     */
    public void generarPedidoAutomatico() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = Conexion.getConnection();

            // Obtener un pez aleatorio
            Integer idPez = getRandomId(conn, "Pez");
            // Obtener un cliente aleatorio
            Integer idCliente = getRandomId(conn, "Cliente");

            if (idCliente != null || idPez != null) {
                int cantidad = MIN_CANTIDAD_PEDIDO +
                random.nextInt(MAX_CANTIDAD_PEDIDO - MIN_CANTIDAD_PEDIDO + 1);

                String numeroReferencia = "PED-" + System.currentTimeMillis();

                pstm = conn.prepareStatement(QUERY_INSERT_PEDIDO);
                pstm.setString(1, numeroReferencia);
                pstm.setInt(2, idCliente);
                pstm.setInt(3, idPez);
                pstm.setInt(4, cantidad);
                pstm.executeUpdate();
                System.out.println("Pedido generado: " + numeroReferencia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (pstm != null) pstm.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    /**
     * Lista los pedidos pendientes.
     * @return Lista de cadenas con la información de los pedidos pendientes.
     */
    public List<String> listarPedidosPendientes() {
        List<String> pedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement(QUERY_LISTAR_PEDIDOS_PENDIENTES);
            rs = pstm.executeQuery();
            while (rs.next()) {
                String ref = rs.getString("numero_referencia");
                String cliente = rs.getString("cliente");
                String pez = rs.getString("pez");
                int cantidad = rs.getInt("cantidad");
                int enviada = rs.getInt("cantidad_enviada");
                int porcentaje = (int) ((enviada * 100.0) / cantidad);
                String pedidoStr = String.format("[%s] %s: %s %d/%d (%d%%)", 
                                                  ref, cliente, pez, enviada, cantidad, porcentaje);
                pedidos.add(pedidoStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (rs != null) rs.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstm != null) pstm.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
        return pedidos;
    }

    /**
     * Envía un pedido actualizando la cantidad enviada según la cantidad disponible,
     * sin superar la cantidad solicitada.
     * @param numeroReferencia La referencia del pedido a enviar.
     * @param cantidadDisponible La cantidad de peces disponibles.
     * @return true si el pedido queda completo, false en caso contrario.
     */
    public boolean enviarPedido(String numeroReferencia, int cantidadDisponible) {
        Connection conn = null;
        PreparedStatement pstmSelect = null;
        PreparedStatement pstmUpdate = null;
        ResultSet rs = null;
        try {
            conn = Conexion.getConnection();
            pstmSelect = conn.prepareStatement(QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA);
            pstmSelect.setString(1, numeroReferencia);
            rs = pstmSelect.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int cantidad = rs.getInt("cantidad");
                int enviada = rs.getInt("cantidad_enviada");
                int pendiente = cantidad - enviada;
                int enviar = Math.min(pendiente, cantidadDisponible);
                int nuevaCantidadEnviada = enviada + enviar;

                pstmUpdate = conn.prepareStatement(QUERY_ACTUALIZAR_PEDIDO);
                pstmUpdate.setInt(1, nuevaCantidadEnviada);
                pstmUpdate.setInt(2, id);
                pstmUpdate.executeUpdate();

                System.out.println("Pedido " + numeroReferencia + " actualizado: se enviaron " + enviar +
                        " peces. Total enviados: " + nuevaCantidadEnviada + "/" + cantidad);
                return nuevaCantidadEnviada >= cantidad;
            } else {
                System.out.println("Pedido con referencia " + numeroReferencia + " no encontrado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (rs != null) rs.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstmSelect != null) pstmSelect.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstmUpdate != null) pstmUpdate.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
        return false;
    }

    /**
     * Borra todos los pedidos de la base de datos.
     */
    public void borrarPedidos() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement(QUERY_BORRAR_PEDIDOS);
            int filas = pstm.executeUpdate();
            System.out.println("\nSe han borrado " + filas + " pedidos de la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (pstm != null) pstm.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    /**
     * Lista los pedidos completados.
     * @return Lista de cadenas con la información de los pedidos completados.
     */
    public List<String> listarPedidosCompletados() {
        List<String> pedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement(QUERY_LISTAR_PEDIDOS_COMPLETADOS);
            rs = pstm.executeQuery();
            while (rs.next()) {
                String ref = rs.getString("numero_referencia");
                String cliente = rs.getString("cliente");
                String pez = rs.getString("pez");
                int cantidad = rs.getInt("cantidad");
                int enviada = rs.getInt("cantidad_enviada");
                int porcentaje = (int) ((enviada * 100.0) / cantidad);
                String pedidoStr = String.format("[%s] %s: %s %d/%d (%d%%)", 
                                                  ref, cliente, pez, enviada, cantidad, porcentaje);
                pedidos.add(pedidoStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (rs != null) rs.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstm != null) pstm.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
        return pedidos;
    }

    /**
     * Obtiene un ID aleatorio de la tabla especificada.
     * @param conn Conexión activa.
     * @param tabla Nombre de la tabla ("Cliente" o "Pez").
     * @return Un ID aleatorio o null si no se encuentra ninguno.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private Integer getRandomId(Connection conn, String tabla) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            String sql = String.format(QUERY_RANDOM_ID_PATTERN, tabla);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } finally {
            try { 
                if (rs != null) rs.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstm != null) pstm.close(); 
            } catch (SQLException e) {}
        }
        return null;
    }
}