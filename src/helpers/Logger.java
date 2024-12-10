package helpers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import commons.Simulador;

/** Clase para manejar el registro de logs en un archivo. */
public class Logger {

    /** Instancia única de la clase Logger. */
    private static Logger instance;

    /** Escritor utilizado para registrar mensajes en el archivo de logs. */
    private BufferedWriter logWriter;

    /** Escritor utilizado para registrar mensajes en el archivo de logs general. */
    private BufferedWriter errorWriter;

    /**
     * Constructor privado para inicializar los escritores de logs.
     * 
     * @param logFileName El nombre del archivo de log.
     * @param errorFileName El nombre del archivo para los logs de errores.
     */
    private Logger(String logFileName) {
        try {
            File logFile = new File("logs/" + logFileName + ".log");

            logWriter = new BufferedWriter(new FileWriter(logFile, true));
            errorWriter = new BufferedWriter(new FileWriter(Simulador.errorLog, true));
            
        } catch (IOException e) {
            Simulador.logger.logError("No se pudo iniciar el Logger: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia del Logger.
     * 
     * @param logFileName El nombre del archivo de log.
     * @param errorFileName El nombre del archivo para logs de errores.
     * @return La instancia del Logger.
     */
    public static Logger getInstance(String logFileName) {
        if (instance == null) {
            instance = new Logger(logFileName);
        }
        return instance;
    }

    /**
     * Escribe un mensaje en el archivo de log con un timestamp.
     * 
     * @param message El mensaje a escribir en el log.
     */
    public void log(String message) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            logWriter.write("[" + timestamp + "] " + message);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException e) {
            Simulador.logger.logError("Error al escribir en el log: " + e.getMessage());
        }
    }

    /**
     * Escribe un mensaje de error en el archivo de log de errores con un timestamp.
     * 
     * @param errorMessage El mensaje de error a escribir en el log.
     */
    public void logError(String errorMessage) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            errorWriter.write("[" + timestamp + "] " + errorMessage);
            errorWriter.newLine();
            errorWriter.flush();
        } catch (IOException e) {
            Simulador.logger.logError("Error al escribir en el log: " + e.getMessage());
        }
    }

    /** Cierra los archivos de log y errores. */
    public void close() {
        try {
            if (logWriter != null) {
                logWriter.close();
            }
            if (errorWriter != null) {
                errorWriter.close();
            }
        } catch (IOException e) {
            Simulador.logger.logError("Error al cerrar los archivos de log: " + e.getMessage());
        }
    }
}
