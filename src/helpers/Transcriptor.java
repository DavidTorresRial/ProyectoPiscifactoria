package helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import commons.Simulador;

/** Clase para gestionar transcripciones detalladas de acciones en el sistema. */
public class Transcriptor {

    /** Instancia única de la clase Transcriptor. */
    private static Transcriptor instancia;

    /** Escritor utilizado para escribir datos. */
    private BufferedWriter writer;

    /** Constructor privado para Singleton. */
    private Transcriptor(String nombrePartida) {
        try {
            writer = new BufferedWriter(new FileWriter("transcripciones/" + nombrePartida + ".tr", true));
        } catch (IOException e) {
            Simulador.logger.logError("Error al crear el archivo de transcripciones: " + e.getMessage());
        }
    }

    /** Obtiene la instancia única del transcriptor. */
    public static Transcriptor getInstance(String nombrePartida) {
        if (instancia == null) {
            instancia = new Transcriptor(nombrePartida);
        }
        return instancia;
    }

    /** Registra una acción detallada en la transcripción. */
    public void transcribir(String mensaje) {
        try {
            writer.write(mensaje);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            Simulador.logger.logError("Error al escribir en la transcripción: " + e.getMessage());
        }
    }

    /** Cierra el archivo de transcripción. */
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            Simulador.logger.logError("Error al cerrar el archivo de transcripción: " + e.getMessage());
        }
    }

}
