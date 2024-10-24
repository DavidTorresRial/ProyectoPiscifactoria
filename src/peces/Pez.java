package peces;

import java.util.Random;
import propiedades.PecesDatos;

public class Pez {
    private int edad = 0; // La edad inicial del pez
    private final boolean sexo; // True para macho, False para hembra
    private boolean fertil = false; // True si el pez es fútil, False si no
    private boolean vivo = true; // True si el pez está vivo, False si no
    private boolean alimentado = false; // True si el pez está alimentado, False si no

    private PecesDatos datos;
    protected int ciclo;

    // Constructor del pez
    public Pez(boolean sexo, PecesDatos datos) {
        this.edad = 0; // Inicialización explícita
        this.sexo = sexo; // Inicialización explícita
        this.fertil = false; // Inicialización explícita
        this.vivo = true; // Inicialización explícita
        this.alimentado = false; // Inicialización explícita

        this.datos = datos;
        this.ciclo = datos.getCiclo();
    }

    // Método que muestra el estado actual del pez
    public void showStatus() {
        System.out.println("--------------- " + datos.getNombre() + " ---------------");
        System.out.println("Edad: " + edad + " días");
        System.out.println("Sexo: " + (sexo ? "M" : "H"));
        System.out.println("Vivo: " + (vivo ? "Si" : "No"));
        System.out.println("Alimentado: " + (alimentado ? "Si" : "No"));
        System.out.println("Adulto: " + (edad >= datos.getMadurez() ? "Si" : "No"));
        System.out.println("Fértil: " + (fertil ? "Si" : "No"));
    }

    // Método que simula el crecimiento del pez
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

    // Método que reinicia el pez
    public void reset() {
        edad = 0;
        fertil = false;
        vivo = true;
        alimentado = false;
    }

    public Pez clonar(boolean nuevoSexo) {
        return new Pez(nuevoSexo, datos);
    }

    // Getters
    public int getEdad() {
        return edad;
    }

    public boolean isSexo() {
        return sexo;
    }

    public boolean isFertil() {
        return fertil;
    }

    public boolean isVivo() {
        return vivo;
    }

    public boolean isAlimentado() {
        return alimentado;
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

    public void setAlimentado() {
        this.alimentado = true;
    }
}
