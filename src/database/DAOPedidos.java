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
    
    private static final String QUERY_RANDOM_CLIENTE = "SELECT id FROM Cliente ORDER BY RAND() LIMIT 1";
    private static final String QUERY_RANDOM_PEZ = "SELECT id FROM Pez ORDER BY RAND() LIMIT 1";

    // Conexión y PreparedStatements como variables de instancia
    private Connection connection;
    private PreparedStatement pstInsertPedido;
    private PreparedStatement pstListarPedidosPendientes;
    private PreparedStatement pstListarPedidosCompletados;
    private PreparedStatement pstSeleccionarPedidoPorReferencia;
    private PreparedStatement pstActualizarPedido;
    private PreparedStatement pstBorrarPedidos;
    private PreparedStatement pstRandomCliente;
    private PreparedStatement pstRandomPez;

    /**
     * Constructor que establece la conexión y prepara los statements.
     */
    public DAOPedidos() {
        try {
            connection = Conexion.getConnection();
            pstInsertPedido = connection.prepareStatement(QUERY_INSERT_PEDIDO);
            pstListarPedidosPendientes = connection.prepareStatement(QUERY_LISTAR_PEDIDOS_PENDIENTES);
            pstListarPedidosCompletados = connection.prepareStatement(QUERY_LISTAR_PEDIDOS_COMPLETADOS);
            pstSeleccionarPedidoPorReferencia = connection.prepareStatement(QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA);
            pstActualizarPedido = connection.prepareStatement(QUERY_ACTUALIZAR_PEDIDO);
            pstBorrarPedidos = connection.prepareStatement(QUERY_BORRAR_PEDIDOS);
            pstRandomCliente = connection.prepareStatement(QUERY_RANDOM_CLIENTE);
            pstRandomPez = connection.prepareStatement(QUERY_RANDOM_PEZ);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un pedido automático seleccionando un cliente y un pez aleatorio.
     */
    public void generarPedidoAutomatico() {
        try {
            Integer idPez = getRandomId("Pez");
            Integer idCliente = getRandomId("Cliente");

            if (idCliente != null && idPez != null) {
                int cantidad = 10 + random.nextInt(41);
                String numeroReferencia = "PED-" + System.currentTimeMillis();

                pstInsertPedido.clearParameters();
                pstInsertPedido.setString(1, numeroReferencia);
                pstInsertPedido.setInt(2, idCliente);
                pstInsertPedido.setInt(3, idPez);
                pstInsertPedido.setInt(4, cantidad);
                pstInsertPedido.executeUpdate();
                System.out.println("Pedido generado: " + numeroReferencia);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lista los pedidos pendientes.
     * @return Lista de objetos DTOPedido.
     */
    public List<DTOPedido> listarPedidosPendientes() {
        List<DTOPedido> pedidos = new ArrayList<>();
        try (ResultSet rs = pstListarPedidosPendientes.executeQuery()) {
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
        }
        return pedidos;
    }

    /**
     * Lista los pedidos completados.
     * @return Lista de objetos DTOPedido.
     */
    public List<DTOPedido> listarPedidosCompletados() {
        List<DTOPedido> pedidos = new ArrayList<>();
        try (ResultSet rs = pstListarPedidosCompletados.executeQuery()) {
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
        }
        return pedidos;
    }

    /**
     * Envía un pedido actualizando la cantidad enviada sin superar la cantidad solicitada.
     * @param numeroReferencia Referencia del pedido.
     * @param cantidadDisponible Cantidad disponible para enviar.
     * @return true si el pedido se completó, false en caso contrario.
     */
    public boolean enviarPedido(String numeroReferencia, int cantidadDisponible) {
        try {
            pstSeleccionarPedidoPorReferencia.clearParameters();
            pstSeleccionarPedidoPorReferencia.setString(1, numeroReferencia);
            try (ResultSet rs = pstSeleccionarPedidoPorReferencia.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int cantidad = rs.getInt("cantidad");
                    int enviada = rs.getInt("cantidad_enviada");

                    int pendiente = cantidad - enviada;
                    int enviar = Math.min(pendiente, cantidadDisponible);
                    int nuevaCantidadEnviada = enviada + enviar;

                    pstActualizarPedido.clearParameters();
                    pstActualizarPedido.setInt(1, nuevaCantidadEnviada);
                    pstActualizarPedido.setInt(2, id);
                    pstActualizarPedido.executeUpdate();

                    return nuevaCantidadEnviada >= cantidad;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Borra todos los pedidos de la base de datos.
     */
    public void borrarPedidos() {
        try {
            System.out.println("Se han borrado " + pstBorrarPedidos.executeUpdate() + " pedidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene un ID aleatorio de la tabla especificada.
     * @param tabla Nombre de la tabla ("Cliente" o "Pez").
     * @return Un ID aleatorio o null si no hay registros.
     * @throws SQLException
     */
    private Integer getRandomId(String tabla) throws SQLException {
        PreparedStatement pst;
        if ("Cliente".equalsIgnoreCase(tabla)) {
            pst = pstRandomCliente;
        } else if ("Pez".equalsIgnoreCase(tabla)) {
            pst = pstRandomPez;
        } else {
            throw new IllegalArgumentException("Tabla no soportada: " + tabla);
        }
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        return null;
    }

    /**
     * Cierra la conexión y todos los PreparedStatement abiertos.
     */
    public void close() {
        try {
            if (pstInsertPedido != null) pstInsertPedido.close();
            if (pstListarPedidosPendientes != null) pstListarPedidosPendientes.close();
            if (pstListarPedidosCompletados != null) pstListarPedidosCompletados.close();
            if (pstSeleccionarPedidoPorReferencia != null) pstSeleccionarPedidoPorReferencia.close();
            if (pstActualizarPedido != null) pstActualizarPedido.close();
            if (pstBorrarPedidos != null) pstBorrarPedidos.close();
            if (pstRandomCliente != null) pstRandomCliente.close();
            if (pstRandomPez != null) pstRandomPez.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}