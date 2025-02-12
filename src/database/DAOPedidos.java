package database;

import database.dtos.DTOPedido;
import commons.Simulador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** DAO para la tabla Pedido. */
public class DAOPedidos {

    /** Generador de números aleatorios. */
    private Random random = new Random();

    /** Query para insertar un pedido en la tabla Pedido. */
    private static final String QUERY_INSERT_PEDIDO =
        "INSERT INTO Pedido (numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada) " +
        "VALUES (?, ?, ?, ?, 0)";

    /** Query para listar los pedidos pendientes con la información de Cliente y Pez. */
    private static final String QUERY_LISTAR_PEDIDOS_PENDIENTES =
        "SELECT p.numero_referencia AS numeroReferencia, " +
        "       c.nombre AS nombreCliente, " +
        "       pe.nombre AS nombrePez, " +
        "       p.cantidad AS cantidadTotal, " +
        "       p.cantidad_enviada AS cantidadEnviada " +
        "FROM Pedido p " +
        "JOIN Cliente c ON p.id_cliente = c.id " +
        "JOIN Pez pe ON p.id_pez = pe.id " +
        "WHERE p.cantidad_enviada < p.cantidad " +
        "ORDER BY pe.nombre";

    /** Query para listar los pedidos completados con la información de Cliente y Pez. */
    private static final String QUERY_LISTAR_PEDIDOS_COMPLETADOS =
        "SELECT p.numero_referencia AS numeroReferencia, " +
        "       c.nombre AS nombreCliente, " +
        "       pe.nombre AS nombrePez, " +
        "       p.cantidad AS cantidadTotal, " +
        "       p.cantidad_enviada AS cantidadEnviada " +
        "FROM Pedido p " +
        "JOIN Cliente c ON p.id_cliente = c.id " +
        "JOIN Pez pe ON p.id_pez = pe.id " +
        "WHERE p.cantidad_enviada = p.cantidad " +
        "ORDER BY pe.nombre";

    /** Query para seleccionar un pedido por su número de referencia. */
    private static final String QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA =
        "SELECT p.numero_referencia AS numeroReferencia, " +
        "       c.nombre AS nombreCliente, " +
        "       pe.nombre AS nombrePez, " +
        "       p.cantidad AS cantidadTotal, " +
        "       p.cantidad_enviada AS cantidadEnviada " +
        "FROM Pedido p " +
        "JOIN Cliente c ON p.id_cliente = c.id " +
        "JOIN Pez pe ON p.id_pez = pe.id " +
        "WHERE p.numero_referencia = ?";

    /** Query para actualizar la cantidad enviada de un pedido. */
    private static final String QUERY_ACTUALIZAR_PEDIDO =
        "UPDATE Pedido SET cantidad_enviada = ? WHERE numero_referencia = ?";

    /** Query para borrar todos los pedidos de la tabla Pedido. */
    private static final String QUERY_BORRAR_PEDIDOS = "DELETE FROM Pedido";

    /** Query para obtener un cliente aleatorio (sólo id y nombre). */
    private static final String QUERY_RANDOM_CLIENTE =
        "SELECT id, nombre FROM Cliente ORDER BY RAND() LIMIT 1";

    /** Query para obtener un pez aleatorio (sólo id y nombre). */
    private static final String QUERY_RANDOM_PEZ =
        "SELECT id, nombre FROM Pez ORDER BY RAND() LIMIT 1";

    /** Conexión a la base de datos. */
    private Connection connection = Conexion.getConnection();

    /** PreparedStatement para ejecutar la inserción de un pedido. */
    private PreparedStatement pstInsertPedido;

    /** PreparedStatement para listar los pedidos pendientes. */
    private PreparedStatement pstListarPedidosPendientes;

    /** PreparedStatement para listar los pedidos completados. */
    private PreparedStatement pstListarPedidosCompletados;

    /** PreparedStatement para seleccionar un pedido por su número de referencia. */
    private PreparedStatement pstSeleccionarPedidoPorReferencia;

    /** PreparedStatement para actualizar un pedido. */
    private PreparedStatement pstActualizarPedido;

