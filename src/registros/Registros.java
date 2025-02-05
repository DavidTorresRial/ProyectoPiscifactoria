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

    public void registroInicioPartida (String nombrePartida, int monedas, String nombrePiscifactoria, int dia) {
        logger.logInicioPartida(nombrePartida, nombrePiscifactoria);
        transcriptor.transcribirInicioPartida(nombrePartida, monedas, nombrePiscifactoria, dia);
    }

    public void registroComprarComidaPiscifactoria(int cantidadComida, String tipoComida, int costoComida, String nombrePiscifactoria) {
        logger.logComprarComidaPiscifactoria(cantidadComida, tipoComida, nombrePiscifactoria);
        transcriptor.transcribirComprarComidaPiscifactoria(cantidadComida, tipoComida, costoComida, nombrePiscifactoria);
    }

    public void registroComprarComidaAlmacenCentral(int cantidadComida, String tipoComida, int costoComida) {
        logger.logComprarComidaAlmacenCentral(cantidadComida, tipoComida);
        transcriptor.transcribirComprarComidaAlmacenCentral(cantidadComida, tipoComida, costoComida);
    }

    public void registroComprarPeces(String nombrePez, Boolean sexoPez, int costePez, int numeroTanque, String nombrePiscifactoria) {
        logger.logComprarPeces(nombrePez, sexoPez, numeroTanque, nombrePiscifactoria);
        transcriptor.transcribirComprarPeces(nombrePez, sexoPez, costePez, numeroTanque, nombrePiscifactoria);
    }

    public void registroVenderPeces(int numeroPecesVendidos, String nombrePiscifactoria, int monedasGanadas) {
        logger.logVenderPeces(numeroPecesVendidos, nombrePiscifactoria);
        transcriptor.transcribirVenderPeces(numeroPecesVendidos, nombrePiscifactoria, monedasGanadas);
    }

    public void registroLimpiarTanque(int numeroTanque, String nombrePiscifactoria) {
        logger.logLimpiarTanque(numeroTanque, nombrePiscifactoria);
        transcriptor.transcribirLimpiarTanque(numeroTanque, nombrePiscifactoria);
    }

    public void registroVaciarTanque(int numeroTanque, String nombrePiscifactoria) {
        logger.logVaciarTanque(numeroTanque, nombrePiscifactoria);
        transcriptor.transcribirVaciarTanque(numeroTanque, nombrePiscifactoria);
    }

    public void registroComprarPiscifactoria(String tipoPiscifactoria, String nombrePiscifactoria, int costePiscifactoria) {
        logger.logComprarPiscifactoria(tipoPiscifactoria, nombrePiscifactoria);
        transcriptor.transcribirComprarPiscifactoria(tipoPiscifactoria, nombrePiscifactoria, costePiscifactoria);
    }

    public void registroComprarTanque(int numeroTanque, String nombrePiscifactoria) {
        logger.logComprarTanque(nombrePiscifactoria);
        transcriptor.transcribirComprarTanque(numeroTanque, nombrePiscifactoria);
    }

    public void registroComprarAlmacenCentral() {
        logger.logComprarAlmacenCentral();
        transcriptor.transcribirComprarAlmacenCentral();
    }

    public void registroMejorarPiscifactoria (String nombrePiscifactoria, int capacidadComida, int costoMejoraPiscifactoria){
        logger.logMejorarPiscifactoria(nombrePiscifactoria);
        transcriptor.transcribirMejorarPiscifactoria(nombrePiscifactoria, capacidadComida, costoMejoraPiscifactoria);
    }

    public void registroMejorarAlmacenCentral(int incrementoCapacidad, int capacidadComida, int costoMejoraAlmacenCentral) {
        logger.logMejorarAlmacenCentral(incrementoCapacidad, capacidadComida);
        transcriptor.transcribirMejorarAlmacenCentral(incrementoCapacidad, capacidadComida, costoMejoraAlmacenCentral);
    }

    public void registroFinDelDia(int dia, int pecesDeRio, int pecesDeMar, int totalMonedasGanadas, int monedasActuales) {
        logger.logFinDelDia(dia);
        transcriptor.transcribirFinDelDia(dia, pecesDeRio, pecesDeMar, totalMonedasGanadas, monedasActuales);
    }

    public void registroOpcionOcultaPeces(String nombrePiscifactoria) {
        logger.logOpcionOcultaPeces(nombrePiscifactoria);
        transcriptor.transcribirOpcionOcultaPeces(nombrePiscifactoria);
    }

    public void registroOpcionOcultaMonedas(int monedasActuales) {
        logger.logOpcionOcultaMonedas();
        transcriptor.transcribirOpcionOcultaMonedas(monedasActuales);
    }

    public void registroCrearRecompensa(String nombreRecompensa) {
        logger.logCrearRecompensa();
        transcriptor.transcribirCrearRecompensa(nombreRecompensa);
    }

    public void registroUsarRecompensa(String nombreRecompensa) {
        logger.logUsarRecompensa(nombreRecompensa);
        transcriptor.transcribirUsarRecompensa(nombreRecompensa);
    }

    //TODO Métodos logCierrePartida, logGuardarSistema y logCargarSistema no tienen trancripción.

    public void registroLogError(String message) {
        logger.logError(message);
    }

    public void closeLogError() {
        logger.close();
    }

    public void registroGuardarSistema() {
        logger.logGuardarSistema();
    }

    public void registroCargarSistema() {
        logger.logCargarSistema();
    }
}
