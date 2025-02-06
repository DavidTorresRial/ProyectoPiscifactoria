package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DAO para la tabla Pedido, que implementa el sistema de pedidos.
 */
public class DAOPedidos {

    private Random random = new Random();

    /**
     * Genera un nuevo pedido de forma automática.
     * Se selecciona un cliente y un pez de forma aleatoria, y se asigna
     * una cantidad aleatoria entre 10 y 50. La referencia se genera de forma única.
     */
    public void generarPedidoAutomatico() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = Conexion.getConnection();

            // Selecciona un cliente al azar
            Integer idCliente = getRandomId(conn, "Cliente");
            if (idCliente == null) {
                System.out.println("No hay clientes registrados.");
                return;
            }

            // Selecciona un pez al azar
            Integer idPez = getRandomId(conn, "Pez");
            if (idPez == null) {
                System.out.println("No hay peces registrados.");
                return;
            }

            // Cantidad aleatoria entre 10 y 50
            int cantidad = 10 + random.nextInt(41);

            // Genera una referencia única, por ejemplo utilizando la hora actual y un número aleatorio
            String numeroReferencia = "PED" + System.currentTimeMillis() + random.nextInt(1000);

            String sql = "INSERT INTO Pedido (numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada) VALUES (?, ?, ?, ?, 0)";
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, numeroReferencia);
            pstm.setInt(2, idCliente);
            pstm.setInt(3, idPez);
            pstm.setInt(4, cantidad);
            pstm.executeUpdate();
            System.out.println("Pedido generado: " + numeroReferencia);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstm != null) pstm.close(); } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    /**
     * Lista los pedidos pendientes (no completados) en el formato:
     * [ref] Nombre cliente: nombre pez enviado/solicitado (X%)
     * Ordenados por el nombre del pez.
     *
     * @return una lista de cadenas con la información de los pedidos pendientes.
     */
    public List<String> listarPedidosPendientes() {
        List<String> pedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        // Se utiliza JOIN para obtener datos de Cliente y Pez
        String sql = "SELECT p.numero_referencia, c.nombre AS cliente, " +
                     "pe.nombre AS pez, p.cantidad, p.cantidad_enviada " +
                     "FROM Pedido p " +
                     "JOIN Cliente c ON p.id_cliente = c.id " +
                     "JOIN Pez pe ON p.id_pez = pe.id " +
                     "WHERE p.cantidad_enviada < p.cantidad " +
                     "ORDER BY pe.nombre";
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement(sql);
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
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstm != null) pstm.close(); } catch (SQLException e) {}
            Conexion.closeConnection();
        }
        return pedidos;
    }

    /**
     * Envía un pedido seleccionado, simulando el envío de peces maduros.
     * Se envían tantos peces como se puedan, sin exceder la cantidad pendiente.
     *
     * @param numeroReferencia la referencia del pedido a enviar.
     * @param cantidadDisponible la cantidad de peces disponibles en el tanque.
     * @return true si el pedido se completó con este envío, false en caso contrario.
     */
    public boolean enviarPedido(String numeroReferencia, int cantidadDisponible) {
        Connection conn = null;
        PreparedStatement pstmSelect = null;
        PreparedStatement pstmUpdate = null;
        ResultSet rs = null;
        try {
            conn = Conexion.getConnection();
            // Selecciona el pedido
            String selectSql = "SELECT id, cantidad, cantidad_enviada FROM Pedido WHERE numero_referencia = ?";
            pstmSelect = conn.prepareStatement(selectSql);
            pstmSelect.setString(1, numeroReferencia);
            rs = pstmSelect.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int cantidad = rs.getInt("cantidad");
                int enviada = rs.getInt("cantidad_enviada");
                int pendiente = cantidad - enviada;
                // Calcula la cantidad a enviar: lo que se pueda enviar sin exceder la cantidad pendiente
                int enviar = Math.min(pendiente, cantidadDisponible);
                int nuevaCantidadEnviada = enviada + enviar;

                String updateSql = "UPDATE Pedido SET cantidad_enviada = ? WHERE id = ?";
                pstmUpdate = conn.prepareStatement(updateSql);
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
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstmSelect != null) pstmSelect.close(); } catch (SQLException e) {}
            try { if (pstmUpdate != null) pstmUpdate.close(); } catch (SQLException e) {}
            Conexion.closeConnection();
        }
        return false;
    }

    /**
     * Borra todos los pedidos de la tabla Pedido.
     * Esta opción se utiliza para pruebas.
     */
    public void borrarPedidos() {
        Connection conn = null;
        PreparedStatement pstm = null;
        String sql = "DELETE FROM Pedido";
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement(sql);
            int filas = pstm.executeUpdate();
            System.out.println("Se han borrado " + filas + " pedidos de la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (pstm != null) pstm.close(); } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    /**
     * Lista los pedidos completados (aquellos en los que la cantidad enviada es igual o superior a la solicitada)
     * en el formato: [ref] Nombre cliente: nombre pez enviado/solicitado (X%)
     * Ordenados por el id (orden de inserción).
     *
     * @return una lista de cadenas con la información de los pedidos completados.
     */
    public List<String> listarPedidosCompletados() {
        List<String> pedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT p.id, p.numero_referencia, c.nombre AS cliente, " +
                     "pe.nombre AS pez, p.cantidad, p.cantidad_enviada " +
                     "FROM Pedido p " +
                     "JOIN Cliente c ON p.id_cliente = c.id " +
                     "JOIN Pez pe ON p.id_pez = pe.id " +
                     "WHERE p.cantidad_enviada >= p.cantidad " +
                     "ORDER BY p.id";
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement(sql);
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
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstm != null) pstm.close(); } catch (SQLException e) {}
            Conexion.closeConnection();
        }
        return pedidos;
    }

    /**
     * Método auxiliar para obtener un id aleatorio de una tabla dada (Cliente o Pez).
     *
     * @param conn la conexión activa.
     * @param tabla el nombre de la tabla ("Cliente" o "Pez").
     * @return un id aleatorio o null si no se encontró ninguno.
     * @throws SQLException
     */
    private Integer getRandomId(Connection conn, String tabla) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            // Se utiliza ORDER BY RAND() para obtener un registro aleatorio.
            String sql = "SELECT id FROM " + tabla + " ORDER BY RAND() LIMIT 1";
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (pstm != null) pstm.close(); } catch (SQLException e) {}
        }
        return null;
    }
}