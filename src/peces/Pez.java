package peces;

import java.util.Random;

import propiedades.PecesDatos;

/**
 * Clase padre de los peces.
 */
public class Pez {
    
    /** Nombre común del pez */
    private final String nombre; 

    /** Nombre cientifico del pez */
    private final String nombreCientifico;

    /** Edad del pez en días */
    private int edad = 0;

    /** Sexo del pez: true para macho, false para hembra */
    private final boolean sexo;

    /** Estado de fertilidad del pez: true si es fértil, false si no lo es */
    private boolean fertil = false;

    /** Estado de vida del pez: true si está vivo, false si está muerto */
    private boolean vivo = true;
    


    /** Estado de alimentación del pez: true si ha sido alimentado, false si no lo ha sido */
    private boolean alimentado = false;

    /** Ciclo reproductivo del pez, decrementa hasta que el pez se vuelve fértil */
    protected int ciclo;

    /** Datos específicos del pez extraídos de PecesDatos */
    private PecesDatos datos;

    
    /**
     * Constructor que inicializa un Pez con su sexo y datos asociados.
     *
     * @param sexo true para macho, false para hembra
     * @param datos contiene las propiedades del pez
     */
    public Pez(boolean sexo, PecesDatos datos) {
        this.nombre = datos.getNombre();
        this.nombreCientifico = datos.getCientifico();
        this.sexo = sexo;
        this.datos = datos;
        this.ciclo = datos.getCiclo();
    }

    /**
     * Muestra el estado actual del pez.
     */
    public void showStatus() {
        System.out.println("--------------- " + nombre + " ---------------");
        System.out.println("Edad: " + edad + " días");
        System.out.println("Sexo: " + (sexo ? "M" : "H"));
        System.out.println("Vivo: " + (vivo ? "Si" : "No"));
        System.out.println("Alimentado: " + (alimentado ? "Si" : "No"));
        System.out.println("Adulto: " + (edad >= datos.getMadurez() ? "Si" : "No"));
        System.out.println("Fértil: " + (fertil ? "Si" : "No"));
    }

    /**
     * Simula el crecimiento del pez a lo largo de un día.
     */
    public void grow() {
        if (vivo) {
            Random rand = new Random();

            // Si no está alimentado, tiene 50% de probabilidad de morir
            if (!alimentado) {
                if (rand.nextDouble() < 0.5) {
                    vivo = false; // El pez muere
                    return; // No realizar más acciones si está muerto
                }
            }

            // Incrementar la edad del pez
            edad++;

            // Manejo del ciclo reproductivo y fertilidad
            if (edad >= datos.getMadurez()) {
                // Si el pez ha alcanzado la madurez y no es fértil, se reduce el ciclo
                if (!fertil) {
                    ciclo--;
                    if (ciclo <= 0) {
                        fertil = true; // Se vuelve fértil después de completar el ciclo
                    }
                }
            } else {
                // Si el pez no ha alcanzado la madurez, aseguramos que no es fértil
                fertil = false;
            }

            // Si el pez es joven (antes de la madurez), tiene un 5% de probabilidad de morir cada 2 días
            if (edad < datos.getMadurez() && edad % 2 == 0) {
                if (rand.nextDouble() < 0.05) {
                    vivo = false; // El pez muere
                    return; // No realizar más acciones si está muerto
                }
            }
        }
    }

    /**
     * Reinicia el estado del pez a su condición inicial (edad 0, no fértil, vivo, no alimentado).
     */
    public void reset() {
        edad = 0;
        fertil = false;
        vivo = true;
        alimentado = false;

        ciclo = datos.getCiclo();
    }

    /**
     * Crea una copia del pez con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra
     * @return una nueva instancia de Pez con el mismo tipo pero con el sexo especificado
     */
    public Pez clonar(boolean nuevoSexo) {
        return new Pez(nuevoSexo, datos);
    }

    public int getTipo() {
        return datos.getPiscifactoria().getValue();
    }

    // Getters

    /**
     * @return el nombre común del pez
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return el nombre científico del pez
     */
    public String getNombreCientifico() {
        return nombreCientifico;
    }

    /**
     * @return la edad actual del pez en días
     */
    public int getEdad() {
        return edad;
    }

    /**
     * @return true si el pez es macho, false si es hembra
     */
    public boolean isSexo() {
        return sexo;
    }

    /**
     * @return true si el pez es fértil, false si no lo es
     */
    public boolean isFertil() {
        return fertil;
    }

    /**
     * @return true si el pez está vivo, false si está muerto
     */
    public boolean isVivo() {
        return vivo;
    }

    /**
     * @return true si el pez ha sido alimentado, false si no
     */
    public boolean isAlimentado() {
        return alimentado;
    }

    /**
     * @return el ciclo reproductivo del pez
     */
    public int getCiclo() {
        return ciclo;
    }

    /**
     * @return el objeto PecesDatos asociado con este pez
     */
    public PecesDatos getDatos() {
        return datos;
    }

    // Setters

    /**
     * Establece la edad del pez.
     * 
     * @param edad la nueva edad del pez
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Establece si el pez está vivo.
     * 
     * @param vivo true si el pez está vivo, false si está muerto
     */
    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    /**
     * Establece el estado de alimentación del pez.
     * 
     * @param alimentado true si el pez ha sido alimentado, false si no
     */
    public void setAlimentado(boolean alimentado) {
        this.alimentado = alimentado;
    }

    /**
     * Establece el ciclo reproductivo del pez.
     * 
     * @param ciclo el nuevo ciclo reproductivo
     */
    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    /**
     * Establece los datos específicos del pez.
     * 
     * @param datos el objeto PecesDatos asociado
     */
    public void setDatos(PecesDatos datos) {
        this.datos = datos;
    }

    /**
     * Establece si el pez es fértil.
     *
     * @param fertil true si el pez es fértil, false si no lo es
     */
    public void setFertil(boolean fertil) {
        this.fertil = fertil;
    }
}
