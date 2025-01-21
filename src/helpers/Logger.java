package helpers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Clase para manejar el registro de logs en archivos. */
public class Logger {

    /** Ruta del archivo de log de errores general. */
    private static final String ERROR_LOG_PATH = "logs/0_errors.log";

    /** Instancia única de la clase Logger. */
    private static Logger instance;

    /** Escritor persistente para el log general de errores. */
    private BufferedWriter errorWriter;

    /**
     * Constructor privado para inicializar el log general de errores.
     */
    private Logger() {      //TODO preguntar si guardar como variable la ruta del archivo de log de partida pasandole el nombre como parametro de el constructor de Logger
        try {
            File errorLogFile = new File(ERROR_LOG_PATH);
            if (!errorLogFile.getParentFile().exists()) {
                errorLogFile.getParentFile().mkdirs();
            }
            errorWriter = new BufferedWriter(new FileWriter(errorLogFile, true));
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el Logger general de errores: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única del Logger.
     * 
     * @return La instancia del Logger.
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Escribe un mensaje en un archivo de log específico con un timestamp.
     * Se utiliza un stream temporal para escribir en logs de partida.
     * 
     * @param logFileName El nombre del archivo de log (sin extensión).
     * @param message El mensaje a escribir.
     */
    public void log(String logFileName, String message) {
        File logFile = new File("logs/" + logFileName + ".log");  //TODO si meter esta linea en el init o aunque sea la ruta pasandole como parametro en nombre de la partida
        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile, true))) {
            if (!logFile.getParentFile().exists()) {
                logFile.getParentFile().mkdirs();
            }
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            logWriter.write("[" + timestamp + "] " + message);
            logWriter.newLine();
        } catch (IOException e) {
            logError("Error al escribir en el log '" + logFileName + "': " + e.getMessage());
        }
    }

    /**
     * Escribe un mensaje en el log general de errores con un timestamp.
     * 
     * @param errorMessage El mensaje de error a escribir.
     */
    public void logError(String errorMessage) {
        try {
            if (errorWriter != null) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                errorWriter.write("[" + timestamp + "] " + errorMessage);
                errorWriter.newLine();
                errorWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el log general de errores: " + e.getMessage());
        }
    }

    /**
     * Cierra el archivo de log general de errores.
     */
    public void close() {
        try {
            if (errorWriter != null) {
                errorWriter.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el archivo de log general de errores: " + e.getMessage());
        }
    }
}