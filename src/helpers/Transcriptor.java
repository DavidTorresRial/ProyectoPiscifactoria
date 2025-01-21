package helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/** Clase para gestionar transcripciones detalladas de acciones en el sistema. */public class Transcriptor {

    /** Instancia única de la clase Transcriptor. */
    private static Transcriptor instancia;

    /** Nombre del archivo de transcripción. */
    private String archivoTranscripcion;

    /**
     * Constructor privado para inicializar el archivo de transcripción.
     * 
     * @param nombrePartida el nombre de la partida que se usará como parte del nombre del archivo.
     */
    private Transcriptor(String nombrePartida) {
        this.archivoTranscripcion = "transcripciones/" + nombrePartida + ".tr";
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTranscripcion, true))) {
            writer.write(mensaje);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en la transcripción: " + e.getMessage());
        }
    }
}
