package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * DAO para la tabla Pedido.
 * Provee métodos para generar, listar, enviar y borrar pedidos.
 */
public class DAOPedidos {

    private Random random = new Random();

    /**
     * Genera un pedido de forma automática.
     * Selecciona un cliente y un pez aleatoriamente, asigna una cantidad (entre 10 y 50)
     * y genera una referencia única para insertar el pedido en la base de datos.
     */
    public void generarPedidoAutomatico() {
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = Conexion.getConnection();

            // Obtener un cliente aleatorio
            Integer idCliente = getRandomId(conn, "Cliente");
            if (idCliente == null) {
                System.out.println("No hay clientes registrados.");
                return;
            }

            // Obtener un pez aleatorio
            Integer idPez = getRandomId(conn, "Pez");
            if (idPez == null) {
                System.out.println("No hay peces registrados.");
                return;
            }

            // Definir cantidad aleatoria entre 10 y 50
            int cantidad = 10 + random.nextInt(41);

            // Generar referencia única
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
            try { 
                if (pstm != null) 
                    pstm.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    /**
     * Lista los pedidos pendientes.
     * Extrae de la base de datos los pedidos que no han sido completados y los muestra en el formato:
     * [ref] Nombre cliente: nombre pez enviado/solicitado (X%), ordenados por el nombre del pez.
     *
     * @return Lista de cadenas con la información de los pedidos pendientes.
     */
    public List<String> listarPedidosPendientes() {
        List<String> pedidos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
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
            try { 
                if (rs != null) 
                    rs.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstm != null) 
                    pstm.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
        return pedidos;
    }

    /**
     * Envía un pedido.
     * Actualiza la cantidad enviada de un pedido, incrementándola según la cantidad disponible
     * en el tanque sin superar la cantidad solicitada.
     *
     * @param numeroReferencia La referencia del pedido a enviar.
     * @param cantidadDisponible La cantidad de peces disponibles para enviar.
     * @return true si el pedido queda completo, false en caso contrario.
     */
    public boolean enviarPedido(String numeroReferencia, int cantidadDisponible) {
        Connection conn = null;
        PreparedStatement pstmSelect = null;
        PreparedStatement pstmUpdate = null;
        ResultSet rs = null;
        try {
            conn = Conexion.getConnection();
            String selectSql = "SELECT id, cantidad, cantidad_enviada FROM Pedido WHERE numero_referencia = ?";
            pstmSelect = conn.prepareStatement(selectSql);
            pstmSelect.setString(1, numeroReferencia);
            rs = pstmSelect.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int cantidad = rs.getInt("cantidad");
                int enviada = rs.getInt("cantidad_enviada");
                int pendiente = cantidad - enviada;
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
            try { 
                if (rs != null) 
                    rs.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstmSelect != null) 
                    pstmSelect.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstmUpdate != null) 
                    pstmUpdate.close(); 
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
        String sql = "DELETE FROM Pedido";
        try {
            conn = Conexion.getConnection();
            pstm = conn.prepareStatement(sql);
            int filas = pstm.executeUpdate();
            System.out.println("Se han borrado " + filas + " pedidos de la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { 
                if (pstm != null) 
                    pstm.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
    }

    /**
     * Lista los pedidos completados.
     * Extrae los pedidos donde la cantidad enviada es mayor o igual a la cantidad solicitada,
     * y los muestra en el formato: [ref] Nombre cliente: nombre pez enviado/solicitado (X%),
     * ordenados por el id.
     *
     * @return Lista de cadenas con la información de los pedidos completados.
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
            try { 
                if (rs != null) 
                    rs.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstm != null) 
                    pstm.close(); 
            } catch (SQLException e) {}
            Conexion.closeConnection();
        }
        return pedidos;
    }

    /**
     * Obtiene un ID aleatorio de la tabla especificada.
     * Selecciona un registro al azar de la tabla (Cliente o Pez) y retorna su ID.
     *
     * @param conn Conexión activa.
     * @param tabla Nombre de la tabla ("Cliente" o "Pez").
     * @return Un ID aleatorio o null si no se encuentra ninguno.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    private Integer getRandomId(Connection conn, String tabla) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT id FROM " + tabla + " ORDER BY RAND() LIMIT 1";
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } finally {
            try { 
                if (rs != null) 
                    rs.close(); 
            } catch (SQLException e) {}
            try { 
                if (pstm != null) 
                    pstm.close(); 
            } catch (SQLException e) {}
        }
        return null;
    }

    /**
     * Envía un pedido de forma manual.
     * Muestra los pedidos pendientes, solicita al usuario la referencia del pedido y la cantidad
     * de peces disponibles en el tanque, y actualiza el pedido en la base de datos.
     */
    public void enviarPedidoManual() { //TODO Cambiar para que implemente InputHelper, Logger y mejorar la manera de introducir las referencias de los pedidos (Quitar ID a la tabla pedidos)
        Scanner sc = new Scanner(System.in);

        // Listar pedidos pendientes
        List<String> pedidosPendientes = listarPedidosPendientes();
        if (pedidosPendientes.isEmpty()) {
            System.out.println("No hay pedidos pendientes.");
            return;
        }
        System.out.println("Pedidos pendientes:");
        for (String pedido : pedidosPendientes) {
            System.out.println(pedido);
        }

        // Solicitar la referencia del pedido a enviar
        System.out.print("Introduce la referencia del pedido que deseas enviar: ");
        String refPedido = sc.nextLine().trim();

        // Solicitar la cantidad de peces disponibles en el tanque
        System.out.print("Introduce la cantidad de peces disponibles en el tanque: ");
        int cantidadDisponible;
        try {
            cantidadDisponible = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("La cantidad debe ser un número entero.");
            return;
        }

        // Actualizar el pedido
        boolean completado = enviarPedido(refPedido, cantidadDisponible);
        if (completado) {
            System.out.println("El pedido ha sido completado.");
        } else {
            System.out.println("El pedido no se completó completamente. Aún quedan peces pendientes.");
        }
    }
}
