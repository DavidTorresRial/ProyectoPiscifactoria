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
    private final String nombreCientifico; // Nombre cientifico del pez

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

        this.edad = 0; // Inicialización explícita
        this.sexo = sexo; // Inicialización explícita
        this.fertil = false; // Inicialización explícita
        this.vivo = true; // Inicialización explícita
        this.alimentado = false; // Inicialización explícita

        this.datos = datos;
        this.ciclo = datos.getCiclo();
    }

    /**
     * Muestra el estado actual del pez (edad, sexo, estado de vida, estado de alimentación, madurez, fertilidad).
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
     * Simula el crecimiento del pez a lo largo de un día, actualizando su edad, estado de fertilidad y vida.
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
     * @return el objeto PecesDatos asociado con este pez, que contiene datos específicos como el ciclo de vida
     */
    public PecesDatos getDatos() {
        return datos;
    }

    /**
     * @return el número de huevos que pone este tipo de pez
     */
    public int getHuevos() {
        return datos.getHuevos();
    }

    /**
     * @return el nombre de la piscifactoría a la que pertenece este pez
     */
    public String geTipo(){
        return datos.getPiscifactoria().getName();
    }

    // Setters

    /**
     * Establece si el pez es fértil.
     *
     * @param fertil true si el pez es fértil, false si no lo es
     */
    public void setFertil(boolean fertil) {
        this.fertil = fertil;
    }

    /**
     * Marca el pez como alimentado.
     */
    public void setAlimentado() {
        this.alimentado = true;
    }
}
