package database.dtos;

/**
 * Clase DTOPedido que representa un objeto de transferencia de datos para un pedido.
 */
public class DTOPedido {
    
    /**
     * Identificador único del pedido.
     */
    private int id;

    /**
     * Número de referencia del pedido.
     */
    private String numero_referencia;

    /**
     * Identificador del cliente asociado al pedido.
     */
    private int id_cliente;

    /**
     * Identificador del pez asociado al pedido.
     */
    private int id_pez;

    /**
     * Cantidad total pedida.
     */
    private int cantidad;

    /**
     * Cantidad enviada hasta el momento.
     */
    private int cantidad_enviada;

    /**
     * Constructor de la clase DTOPedido.
     * 
     * @param id Identificador único del pedido.
     * @param numero_referencia Número de referencia del pedido.
     * @param id_cliente Identificador del cliente asociado al pedido.
     * @param id_pez Identificador del pez asociado al pedido.
     * @param cantidad Cantidad total pedida.
     * @param cantidad_enviada Cantidad enviada hasta el momento.
     */
    public DTOPedido(int id, String numero_referencia, int id_cliente, int id_pez, int cantidad, int cantidad_enviada) {
        this.id = id;
        this.numero_referencia = numero_referencia;
        this.id_cliente = id_cliente;
        this.id_pez = id_pez;
        this.cantidad = cantidad;
        this.cantidad_enviada = cantidad_enviada;
    }

    /**
     * Obtiene el identificador del pedido.
     * 
     * @return El identificador del pedido.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el número de referencia del pedido.
     * 
     * @return El número de referencia del pedido.
     */
    public String getNumero_referencia() {
        return numero_referencia;
    }

    /**
     * Obtiene el identificador del cliente asociado al pedido.
     * 
     * @return El identificador del cliente.
     */
    public int getId_cliente() {
        return id_cliente;
    }

    /**
     * Obtiene el identificador del pez asociado al pedido.
     * 
     * @return El identificador del pez.
     */
    public int getId_pez() {
        return id_pez;
    }

    /**
     * Obtiene la cantidad total pedida.
     * 
     * @return La cantidad total pedida.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Obtiene la cantidad enviada hasta el momento.
     * 
     * @return La cantidad enviada.
     */
    public int getCantidad_enviada() {
        return cantidad_enviada;
    }
}
