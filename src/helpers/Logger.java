package helpers;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger instance;
    private static Logger errorLogger;
    private BufferedWriter logWriter;

    private Logger(String logFileName) {
        try {
            File logFile = new File("logs/" + logFileName);
            logFile.getParentFile().mkdirs(); // Crear el directorio si no existe
            logWriter = new BufferedWriter(new FileWriter(logFile, true)); // Modo append
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el Logger: " + e.getMessage());
        }
    }

    // Obtener la instancia para el log general
    public static Logger getInstance(String logFileName) {
        if (instance == null) {
            instance = new Logger(logFileName);
        }
        return instance;
    }

    // Obtener la instancia para el log de errores
    public static Logger getErrorLogger() {
        if (errorLogger == null) {
            errorLogger = new Logger("0_errors.log");
        }
        return errorLogger;
    }

    // Método para escribir en el log general
    public void log(String message) {
        writeMessage(message);
    }

    // Método para escribir en el log de errores
    public void logError(String errorMessage) {
        writeMessage("ERROR: " + errorMessage);
    }

    // Método interno para escribir mensajes con timestamp
    private void writeMessage(String message) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            logWriter.write("[" + timestamp + "] " + message);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException e) {
            System.err.println("Error al escribir en el log: " + e.getMessage());
        }
    }

    // Método para cerrar el log individual
    public void close() {
        try {
            if (logWriter != null) {
                logWriter.close();
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el log: " + e.getMessage());
        }
    }

    // Método estático para cerrar todos los logs abiertos
    public static void closeAllLogs() {
        if (instance != null) {
            instance.close();
        }
        if (errorLogger != null) {
            errorLogger.close();
        }
    }
}