    /** PreparedStatement para borrar todos los pedidos. */
    private PreparedStatement pstBorrarPedidos;

    /** PreparedStatement para obtener un cliente aleatorio. */
    private PreparedStatement pstRandomCliente;

    /** PreparedStatement para obtener un pez aleatorio. */
    private PreparedStatement pstRandomPez;

    /** Constructor DAOPedidos que prepara los statements. */
    public DAOPedidos() {
        try {
            pstInsertPedido = connection.prepareStatement(QUERY_INSERT_PEDIDO);
            pstListarPedidosPendientes = connection.prepareStatement(QUERY_LISTAR_PEDIDOS_PENDIENTES);
            pstListarPedidosCompletados = connection.prepareStatement(QUERY_LISTAR_PEDIDOS_COMPLETADOS);
            pstSeleccionarPedidoPorReferencia = connection.prepareStatement(QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA);
            pstActualizarPedido = connection.prepareStatement(QUERY_ACTUALIZAR_PEDIDO);
            pstBorrarPedidos = connection.prepareStatement(QUERY_BORRAR_PEDIDOS);
            pstRandomCliente = connection.prepareStatement(QUERY_RANDOM_CLIENTE);
            pstRandomPez = connection.prepareStatement(QUERY_RANDOM_PEZ);
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al inicializar DAOPedidos: " + e.getMessage());
        }
    }

