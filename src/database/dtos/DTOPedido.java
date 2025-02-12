package database.dtos;

/** DTO que representa un pedido con información resumida. */
public class DTOPedido {

    /** Número de referencia único del pedido. */
    private String numeroReferencia;

    /** Nombre del cliente que realizó el pedido. */
    private String nombreCliente;

    /** Nombre del pez solicitado en el pedido. */
    private String nombrePez;

    /** Cantidad de peces enviados hasta el momento. */
    private int cantidadEnviada;

    /** Cantidad total de peces solicitados en el pedido. */
    private int cantidadTotal;

    /**
     * Construye un objeto DTOPedido con los datos proporcionados.
     * 
     * @param numeroReferencia Número de referencia del pedido.
     * @param nombreCliente Nombre del cliente que realizó el pedido.
     * @param nombrePez Nombre del pez solicitado.
     * @param cantidadEnviada Cantidad de peces enviados.
     * @param cantidadTotal Cantidad total de peces solicitados.
     */
    public DTOPedido(String numeroReferencia, String nombreCliente, String nombrePez, int cantidadEnviada, int cantidadTotal) {
        this.numeroReferencia = numeroReferencia;
        this.nombreCliente = nombreCliente;
        this.nombrePez = nombrePez;
        this.cantidadEnviada = cantidadEnviada;
        this.cantidadTotal = cantidadTotal;
    }

    /**
     * Obtiene el número de referencia del pedido.
     * 
     * @return Número de referencia del pedido.
     */
    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    /**
     * Obtiene el nombre del cliente.
     * 
     * @return Nombre del cliente.
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Obtiene el nombre del pez solicitado en el pedido.
     * 
     * @return Nombre del pez.
     */
    public String getNombrePez() {
        return nombrePez;
    }

    /**
     * Obtiene la cantidad de peces enviados hasta el momento.
     * 
     * @return Cantidad de peces enviados.
     */
    public int getCantidadEnviada() {
        return cantidadEnviada;
    }

    /**
     * Obtiene la cantidad total de peces solicitados en el pedido.
     * 
     * @return Cantidad total solicitada.
     */
    public int getCantidadTotal() {
        return cantidadTotal;
    }

    /**
     * Calcula el porcentaje de peces enviados en relación con la cantidad total.
     * 
     * @return Porcentaje de peces enviados. Si la cantidad total es 0, devuelve 0.
     */
    public int getPorcentajeEnviado() {
        if (cantidadTotal > 0) {
            return (cantidadEnviada * 100) / cantidadTotal;
        }
        return 0;
    }

    /**
     * Devuelve una representación en cadena del pedido.
     * 
     * @return una cadena que describe los detalles del pedido.
     */
    @Override
    public String toString() {
        return "\nInformación del Pedido:" +
                "\n  Número de Referencia  : " + numeroReferencia +
                "\n  Cliente               : " + nombreCliente +
                "\n  Pez                   : " + nombrePez +
                "\n  Cantidad Total        : " + cantidadTotal +
                "\n  Cantidad Enviada      : " + cantidadEnviada +
                " (" + (cantidadTotal > 0 ? ((cantidadEnviada * 100.0) / cantidadTotal) : "0") + "% enviado)";
    }
}