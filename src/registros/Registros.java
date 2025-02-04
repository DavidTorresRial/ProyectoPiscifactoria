package registros;

/** Clase para manejar los registros de la partida. */
public class Registros {
    
    /**  Logger para gestionar los registros de eventos y errores del sistema. */
    public Logger logger;

    /**  Transcriptor para registrar los eventos de las piscifactorías en archivos. */
    public Transcriptor transcriptor;

    /**
     * Constructor para inicializar el registro de la partida.
     *
     * @param nombrePartida Nombre de la partida para generar la ruta del log y del transcriptor de la partida.
     */
    public Registros(String nombrePartida) {
        logger = Logger.getInstance(nombrePartida);
        transcriptor = Transcriptor.getInstance(nombrePartida);
    }
    
    /**
     * Registra el inicio de la simulación con el nombre de la partida.
     *
     * @param nombrePartida Nombre de la partida.
     */
    public void registroInicioPartida (String nombrePartida, int monedas, String nombrePiscifactoria, int dia) {
        logger.logInicioPartida(nombrePartida);
        transcriptor.transcribirInicioPartida(nombrePartida, monedas, nombrePiscifactoria, dia);
    }


    public void registroAddFood(int cantidadComida, String tipoComida, int costo, String nombrePiscifactoria) {
        logger.logComprarComida(cantidadComida, tipoComida, nombrePiscifactoria);
        transcriptor.transcribirAddFoodPiscifactoria(cantidadComida, tipoComida, costo, nombrePiscifactoria);
    }
}
