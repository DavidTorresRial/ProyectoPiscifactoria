package registros;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;

/** Clase para manejar el registro de logs en un archivo. */
class Logger {

    /** Ruta del archivo de log de errores general. */
    private final String ERROR_LOG_PATH = "logs/0_errors.log";

    /** Escritor persistente para el log general de errores. */
    private BufferedWriter errorWriter;

    /** Instancia única de la clase Logger. */
    private static Logger instance;

    /** Ruta del archivo de log específico de la partida. */
    private String logPath;

    /**
     * Constructor privado para inicializar el log general y de la partida.
     *
     * @param nombrePartida Nombre de la partida para generar la ruta del log de la partida.
     */
    private Logger(String nombrePartida) {
        try {
            errorWriter = new BufferedWriter(new FileWriter(ERROR_LOG_PATH, true));
            this.logPath = "logs/" + nombrePartida + ".log";
            
        } catch (IOException e) {
            System.err.println("No se pudo iniciar el Logger: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única del Logger.
     *
     * @param nombrePartida Nombre de la partida para generar la ruta del log de la partida.
     * @return La instancia única del Logger.
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
     * @param message El mensaje que será registrado en el archivo de logs.
     */
    void logError(String message) {
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
     * @param message El mensaje que será registrado en el archivo de logs.
     */
    void log(String message) {
        File logFile = new File(logPath);
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
    void close() {
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
     * Registra el inicio de la partida y la piscifactoría inicial.
     * @param nombrePartida Nombre de la partida.
     * @param nombrePiscifactoria Nombre de la piscifactoría inicial.
     */
    void logInicioPartida(String nombrePartida, String nombrePiscifactoria) {
        log("Inicio de la simulación " + nombrePartida + ".");
        log("Piscifactoría inicial: " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la compra de comida para una piscifactoría.
     * @param cantidadComida Cantidad de comida comprada.
     * @param tipoComida Tipo de comida comprada.
     * @param nombrePiscifactoria Nombre de la piscifactoría donde se almacena.
     */
    void logComprarComidaPiscifactoria(int cantidadComida, String tipoComida, String nombrePiscifactoria) {
        log(cantidadComida + " de comida de tipo " + tipoComida + " comprada. Se almacena en  en la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la compra de comida para el almacén central.
     * @param cantidadComida Cantidad de comida comprada.
     * @param tipoComida Tipo de comida comprada.
     */
    void logComprarComidaAlmacenCentral(int cantidadComida, String tipoComida) {
        log(cantidadComida + " de comida de tipo " + tipoComida + " comprada. Se almacena en el almacén central.");
    }

    /**
     * Registra la compra de peces y su ubicación.
     * @param nombrePez Nombre del pez.
     * @param sexoPez Sexo del pez (true = macho, false = hembra).
     * @param numeroTanque Número del tanque donde se añade.
     * @param nombrePiscifactoria Nombre de la piscifactoría destino.
     */
    void logComprarPeces(String nombrePez, Boolean sexoPez, int numeroTanque, String nombrePiscifactoria) {
        log(nombrePez + " (" + (sexoPez ? "M" : "H") + ") comprado. Añadido al tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la venta de peces desde una piscifactoría.
     * @param numeroPecesVendidos Cantidad de peces vendidos.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     */
    void logVenderPeces(int numeroPecesVendidos, String nombrePiscifactoria){
        log("Vendidos " + numeroPecesVendidos + " peces de la piscifactoría " + nombrePiscifactoria + " de forma manual.");
    }

    /**
     * Registra la limpieza de un tanque.
     * @param numeroTanque Número del tanque limpiado.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     */
    void logLimpiarTanque(int numeroTanque, String nombrePiscifactoria) {
        log("Limpiado el tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Registra el vaciado de un tanque.
     * @param numeroTanque Número del tanque vaciado.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     */
    void logVaciarTanque(int numeroTanque, String nombrePiscifactoria) {
        log("Vaciado el tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la compra de una nueva piscifactoría.
     * @param tipoPiscifactoria Tipo de piscifactoría comprada.
     * @param nombrePiscifactoria Nombre de la nueva piscifactoría.
     */
    void logComprarPiscifactoria(String tipoPiscifactoria, String nombrePiscifactoria) {
        log("Comprada la piscifactoría de " + tipoPiscifactoria + " " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la compra de un tanque adicional.
     * @param nombrePiscifactoria Nombre de la piscifactoría donde se añade el tanque.
     */
    void logComprarTanque(String nombrePiscifactoria) {
        log("Comprado un tanque para la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la compra del almacén central.
     */
    void logComprarAlmacenCentral() {
        log("Comprado el almacén central.");
    }

    /**
     * Registra la mejora de una piscifactoría.
     * @param nombrePiscifactoria Nombre de la piscifactoría mejorada.
     */
    void logMejorarPiscifactoria(String nombrePiscifactoria) {
        log("Mejorada la piscifactoría " + nombrePiscifactoria + " aumentando su capacidad de comida.");
    }

    /**
     * Registra la mejora del almacén central.
     * @param incrementoCapacidad Incremento de la capacidad de comida.
     * @param capacidadComida Nueva capacidad total de comida.
     */
    void logMejorarAlmacenCentral(int incrementoCapacidad, int capacidadComida) {
        log("Mejorando el almacén central, aumentando su capacidad de comida en " + incrementoCapacidad + " unidades hasta " + capacidadComida + ".");
    }

    /**
     * Registra el final del día en la simulación.
     * @param dia Número del día finalizado.
     */
    void logFinDelDia(int dia) {
        log("Fin del día " + dia + ".");
    }

    /**
     * Registra la adición de peces mediante una opción oculta.
     * @param nombrePiscifactoria Nombre de la piscifactoría afectada.
     */
    void logOpcionOcultaPeces(String nombrePiscifactoria) {
        log("Añadidos peces mediante la opción oculta a la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la adición de monedas mediante una opción oculta.
     */
    void logOpcionOcultaMonedas() {
        log("Añadidas monedas mediante la opción oculta.");
    }

    /**
     * Registra el cierre de la partida.
     */
    void logCierrePartida() {
        log("Cierre de la partida.");
    }

    /**
     * Registra la creación de una recompensa.
     */
    void logCrearRecompensa() {
        log("Recompensa creada.");
    }

    /**
     * Registra el uso de una recompensa.
     * @param nombreRecompensa Nombre de la recompensa utilizada.
     */
    void logUsarRecompensa(String nombreRecompensa) {
        log("Recompensa " + nombreRecompensa + " usada.");
    }

    /**
     * Registra la acción de guardar el sistema.
     */
    void logGuardarSistema() {
        log("Sistema guardado.");
    }

    /**
     * Registra la acción de cargar el sistema.
     */
    void logCargarSistema() {
        log("Sistema cargado.");
    }

    /**
     * Registra la generación de un pedido.
     * @param referenciaPedido Referencia del pedido generado.
     */
    void logGenerarPedidos(String referenciaPedido) {
        log("Generando el pedido con referencia " + referenciaPedido + ".");
    }

    /**
     * Registra el envío de un pedido.
     * @param nombre Nombre del cliente que realizó el pedido.
     * @param referenciaPedido Referencia del pedido enviado.
     */
    void logPedidoEnviado(String nombrePez, String referenciaPedido) {
        log("Pedido de " + nombrePez + " con referencia " + referenciaPedido + " enviado.");
    }

    /**
     * Registra la cantidad de peces enviados a un pedido con su referencia.
     * @param cantidadPeces Cantidad de peces enviados.
     * @param nombrePez Nombre del pez enviado.
     * @param referenciaPedido Referencia del pedido al que se envían los peces.
     */
    void logEnviadosConReferencia(int cantidadPeces, String nombrePez, String referenciaPedido) {
        log("Enviados " + cantidadPeces + " peces al pedido de " + nombrePez + " con referencia " + referenciaPedido + ".");
    }

    /** Registra la compra de una granja de fitoplancton. */
    void logCompraGranjaFitoplancton() {
        log("Comprada la granja de fitoplancton.");
    }

    /** Registra la compra de una granja de langostinos. */
    void logCompraGranjaLangostinos() {
        log("Comprada la granja de langostinos.");
    }

    /**
     * Registra la mejora de la granja de fitoplancton añadiendo un nuevo tanque.
     * 
     * @param totalTanques Número total de tanques después de la mejora.
     */
    void logMejoraGranjaFitoplancton(int totalTanques) {
        log("Mejorada la granja de fitoplancton añadiendo un tanque por un total de " + totalTanques + " tanques.");
    }

    /**
     * Registra la mejora de la granja de langostinos añadiendo un nuevo tanque.
     * 
     * @param totalTanques Número total de tanques después de la mejora.
     */
    void logMejoraGranjaLangostinos(int totalTanques) {
        log("Mejorada la granja de langostinos añadiendo un tanque por un total de " + totalTanques + " tanques.");
    }
}