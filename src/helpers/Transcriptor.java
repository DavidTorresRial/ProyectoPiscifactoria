package helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/** Clase para gestionar transcripciones detalladas de acciones en el sistema. */
public class Transcriptor {
    private static Transcriptor instancia;
    private BufferedWriter writer;

    /**
     * Constructor privado para inicializar el archivo de transcripción.
     * 
     * @param nombrePartida el nombre de la partida que se usará como parte del nombre del archivo.
     */
    private Transcriptor(String nombrePartida) {
        try {
            writer = new BufferedWriter(new FileWriter("transcripciones/" + nombrePartida + ".tr", true));
        } catch (IOException e) {
            System.err.println("Error al crear el archivo de transcripciones: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única del transcriptor.
     * 
     * @param nombrePartida el nombre de la partida para el archivo de transcripción.
     * @return la instancia única del Transcriptor.
     */
    public static Transcriptor getInstance(String nombrePartida) {
        if (instancia == null) {
            instancia = new Transcriptor(nombrePartida);
        }
        return instancia;
    }

    /**
     * Registra una acción detallada en la transcripción.
     * 
     * @param mensaje el mensaje que será registrado en el archivo de transcripción.
     */
    public void transcribir(String mensaje) {
        try {
            writer.write(mensaje);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error al escribir en la transcripción: " + e.getMessage());
        }
    }

    /** Cierra el archivo de transcripción para liberar los recursos asociados. */
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo de transcripción: " + e.getMessage());
        }
    }
}
