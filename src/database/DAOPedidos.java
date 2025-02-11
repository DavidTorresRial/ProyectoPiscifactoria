package database;

import database.dtos.DTOCliente;
import database.dtos.DTOPedido;
import database.dtos.DTOPez;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import commons.Simulador;

/**
 * DAO para la tabla Pedido.
 * Utiliza DTOs para gestionar la información, devolviendo y actualizando objetos
 * de transferencia en cada operación.
 */
public class DAOPedidos {

    private Random random = new Random();

    private static final String QUERY_INSERT_PEDIDO = 
    "INSERT INTO Pedido (numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada) VALUES (?, ?, ?, ?, 0)";
    private static final String QUERY_LISTAR_PEDIDOS_COMPLETADOS =
    "SELECT numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada FROM Pedido WHERE cantidad_enviada >= cantidad ORDER BY numero_referencia";
    private static final String QUERY_LISTAR_PEDIDOS_PENDIENTES =
    "SELECT numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada FROM Pedido WHERE cantidad_enviada < cantidad ORDER BY numero_referencia";
    private static final String QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA =
    "SELECT numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada FROM Pedido WHERE numero_referencia = ?";
    private static final String QUERY_ACTUALIZAR_PEDIDO =
    "UPDATE Pedido SET cantidad_enviada = ? WHERE numero_referencia = ?";
    private static final String QUERY_BORRAR_PEDIDOS = "DELETE FROM Pedido";
    
    private static final String QUERY_OBTENER_PEZ = "SELECT id, nombre, nombre_cientifico FROM Pez WHERE id = ?";

    private static final String QUERY_OBTENER_NOMBRE = "SELECT id, nombre, nif, telefono FROM Cliente WHERE id = ?";


    // --- Queries para obtener un Cliente o un Pez aleatorio ---
    private static final String QUERY_RANDOM_CLIENTE =
        "SELECT id, nombre, nif, telefono FROM Cliente ORDER BY RAND() LIMIT 1";
    private static final String QUERY_RANDOM_PEZ =
        "SELECT id, nombre, nombre_cientifico FROM Pez ORDER BY RAND() LIMIT 1";

