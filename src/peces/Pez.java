package peces;

import java.util.Random;

import propiedades.PecesDatos;

/** Clase padre de los peces. */
public abstract class Pez {

    /** Nombre común del pez. */
    private final String nombre;

    /** Nombre cientifico del pez. */
    private final String nombreCientifico;

    /** Edad del pez en días. */
    private int edad = 0;

    /** Sexo del pez: true para macho, false para hembra. */
    private final boolean sexo;

    /** Estado de fertilidad del pez: true si es fértil, false si no lo es. */
    private boolean fertil = false;

    /** Estado de vida del pez: true si está vivo, false si está muerto. */
    private boolean vivo = true;

    /** Estado de alimentación del pez: true si ha sido alimentado, false si no. */
    private boolean alimentado = false;

    /** Ciclo reproductivo del pez, decrementa hasta que el pez se vuelve fértil. */
    protected int ciclo;

    /** Datos específicos del pez extraídos de PecesDatos. */
    private PecesDatos datos;

    private boolean primeraVez = true; // TODO revisar si es completamente necesario este atributo

    /**
     * Constructor que inicializa un Pez con su sexo y datos asociados.
     *
     * @param sexo  true para macho, false para hembra
     * @param datos contiene las propiedades del pez
     */
    public Pez(boolean sexo, PecesDatos datos) {
        this.nombre = datos.getNombre();
        this.nombreCientifico = datos.getCientifico();
        this.sexo = sexo;
        this.datos = datos;
        this.ciclo = datos.getCiclo();
    }

    /** Muestra el estado actual del pez. */
    public void showStatus() {
        System.out.println("--------------- " + nombre + " ---------------");
        System.out.println("Edad: " + edad + " días");
        System.out.println("Sexo: " + (sexo ? "M" : "H"));
        System.out.println("Vivo: " + (vivo ? "Si" : "No"));
        System.out.println("Alimentado: " + (alimentado ? "Si" : "No"));
        System.out.println("Adulto: " + (edad >= datos.getMadurez() ? "Si" : "No"));
        System.out.println("Fértil: " + (fertil ? "Si" : "No"));
    }

    /** Hace crecer un día el pez, realizando toda la lógica. */
    public void grow() {
        if (vivo) {
            Random rand = new Random();

            if (!alimentado && rand.nextBoolean() == true) {
                vivo = false;
                alimentado = false;
                fertil = false;
            } else {
                edad++;

                if (!sexo) {
                    if (edad >= datos.getMadurez() && primeraVez) {
                        fertil = true;
                        primeraVez = false;
                    } else if (edad >= datos.getMadurez()) {
                        ciclo--;
                        if (ciclo <= 0) {
                            fertil = true;
                            ciclo = datos.getCiclo();
                        }
                    } else {
                        fertil = false;
                    }
                } else if (edad >= datos.getMadurez()) {
                    fertil = true;
                }

                if (edad < datos.getMadurez() && edad % 2 == 0) {
                    if (rand.nextDouble() < 0.05) {
                        vivo = false;
                        alimentado = false;
                        fertil = false;
                    }
                }
            }
        }
    }

    /** Reinicia el estado del pez a su condición inicial. */
    public void reset() {
        edad = 0;
        fertil = false;
        vivo = true;
        alimentado = false;
        ciclo = datos.getCiclo();
    }

    /**
     * Método abstracto para clonar el pez con un nuevo sexo.
     *
     * @param nuevoSexo true para macho, false para hembra.
     * @return una nueva instancia de la subclase correspondiente.
     */
    public abstract Pez clonar(boolean nuevoSexo);

    /**
     * @return el nombre común del pez.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return el nombre científico del pez.
     */
    public String getNombreCientifico() {
        return nombreCientifico;
    }

    /**
     * @return la edad actual del pez en días.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * @return true si el pez es macho, false si es hembra.
     */
    public boolean isSexo() {
        return sexo;
    }

    /**
     * @return true si el pez es fértil, false si no lo es.
     */
    public boolean isFertil() {
        return fertil;
    }

    /**
     * @return true si el pez está vivo, false si está muerto.
     */
    public boolean isVivo() {
        return vivo;
    }

    /**
     * @return true si el pez ha sido alimentado, false si no.
     */
    public boolean isAlimentado() {
        return alimentado;
    }

    /**
     * @return el ciclo reproductivo del pez.
     */
    public int getCiclo() {
        return ciclo;
    }

    /**
     * @return el objeto PecesDatos asociado con este pez.
     */
    public PecesDatos getDatos() {
        return datos;
    }

    /**
     * Establece la edad del pez.
     * 
     * @param edad la nueva edad del pez.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Establece si el pez es fértil.
     *
     * @param fertil true si el pez es fértil, false si no lo es.
     */
    public void setFertil(boolean fertil) {
        this.fertil = fertil;
    }

    /**
     * Establece si el pez está vivo.
     * 
     * @param vivo true si el pez está vivo, false si está muerto.
     */
    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    /**
     * Establece el estado de alimentación del pez.
     * 
     * @param alimentado true si el pez ha sido alimentado, false si no.
     */
    public void setAlimentado(boolean alimentado) {
        this.alimentado = alimentado;
    }

    /**
     * Establece el ciclo reproductivo del pez.
     * 
     * @param ciclo el nuevo ciclo reproductivo.
     */
    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    /**
     * Establece los datos específicos del pez.
     * 
     * @param datos el objeto PecesDatos asociado.
     */
    public void setDatos(PecesDatos datos) {
        this.datos = datos;
    }

    /**
     * Devuelve una representación en cadena del estado de un pez.
     *
     * @return una cadena con la información detallada del pez.
     */
    @Override
    public String toString() {
        return "Información del Pez:" +
                "\n  Nombre Común       : " + nombre +
                "\n  Nombre Científico  : " + nombreCientifico +
                "\n  Edad               : " + edad + " días" +
                "\n  Sexo               : " + (sexo ? "Macho" : "Hembra") +
                "\n  Vivo               : " + (vivo ? "Sí" : "No") +
                "\n  Alimentado         : " + (alimentado ? "Sí" : "No") +
                "\n  Adulto             : " + (edad >= datos.getMadurez() ? "Sí" : "No") +
                "\n  Fértil             : " + (fertil ? "Sí" : "No") +
                "\n  Ciclo              : " + ciclo;
    }
}