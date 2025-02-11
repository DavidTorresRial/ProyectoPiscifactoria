package database;

import database.dtos.DTOCliente;
import database.dtos.DTOPedido;
import database.dtos.DTOPez;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

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

    /** Generador de números aleatorios. */
    private Random random = new Random();

    /** Consulta SQL para insertar un nuevo pedido en la base de datos. */
    private static final String QUERY_INSERT_PEDIDO = 
        "INSERT INTO Pedido (numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada) VALUES (?, ?, ?, ?, 0)";
    
    /** Consulta SQL para listar los pedidos completados. */
    private static final String QUERY_LISTAR_PEDIDOS_COMPLETADOS =
        "SELECT numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada FROM Pedido WHERE cantidad_enviada >= cantidad ORDER BY numero_referencia";
    
    /** Consulta SQL para listar los pedidos pendientes. */
    private static final String QUERY_LISTAR_PEDIDOS_PENDIENTES =
        "SELECT numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada FROM Pedido WHERE cantidad_enviada < cantidad ORDER BY numero_referencia";
    
    /** Consulta SQL para seleccionar un pedido por su número de referencia. */
    private static final String QUERY_SELECCIONAR_PEDIDO_POR_REFERENCIA =
        "SELECT numero_referencia, id_cliente, id_pez, cantidad, cantidad_enviada FROM Pedido WHERE numero_referencia = ?";
    
    /** Consulta SQL para actualizar la cantidad enviada de un pedido. */
    private static final String QUERY_ACTUALIZAR_PEDIDO =
        "UPDATE Pedido SET cantidad_enviada = ? WHERE numero_referencia = ?";
    
    /** Consulta SQL para eliminar todos los pedidos de la base de datos. */
    private static final String QUERY_BORRAR_PEDIDOS = "DELETE FROM Pedido";

    /** Consulta SQL para obtener un cliente aleatorio de la base de datos. */
    private static final String QUERY_RANDOM_CLIENTE =
        "SELECT id, nombre, nif, telefono FROM Cliente ORDER BY RAND() LIMIT 1";

    /** Consulta SQL para obtener un pez aleatorio de la base de datos. */
    private static final String QUERY_RANDOM_PEZ =
        "SELECT id, nombre, nombre_cientifico FROM Pez ORDER BY RAND() LIMIT 1";
    
    /** Consulta SQL para obtener los datos de un pez por su ID. */
    private static final String QUERY_OBTENER_PEZ = "SELECT id, nombre, nombre_cientifico FROM Pez WHERE id = ?";

    /** Consulta SQL para obtener los datos de un cliente por su ID. */
    private static final String QUERY_OBTENER_NOMBRE = "SELECT id, nombre, nif, telefono FROM Cliente WHERE id = ?";

    /** Conexión a la base de datos. */
    private Connection connection = Conexion.getConnection();
    
    /** PreparedStatement para insertar un pedido. */
    private PreparedStatement pstInsertPedido;

    /** PreparedStatement para listar pedidos pendientes. */
    private PreparedStatement pstListarPedidosPendientes;

    /** PreparedStatement para listar pedidos completados. */
    private PreparedStatement pstListarPedidosCompletados;

    /** PreparedStatement para seleccionar un pedido por referencia. */
    private PreparedStatement pstSeleccionarPedidoPorReferencia;

    /** PreparedStatement para actualizar la cantidad enviada de un pedido. */
    private PreparedStatement pstActualizarPedido;

    /** PreparedStatement para borrar todos los pedidos. */
    private PreparedStatement pstBorrarPedidos;

    /** PreparedStatement para obtener los datos de un pez por su ID. */
    private PreparedStatement ptsObtenerPez;

    /** PreparedStatement para obtener los datos de un cliente por su ID. */
    private PreparedStatement ptsObtenerNombre;

    /** PreparedStatement para obtener un cliente aleatorio. */
    private PreparedStatement pstRandomCliente;

    /** PreparedStatement para obtener un pez aleatorio. */
    private PreparedStatement pstRandomPez;

    /** Constructor que prepara los statements. */
    public DAOPedidos() {
        try {
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
            Simulador.registro.registroLogError("Error al inicializar DAOPedidos y preparar los statements: " + e.getMessage());
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
            Simulador.registro.registroLogError("Error al generar un pedido automático: " + e.getMessage());
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
            Simulador.registro.registroLogError("Error al listar los pedidos pendientes: " + e.getMessage());
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
            Simulador.registro.registroLogError("Error al listar los pedidos completados: " + e.getMessage());
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
            Simulador.registro.registroLogError("Error al obtener un pedido por referencia: " + e.getMessage());
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
            Simulador.registro.registroLogError("Error al actualizar un pedido: " + e.getMessage());
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
            Simulador.registro.registroLogError("Error al borrar los pedidos de la base de datos: " + e.getMessage());
        }
        return 0;
    }

    public DTOCliente obtenerClientePorId(int idCliente) {  //TODO Hacer DTOInfoPedido y quitar método.
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
            Simulador.registro.registroLogError("Error al obtener un cliente por ID: " + e.getMessage());
        }
        return cliente;
    }

    public DTOPez obtenerPezPorId(int idPez) {              //TODO Hacer DTOInfoPedido y quitar método.
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
            Simulador.registro.registroLogError("Error al obtener un pez por ID: " + e.getMessage());
        }
        return pez;
    }
    
    /**
     * Obtiene un DTOCliente aleatorio de la base de datos.
     *
     * @return DTOCliente obtenido o null si no hay registros o se produce un error.
     */
    private DTOCliente getRandomCliente() {
        try (ResultSet rs = pstRandomCliente.executeQuery()) {
            if (rs.next()) {
                return new DTOCliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("nif"),
                        rs.getInt("telefono")
                );
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al obtener un cliente aleatorio: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene un DTOPez aleatorio de la base de datos.
     *
     * @return DTOPez obtenido o null si no hay registros o se produce un error.
     */
    private DTOPez getRandomPez() {
        try (ResultSet rs = pstRandomPez.executeQuery()) {
            if (rs.next()) {
                return new DTOPez(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("nombre_cientifico")
                );
            }
        } catch (SQLException e) {
            Simulador.registro.registroLogError("Error al obtener un pez aleatorio: " + e.getMessage());
        }
        return null;
    }


    /** Cierra la conexión y todos los PreparedStatement abiertos. */
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
            Simulador.registro.registroLogError("Error al cerrar recursos de base de datos: " + e.getMessage());
        }        
    }
}