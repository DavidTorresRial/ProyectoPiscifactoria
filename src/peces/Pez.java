package peces;

import java.util.Random;
import propiedades.PecesDatos;

public class Pez {
    private int edad = 0;
    private final boolean sexo; // True para macho, False para hembra
    private boolean fertil = false;
    private boolean vivo = true;
    private boolean alimentado = false;

    private PecesDatos datos;
    
    protected int ciclo;

    // Constructor del pez
    public Pez(boolean sexo, PecesDatos datos) {
        this.edad = 0; // La edad inicial del pez
        this.sexo = sexo;
        this.datos = datos;

        this.ciclo = datos.getCiclo();
    }

    // Método que muestra el estado actual del pez
    public void showStatus() {
        System.out.println("--------------- " + datos.getNombre() + " ---------------");
        System.out.println("Edad: " + edad + " días");
        System.out.println("Sexo: " + (sexo ? "M" : "H")); // "M" para macho, "H" para hembra
        System.out.println("Vivo: " + (vivo ? "Si" : "No"));
        System.out.println("Alimentado: " + (alimentado ? "Si" : "No"));
        System.out.println("Adulto: " + (edad >= datos.getMadurez() ? "Si" : "No"));
        System.out.println("Fértil: " + (fertil ? "Si" : "No"));
    }

    // Método que simula el crecimiento del pez
    public void grow() { // TODO revisar en que orden se ejecutan el metodo y si lo de restar la comida tiene que ir aqui
        if (vivo) {

            // Si no está alimentado, tiene 50% de probabilidad de morir
            if (!alimentado) {
                Random rand = new Random();
                if (rand.nextDouble() < 0.5) {
                    vivo = false; // El pez muere
                    return; // No realizar más acciones si está muerto
                }
            }

            // Incrementar la edad del pez
            edad++;

            // Si la edad alcanza la madurez, el pez se hace fértil
            if (edad >= datos.getMadurez()) {
                fertil = true;
            } else {
                fertil = false;
            }

            alimentado = false;

            if (edad >= datos.getMadurez() && fertil == false) {
                ciclo--;
                if (ciclo == 0) {
                    fertil = true;
                }
            }

            // Si es joven (antes de alcanzar la madurez), tiene un 5% de probabilidad de morir cada 2 días
            if (edad < datos.getMadurez() && edad % 2 == 0) {
                Random rand = new Random();
                if (rand.nextDouble() < 0.05) {
                    vivo = false;
                    return;
                }
            }
        }
    }

    // Método que reinicia el pez
    public void reset() {
        edad = 0;
        fertil = false;
        vivo = true;
        alimentado = false;
    }

    // Getters
    public int getEdad() {
        return edad;
    }

    public boolean isVivo() {
        return vivo;
    }

    public boolean isAlimentado() {
        return alimentado;
    }

    public void alimentar() {
        this.alimentado = true;
    }

    public boolean isFertil() {
        return fertil;
    }

    public boolean isSexo() {
        return sexo;
    }

    public int getHuevos() {
        return datos.getHuevos();
    }

    public PecesDatos getDatos() {
        return datos;
    }

    // Setters
    public void setFertil(boolean fertil) {
        this.fertil = fertil;
    }
}
