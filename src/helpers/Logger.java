package helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Clase para manejar el registro de logs en un archivo. */
public class Logger {

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






    /**
     * Registra el inicio de la simulación con el nombre de la partida.
     *
     * @param nombrePartida Nombre de la partida.
     */
    public void logInicioPartida(String nombrePartida) {
        logPartida("Inicio de la simulación " + nombrePartida + ".");
    }

    /**
     * Registra la configuración inicial de una piscifactoría.
     *
     * @param nombrePiscifactoria Nombre de la piscifactoría inicial.
     */
    public void logPiscifactoriaInicial(String nombrePiscifactoria) {
        logPartida("Piscifactoría inicial: " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la compra de comida con detalles de cantidad, tipo y destino.
     *
     * @param cantidad Cantidad de comida comprada.
     * @param tipo Tipo de comida comprada.
     * @param destino Lugar donde se almacena la comida.
     */
    public void logComprarComida(int cantidad, String tipo, String destino) {
        logPartida(cantidad + " de comida de tipo " + tipo + " comprada. Se almacena en " + destino + ".");
    }

    /**
     * Registra la compra de un pez y su asignación a un tanque específico.
     *
     * @param nombre Nombre del pez comprado.
     * @param sexo Sexo del pez comprado.
     * @param tanque Número del tanque donde se asigna el pez.
     * @param piscifactoria Nombre de la piscifactoría donde se asigna el pez.
     */
    public void logComprarPeces(String nombre, char sexo, int tanque, String piscifactoria) {
        logPartida(nombre + " (" + sexo + ") comprado. Añadido al tanque " + tanque + " de la piscifactoría " + piscifactoria + ".");
    }

    /**
     * Registra la limpieza de un tanque en una piscifactoría.
     *
     * @param tanque Número del tanque limpiado.
     * @param piscifactoria Nombre de la piscifactoría.
     */
    public void logLimpiarTanque(int tanque, String piscifactoria) {
        logPartida("Limpiado el tanque " + tanque + " de la piscifactoría " + piscifactoria + ".");
    }

    /**
     * Registra el vaciado de un tanque en una piscifactoría.
     *
     * @param tanque Número del tanque vaciado.
     * @param piscifactoria Nombre de la piscifactoría.
     */
    public void logVaciarTanque(int tanque, String piscifactoria) {
        logPartida("Vaciado el tanque " + tanque + " de la piscifactoría " + piscifactoria + ".");
    }

    /**
     * Registra el fin de un día en la simulación.
     *
     * @param dia Número del día finalizado.
     */
    public void logFinDelDia(int dia) {
        logPartida("Fin del día " + dia + ".");
    }

    /**
     * Registra el cierre de la partida.
     */
    public void logSalirPartida() {
        logPartida("Cierre de la partida.");
    }









    //TODO SISTEMA DE COMPRAS
    /**
     * Registra la compra de una piscifactoría especificando su tipo y nombre.
     *
     * @param tipoPiscifactoria Tipo de piscifactoría (río/mar).
     * @param nombre Nombre de la piscifactoría.
     */
    public void logComprarPiscifactoria(String tipoPiscifactoria, String nombre) {
        logPartida("Comprada la piscifactoría de " + tipoPiscifactoria + " " + nombre + ".");
    }

    /**
     * Registra la compra de un tanque especificando la piscifactoría a la que pertenece.
     *
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     */
    public void logComprarTanque(String nombrePiscifactoria) {
        logPartida("Comprado un tanque para la piscifactoría " + nombrePiscifactoria + ".");
    }
    
    /**
     * Registra la compra del almacén central.
     */
    public void logComprarAlmacenCentral() {
        logPartida("Comprado el almacén central.");
    }











    //TODO SISTEMA DE MEJORAS
    /**
     * Registra la mejora de una piscifactoría, especificando su nombre y la mejora realizada.
     *
     * @param nombrePiscifactoria Nombre de la piscifactoría mejorada.
     */
    public void logMejorarPiscifactoria(String nombrePiscifactoria) {
        logPartida("Mejorada la piscifactoría " + nombrePiscifactoria + " aumentando su capacidad de comida.");
    }

    /**
     * Registra la mejora del almacén central especificando el incremento y la nueva capacidad.
     *
     * @param incrementoCapacidad Incremento en la capacidad del almacén.
     * @param nuevaCapacidad Capacidad total del almacén después de la mejora.
     */
    public void logMejorarAlmacenCentral(int incrementoCapacidad, int nuevaCapacidad) {
        logPartida("Mejorando el almacén central, aumentando su capacidad de comida en " 
            + incrementoCapacidad + " unidades hasta " + nuevaCapacidad + ".");
    }











    //TODO SISTEMA DE RECOMPENSAS
    /** Registra la creación de una recompensa por parte de una entidad. */
    public void logCrearRecompensa() {
        logPartida("Recompensa creada.");
    }

    /**
     * Registra el uso de una recompensa especificando su nombre.
     *
     * @param nombreRecompensa Nombre de la recompensa utilizada.
     */
    public void logUsarRecompensa(String nombreRecompensa) {
        logPartida("Recompensa " + nombreRecompensa + " usada.");
    }









    //TODO SISTEMA DE GUARDADO Y CARGA
    /**
     * Registra el evento de guardar el estado del sistema.
     */
    public void logSistemaGuardado() {
        logPartida("Sistema guardado correctamente.");
    }

    /**
     * Registra el evento de cargar el estado del sistema.
     */
    public void logSistemaCargado() {
        logPartida("Sistema cargado correctamente.");
    }







    //TODO OPCIONES OCULTAS
    /**
     * Registra la adición de peces mediante la opción oculta a una piscifactoría.
     *
     * @param nombrePiscifactoria Nombre de la piscifactoría a la que se añaden los peces.
     */
    public void logOpcionOcultaPeces(String nombrePiscifactoria) {
        logPartida("Añadidos peces mediante la opción oculta a la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la adición de monedas mediante la opción oculta.
     */
    public void logOpcionOcultaMonedas() {
        logPartida("Añadidas monedas mediante la opción oculta.");
    }
}