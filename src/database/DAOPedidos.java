package database;

import database.dtos.DTOPedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DAO para la tabla Pedido.
 * Provee métodos para generar, listar, enviar y borrar pedidos.
 */
public class DAOPedidos {

    private Random random = new Random();

    // Query para insertar un pedido
    private static final String QUERY_INSERT_PEDIDO = 
        "INSERT INTO Pedido (numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada) VALUES (?, ?, ?, ?, 0)";

    private static final String QUERY_LISTAR_PEDIDOS_COMPLETADOS =
        "SELECT id, numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada FROM Pedido WHERE cantidad_enviada >= cantidad ORDER BY id";

    private static final String QUERY_LISTAR_PEDIDOS_PENDIENTES =
        "SELECT id, numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada FROM Pedido WHERE cantidad_enviada < cantidad ORDER BY id";

    private static final String QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA =
        "SELECT id, cantidad, cantidad_enviada FROM Pedido WHERE numero_referencia = ?";

    private static final String QUERY_ACTUALIZAR_PEDIDO =
        "UPDATE Pedido SET cantidad_enviada = ? WHERE id = ?";

    private static final String QUERY_BORRAR_PEDIDOS = "DELETE FROM Pedido";

    private static final String QUERY_RANDOM_ID_PATTERN = "SELECT id FROM %s ORDER BY RAND() LIMIT 1";

    /**
     * Genera un pedido automático seleccionando un cliente y un pez aleatorio.
     */
    public void generarPedidoAutomatico() {
        Connection conn = null;
        
        try {
            conn = Conexion.getConnection();
            Integer idPez = getRandomId(conn, "Pez");
            Integer idCliente = getRandomId(conn, "Cliente");

            if (idCliente != null && idPez != null) {
                int cantidad = 10 + random.nextInt(41);
                String numeroReferencia = "PED-" + System.currentTimeMillis();

                try (PreparedStatement pstm = conn.prepareStatement(QUERY_INSERT_PEDIDO)) {
                    pstm.setString(1, numeroReferencia);
                    pstm.setInt(2, idCliente);
                    pstm.setInt(3, idPez);
                    pstm.setInt(4, cantidad);
                    pstm.executeUpdate();
                    System.out.println("Pedido generado: " + numeroReferencia);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexion.closeConnection();
        }
    }

    /**
     * Lista los pedidos pendientes.
     * @return Lista de objetos DTOPedido.
     */
    public List<DTOPedido> listarPedidosPendientes() {
        List<DTOPedido> pedidos = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstm = conn.prepareStatement(QUERY_LISTAR_PEDIDOS_PENDIENTES);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                pedidos.add(new DTOPedido(
                        rs.getInt("id"),
                        rs.getString("numero_referencia"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_pez"),
                        rs.getInt("cantidad"),
                        rs.getInt("cantidad_enviada")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexion.closeConnection();
        }
        return pedidos;
    }

    /**
     * Lista los pedidos completados.
     * @return Lista de objetos DTOPedido.
     */
    public List<DTOPedido> listarPedidosCompletados() {
        List<DTOPedido> pedidos = new ArrayList<>();
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstm = conn.prepareStatement(QUERY_LISTAR_PEDIDOS_COMPLETADOS);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                pedidos.add(new DTOPedido(
                        rs.getInt("id"),
                        rs.getString("numero_referencia"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_pez"),
                        rs.getInt("cantidad"),
                        rs.getInt("cantidad_enviada")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexion.closeConnection();
        }
        return pedidos;
    }

    /**
     * Envía un pedido actualizando la cantidad enviada sin superar la cantidad solicitada.
     */
    public boolean enviarPedido(String numeroReferencia, int cantidadDisponible) {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmSelect = conn.prepareStatement(QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA)) {

            pstmSelect.setString(1, numeroReferencia);
            try (ResultSet rs = pstmSelect.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int cantidad = rs.getInt("cantidad");
                    int enviada = rs.getInt("cantidad_enviada");

                    int pendiente = cantidad - enviada;
                    int enviar = Math.min(pendiente, cantidadDisponible);
                    int nuevaCantidadEnviada = enviada + enviar;

                    try (PreparedStatement pstmUpdate = conn.prepareStatement(QUERY_ACTUALIZAR_PEDIDO)) {
                        pstmUpdate.setInt(1, nuevaCantidadEnviada);
                        pstmUpdate.setInt(2, id);
                        pstmUpdate.executeUpdate();
                    }

                    return nuevaCantidadEnviada >= cantidad;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexion.closeConnection();
        }
        return false;
    }

    /**
     * Borra todos los pedidos de la base de datos.
     */
    public void borrarPedidos() {
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstm = conn.prepareStatement(QUERY_BORRAR_PEDIDOS)) {
            
            int filas = pstm.executeUpdate();
            System.out.println("Se han borrado " + filas + " pedidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Conexion.closeConnection();
        }
    }

    /**
     * Obtiene un ID aleatorio de la tabla especificada.
     * @param conn Conexión activa.
     * @param tabla Nombre de la tabla ("Cliente" o "Pez").
     * @return Un ID aleatorio o null si no hay registros.
     */
    private Integer getRandomId(Connection conn, String tabla) throws SQLException {
        String sql = String.format(QUERY_RANDOM_ID_PATTERN, tabla);
        try (PreparedStatement pstm = conn.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id");
            }
        }notify();
        return null;
    }
}