    /**
     * Genera un pedido automático con un cliente y pez aleatorio.
     * 
     * @return Un objeto DTOPedido si se genera correctamente, o null si ocurre un error.
     */
    public DTOPedido generarPedidoAutomatico() {
        try {
            int idCliente = getRandomClienteId();
            int idPez = getRandomPezId();

            String nombreCliente = obtenerNombreClientePorId(idCliente);
            String nombrePez = obtenerNombrePezPorId(idPez);

            if (idCliente != -1 && idPez != -1 && nombreCliente != null && nombrePez != null) {
                int cantidad = 10 + random.nextInt(41);
                String numeroReferencia = "PED-" + System.currentTimeMillis();

                pstInsertPedido.clearParameters();
                pstInsertPedido.setString(1, numeroReferencia);
                pstInsertPedido.setInt(2, idCliente);
                pstInsertPedido.setInt(3, idPez);
                pstInsertPedido.setInt(4, cantidad);

                int affected = pstInsertPedido.executeUpdate();
                if (affected > 0) {
                    DTOPedido pedido = new DTOPedido(numeroReferencia, nombreCliente, nombrePez, 0, cantidad);
                    System.out.println("Se ha generado el pedido: " + pedido);
                    Simulador.registro.registroGenerarPedidos(pedido.getNumeroReferencia());
                    return pedido;
                }
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al generar pedido automático: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista los pedidos pendientes (donde la cantidad enviada es menor que la solicitada).
     *
     * @return Lista de DTOPedido pendientes.
     */
    public List<DTOPedido> listarPedidosPendientes() {
        List<DTOPedido> pedidos = new ArrayList<>();
        try (ResultSet rs = pstListarPedidosPendientes.executeQuery()) {
            while (rs.next()) {
                DTOPedido pedido = new DTOPedido(
                        rs.getString("numeroReferencia"),
                        rs.getString("nombreCliente"),
                        rs.getString("nombrePez"),
                        rs.getInt("cantidadEnviada"),
                        rs.getInt("cantidadTotal")
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al listar pedidos pendientes: " + e.getMessage());
        }
        return pedidos;
    }

    /**
     * Lista los pedidos completados (donde la cantidad enviada es igual a la solicitada).
     *
     * @return Lista de DTOPedido completados.
     */
    public List<DTOPedido> listarPedidosCompletados() {
        List<DTOPedido> pedidos = new ArrayList<>();
        try (ResultSet rs = pstListarPedidosCompletados.executeQuery()) {
            while (rs.next()) {
                DTOPedido pedido = new DTOPedido(
                        rs.getString("numeroReferencia"),
                        rs.getString("nombreCliente"),
                        rs.getString("nombrePez"),
                        rs.getInt("cantidadEnviada"),
                        rs.getInt("cantidadTotal")
                );
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al listar pedidos completados: " + e.getMessage());
        }
        return pedidos;
    }

    /**
     * Busca un pedido en la base de datos por su número de referencia.
     *
     * @param numeroReferencia El número de referencia del pedido.
     * @return Un objeto DTOPedido si se encuentra, o null si no existe o ocurre un error.
     */
    public DTOPedido obtenerPedidoPorReferencia(String numeroReferencia) {
        try {
            pstSeleccionarPedidoPorReferencia.clearParameters();
            pstSeleccionarPedidoPorReferencia.setString(1, numeroReferencia);
            try (ResultSet rs = pstSeleccionarPedidoPorReferencia.executeQuery()) {
                if (rs.next()) {
                    return new DTOPedido(
                            rs.getString("numeroReferencia"),
                            rs.getString("nombreCliente"),
                            rs.getString("nombrePez"),
                            rs.getInt("cantidadEnviada"),
                            rs.getInt("cantidadTotal")
                    );
                }
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al obtener pedido por referencia: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza la cantidad enviada de un pedido en la base de datos.
     *
     * @param pedido El pedido a actualizar.
     * @return true si la actualización fue exitosa, false si ocurrió un error.
     */
    public boolean actualizarPedido(DTOPedido pedido) {
        try {
            pstActualizarPedido.clearParameters();
            pstActualizarPedido.setInt(1, pedido.getCantidadEnviada());
            pstActualizarPedido.setString(2, pedido.getNumeroReferencia());
            int affected = pstActualizarPedido.executeUpdate();
            Simulador.registro.registroEnviadosConReferencia(
                    pedido.getCantidadEnviada(),
                    pedido.getNombrePez(),
                    pedido.getNumeroReferencia()
            );
            return affected > 0;
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al actualizar pedido: " + e.getMessage());
        }
        return false;
    }

    /**
     * Envía una cantidad de un pedido, actualizando la cantidad enviada.
     *
     * @param pedido El pedido a actualizar.
     * @param cantidadDisponible La cantidad disponible para enviar.
     * @return El pedido actualizado si se realizó el envío, o null si hubo un error.
     */
    public DTOPedido enviarPedido(DTOPedido pedido, int cantidadDisponible) {
        int pendiente = pedido.getCantidadTotal() - pedido.getCantidadEnviada();
        int enviar = Math.min(pendiente, cantidadDisponible);
        int nuevaCantidadEnviada = pedido.getCantidadEnviada() + enviar;

        DTOPedido pedidoActualizado = new DTOPedido(
                pedido.getNumeroReferencia(),
                pedido.getNombreCliente(),
                pedido.getNombrePez(),
                nuevaCantidadEnviada,
                pedido.getCantidadTotal()
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
            Simulador.registro.registroLogError("Error al borrar pedidos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Obtiene un ID aleatorio de la tabla Cliente.
     *
     * @return ID obtenido o -1 en caso de error.
     */
    private int getRandomClienteId() {
        String query = "SELECT id FROM Cliente ORDER BY RAND() LIMIT 1";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al obtener ID de cliente aleatorio: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Obtiene un ID aleatorio de la tabla Pez.
     *
     * @return ID obtenido o -1 en caso de error.
     */
    private int getRandomPezId() {
        String query = "SELECT id FROM Pez ORDER BY RAND() LIMIT 1";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al obtener ID de pez aleatorio: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Obtiene el nombre de un cliente dado su ID.
     *
     * @param id ID del cliente.
     * @return Nombre del cliente o null.
     */
    public String obtenerNombreClientePorId(int id) {
        String query = "SELECT nombre FROM Cliente WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al obtener el nombre del cliente: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene el nombre de un pez dado su ID.
     *
     * @param id ID del pez.
     * @return Nombre del pez o null.
     */
    public String obtenerNombrePezPorId(int id) {
        String query = "SELECT nombre FROM Pez WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al obtener el nombre del pez: " + e.getMessage());
        }
        return null;
    }

    /** Cierra la conexión y todos los PreparedStatements. */
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
            Simulador.registro.registroLogError("Error al cerrar recursos: " + e.getMessage());
        }
    }
}