    // --- Variables de conexión y PreparedStatement ---
    private Connection connection;
    private PreparedStatement pstInsertPedido;
    private PreparedStatement pstListarPedidosPendientes;
    private PreparedStatement pstListarPedidosCompletados;
    private PreparedStatement pstSeleccionarPedidoPorReferencia;
    private PreparedStatement pstActualizarPedido;
    private PreparedStatement pstBorrarPedidos;
    private PreparedStatement ptsObtenerPez;
    private PreparedStatement ptsObtenerNombre;
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
            ptsObtenerPez = connection.prepareStatement(QUERY_OBTENER_PEZ);
            ptsObtenerNombre = connection.prepareStatement(QUERY_OBTENER_NOMBRE);
            pstRandomCliente = connection.prepareStatement(QUERY_RANDOM_CLIENTE);
            pstRandomPez = connection.prepareStatement(QUERY_RANDOM_PEZ);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un pedido automático seleccionando un Cliente y un Pez aleatorios.
     * Se devuelve el objeto DTOPedido generado o null si ocurre algún error.
     *
     * @return DTOPedido generado.
     */
    public DTOPedido generarPedidoAutomatico() {
        try {
            // Obtener un Cliente y un Pez aleatorios
            DTOCliente cliente = getRandomCliente();
            DTOPez pez = getRandomPez();
    
            if (cliente != null && pez != null) {
                int cantidad = 10 + random.nextInt(41);
                String numeroReferencia = "PED-" + System.currentTimeMillis();
    
                pstInsertPedido.clearParameters();
                pstInsertPedido.setString(1, numeroReferencia);
                pstInsertPedido.setInt(2, cliente.getId());
                pstInsertPedido.setInt(3, pez.getId());
                pstInsertPedido.setInt(4, cantidad);
    
                int affected = pstInsertPedido.executeUpdate();
                if (affected > 0) {
                    DTOPedido pedido = new DTOPedido(numeroReferencia, cliente.getId(), pez.getId(), cantidad, 0);
                    System.out.println("Se ha generado el pedido con número de referencia: " + pedido.getNumero_referencia() + ".");
                    Simulador.registro.registroGenerarPedidos(pedido.getNumero_referencia());
                    return pedido;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lista los pedidos pendientes (aquellos en los que la cantidad enviada es menor que la solicitada).
     *
     * @return Lista de DTOPedido pendientes.
     */
    public List<DTOPedido> listarPedidosPendientes() {
        List<DTOPedido> pedidos = new ArrayList<>();
        try (ResultSet rs = pstListarPedidosPendientes.executeQuery()) {
            while (rs.next()) {
                pedidos.add(new DTOPedido(
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
     * Lista los pedidos completados (aquellos en los que la cantidad enviada es igual o mayor que la solicitada).
     *
     * @return Lista de DTOPedido completados.
     */
    public List<DTOPedido> listarPedidosCompletados() {
        List<DTOPedido> pedidos = new ArrayList<>();
        try (ResultSet rs = pstListarPedidosCompletados.executeQuery()) {
            while (rs.next()) {
                pedidos.add(new DTOPedido(
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
     * Obtiene un pedido a partir de su número de referencia.
     *
     * @param numeroReferencia Referencia única del pedido.
     * @return DTOPedido encontrado o null si no se encuentra.
     */
    public DTOPedido obtenerPedidoPorReferencia(String numeroReferencia) {
        try {
            pstSeleccionarPedidoPorReferencia.clearParameters();
            pstSeleccionarPedidoPorReferencia.setString(1, numeroReferencia);
            try (ResultSet rs = pstSeleccionarPedidoPorReferencia.executeQuery()) {
                if (rs.next()) {
                    return new DTOPedido(
                            rs.getString("numero_referencia"),
                            rs.getInt("id_cliente"),
                            rs.getInt("id_pez"),
                            rs.getInt("cantidad"),
                            rs.getInt("cantidad_enviada")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza un pedido en la base de datos utilizando el DTOPedido proporcionado.
     *
     * @param pedido DTOPedido con la información actualizada.
     * @return true si la actualización fue exitosa; false en caso contrario.
     */
    public boolean actualizarPedido(DTOPedido pedido) {
        try {
            pstActualizarPedido.clearParameters();
            pstActualizarPedido.setInt(1, pedido.getCantidad_enviada());
            pstActualizarPedido.setString(2, pedido.getNumero_referencia());
            int affected = pstActualizarPedido.executeUpdate();
            Simulador.registro.registroEnviadosConReferencia(pedido.getCantidad_enviada(), obtenerPezPorId(pedido.getId_pez()).getNombre(), pedido.getNumero_referencia());
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Envía una cantidad de peces para un pedido, actualizando la cantidad enviada.
     * Se utiliza el DTOPedido recibido para calcular la cantidad pendiente y se actualiza
     * tanto la base de datos como el objeto DTO.
     *
     * @param pedido DTOPedido del pedido a enviar.
     * @param cantidadDisponible Cantidad de peces disponibles para enviar.
     * @return El DTOPedido actualizado o null si ocurre algún error.
     */
    public DTOPedido enviarPedido(DTOPedido pedido, int cantidadDisponible) {
        int pendiente = pedido.getCantidad() - pedido.getCantidad_enviada();
        int enviar = Math.min(pendiente, cantidadDisponible);
        int nuevaCantidadEnviada = pedido.getCantidad_enviada() + enviar;

        // Se crea un nuevo objeto DTOPedido con la cantidad enviada actualizada
        DTOPedido pedidoActualizado = new DTOPedido(
                pedido.getNumero_referencia(),
                pedido.getId_cliente(),
                pedido.getId_pez(),
                pedido.getCantidad(),
                nuevaCantidadEnviada
        );

        if (actualizarPedido(pedidoActualizado)) {
            return pedidoActualizado;
        }
        return null;
    }

    /**
     * Borra todos los pedidos de la base de datos.
     *
     * @return Número de pedidos borrados.
     */
    public int borrarPedidos() {
        try {
            return pstBorrarPedidos.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public DTOCliente obtenerClientePorId(int idCliente) {
        DTOCliente cliente = null;
        try {
            ptsObtenerNombre.setInt(1, idCliente);
            try (ResultSet rs = ptsObtenerNombre.executeQuery()) {
                if (rs.next()) {
                    cliente = new DTOCliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("nif"),
                        rs.getInt("telefono")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public DTOPez obtenerPezPorId(int idPez) {
        DTOPez pez = null;
        try {
            ptsObtenerPez.setInt(1, idPez);
            try (ResultSet rs = ptsObtenerPez.executeQuery()) {
                if (rs.next()) {
                    pez = new DTOPez(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("nombre_cientifico")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pez;
    }
    

    // --- Métodos privados para obtener DTOCliente y DTOPez aleatorios ---

    /**
     * Obtiene un DTOCliente aleatorio de la base de datos.
     *
     * @return DTOCliente obtenido o null si no hay registros.
     * @throws SQLException
     */
    private DTOCliente getRandomCliente() throws SQLException {
        try (ResultSet rs = pstRandomCliente.executeQuery()) {
            if (rs.next()) {
                return new DTOCliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("nif"),
                        rs.getInt("telefono")
                );
            }
        }
        return null;
    }


    /**
     * Obtiene un DTOPez aleatorio de la base de datos.
     *
     * @return DTOPez obtenido o null si no hay registros.
     * @throws SQLException
     */
    private DTOPez getRandomPez() throws SQLException {
        try (ResultSet rs = pstRandomPez.executeQuery()) {
            if (rs.next()) {
                return new DTOPez(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("nombre_cientifico")
                );
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