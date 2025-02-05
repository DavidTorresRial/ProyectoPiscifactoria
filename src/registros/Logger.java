package registros;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Clase para manejar el registro de logs en un archivo. */
class Logger {

    /** Ruta del archivo de log de errores general. */
    private static final String ERROR_LOG_PATH = "logs/0_errors.log";

    /** Escritor persistente para el log general de errores. */
    private BufferedWriter errorWriter;

    /** Ruta del archivo de log específico de la partida. */
    private String partidaLogPath;

    /** Instancia única de la clase Logger (Singleton). */
    private static Logger instance;

    /**
     * Constructor privado para inicializar el log general y de la partida.
     *
     * @param nombrePartida Nombre de la partida para generar la ruta del log de la partida.
     */
    private Logger(String nombrePartida) {
        try {
            File errorLogFile = new File(ERROR_LOG_PATH);
            if (!errorLogFile.getParentFile().exists()) {
                errorLogFile.getParentFile().mkdirs();
            }
            errorWriter = new BufferedWriter(new FileWriter(errorLogFile, true));

            this.partidaLogPath = "logs/" + nombrePartida + ".log";
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el Logger: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única del Logger (Singleton).
     *
     * @param nombrePartida Nombre de la partida para generar la ruta del log de la partida.
     * @return Instancia del Logger.
     */
    public static Logger getInstance(String nombrePartida) {
        if (instance == null) {
            instance = new Logger(nombrePartida);
        }
        return instance;
    }

    /**
     * Escribe un mensaje en el log general de errores con un timestamp.
     *
     * @param message Mensaje a escribir.
     */
    public void logError(String message) {
        if (errorWriter != null) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String logEntry = "[" + timestamp + "] " + message;
            try {
                errorWriter.write(logEntry);
                errorWriter.newLine();
                errorWriter.flush(); // Asegura que se escribe al archivo inmediatamente.
            } catch (IOException e) {
                System.err.println("Error al escribir en el log general: " + e.getMessage());
            }
        }
    }

    /**
     * Escribe un mensaje en el log específico de la partida.
     *
     * @param message Mensaje a escribir.
     */
    public void logPartida(String message) {
        File logFile = new File(partidaLogPath);
        try {
            if (!logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String logEntry = "[" + timestamp + "] " + message;
                writer.write(logEntry);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el log de partida: " + e.getMessage());
        }
    }

    /** Cierra el escritor persistente del log general. */
    public void close() {
        if (errorWriter != null) {
            try {
                errorWriter.close();
                errorWriter = null;
            } catch (IOException e) {
                System.err.println("Error al cerrar el logger general: " + e.getMessage());
            }
        }
    }






    void logInicioPartida(String nombrePartida, String nombrePiscifactoria) {
        logPartida("Inicio de la simulación " + nombrePartida + ".");
        logPartida("Piscifactoría inicial: " + nombrePiscifactoria + ".");
    }

    void logComprarComidaPiscifactoria(int cantidadComida, String tipoComida, String nombrePiscifactoria) {
        logPartida(cantidadComida + " de comida de tipo " + tipoComida + " comprada. Se almacena en  en la piscifactoría " + nombrePiscifactoria + ".");
    }

    void logComprarComidaAlmacenCentral(int cantidadComida, String tipoComida) {
        logPartida(cantidadComida + " de comida de tipo " + tipoComida + " comprada. Se almacena en el almacén central.");
    }

    void logComprarPeces(String nombrePez, Boolean sexoPez, int numeroTanque, String nombrePiscifactoria) {
        logPartida(nombrePez + " (" + (sexoPez ? "M" : "H") + ") comprado. Añadido al tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    void logVenderPeces(int numeroPecesVendidos, String nombrePiscifactoria){
        logPartida("Vendidos " + numeroPecesVendidos + " peces de la piscifactoría " + nombrePiscifactoria + " de forma manual.");
    }

    void logLimpiarTanque(int numeroTanque, String nombrePiscifactoria) {
        logPartida("Limpiado el tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    void logVaciarTanque(int numeroTanque, String nombrePiscifactoria) {
        logPartida("Vaciado el tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    void logComprarPiscifactoria(String tipoPiscifactoria, String nombrePiscifactoria) {
        logPartida("Comprada la piscifactoría de " + tipoPiscifactoria + " " + nombrePiscifactoria + ".");
    }

    void logComprarTanque(String nombrePiscifactoria) {
        logPartida("Comprado un tanque para la piscifactoría " + nombrePiscifactoria + ".");
    }

    void logComprarAlmacenCentral() {
        logPartida("Comprado el almacén central.");
    }

    void logMejorarPiscifactoria(String nombrePiscifactoria) {
        logPartida("Mejorada la piscifactoría " + nombrePiscifactoria + " aumentando su capacidad de comida.");
    }

    void logMejorarAlmacenCentral(int incrementoCapacidad, int capacidadComida) {
        logPartida("Mejorando el almacén central, aumentando su capacidad de comida en " + incrementoCapacidad + " unidades hasta " + capacidadComida + ".");
    }

    void logFinDelDia(int dia) {
        logPartida("Fin del día " + dia + ".");
    }

    void logOpcionOcultaPeces(String nombrePiscifactoria) {
        logPartida("Añadidos peces mediante la opción oculta a la piscifactoría " + nombrePiscifactoria + ".");
    }

    void logOpcionOcultaMonedas() {
        logPartida("Añadidas monedas mediante la opción oculta.");
    }

    void logCierrePartida() {
        logPartida("Cierre de la partida.");
    }

    void logCrearRecompensa() {
        logPartida("Recompensa creada.");
    }

    void logUsarRecompensa(String nombreRecompensa) {
        logPartida("Recompensa " + nombreRecompensa + " usada.");
    }

    void logGuardarSistema() {
        logPartida("Sistema guardado.");
    }

    void logCargarSistema() {
        logPartida("Sistema cargado.");
    }
}