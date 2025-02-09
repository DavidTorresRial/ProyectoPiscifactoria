package database.dtos;

/**
 * Clase DTOCliente que representa un objeto de transferencia de datos para un cliente.
 */
public class DTOCliente {

    /**
     * Identificador único del cliente.
     */
    private int id;

    /**
     * Nombre del cliente.
     */
    private String nombre;

    /**
     * Número de identificación fiscal (NIF) del cliente.
     */
    private String nif;

    /**
     * Número de teléfono del cliente.
     */
    private int telefono;

    /**
     * Constructor de la clase DTOCliente.
     *
     * @param id Identificador único del cliente.
     * @param nombre Nombre del cliente.
     * @param nif Número de identificación fiscal del cliente.
     * @param telefono Número de teléfono del cliente.
     */
    public DTOCliente(int id, String nombre, String nif, int telefono) {
        this.id = id;
        this.nombre = nombre;
        this.nif = nif;
        this.telefono = telefono;
    }

    /**
     * Obtiene el identificador del cliente.
     *
     * @return El identificador del cliente.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del cliente.
     *
     * @return El nombre del cliente.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el número de identificación fiscal (NIF) del cliente.
     *
     * @return El NIF del cliente.
     */
    public String getNif() {
        return nif;
    }

    /**
     * Obtiene el número de teléfono del cliente.
     *
     * @return El número de teléfono del cliente.
     */
    public int getTelefono() {
        return telefono;
    }
}
