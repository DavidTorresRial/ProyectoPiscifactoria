package database.dtos;

/**
 * Clase DTOPeZ que representa un objeto de transferencia de datos para un pez.
 */
public class DTOPez {

    /**
     * Identificador único del pez.
     */
    private int id;

    /**
     * Nombre común del pez.
     */
    private String nombre;

    /**
     * Nombre científico del pez.
     */
    private String nombre_cientifico;

    /**
     * Constructor de la clase DTOPeZ.
     * 
     * @param id Identificador único del pez.
     * @param nombre Nombre común del pez.
     * @param nombre_cientifico Nombre científico del pez.
     */
    public DTOPez(int id, String nombre, String nombre_cientifico) {
        this.id = id;
        this.nombre = nombre;
        this.nombre_cientifico = nombre_cientifico;
    }

    /**
     * Obtiene el identificador del pez.
     * 
     * @return El identificador del pez.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre común del pez.
     * 
     * @return El nombre común del pez.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el nombre científico del pez.
     * 
     * @return El nombre científico del pez.
     */
    public String getNombre_cientifico() {
        return nombre_cientifico;
    }
}
