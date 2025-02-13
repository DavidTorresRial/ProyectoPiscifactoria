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
     * Registra y transcribe el inicio de la partida.
     * @param nombrePartida Nombre de la partida.
     * @param monedas Cantidad de monedas iniciales.
     * @param nombrePiscifactoria Nombre de la piscifactoría inicial.
     * @param dia Día en que inicia la simulación.
     */
    public void registroInicioPartida (String nombrePartida, int monedas, String nombrePiscifactoria, int dia) {
        logger.logInicioPartida(nombrePartida, nombrePiscifactoria);
        transcriptor.transcribirInicioPartida(nombrePartida, monedas, nombrePiscifactoria, dia);
    }

    /**
     * Registra y transcribe la compra de comida para una piscifactoría.
     * @param cantidadComida Cantidad de comida comprada.
     * @param tipoComida Tipo de comida comprada.
     * @param costoComida Costo en monedas.
     * @param nombrePiscifactoria Nombre de la piscifactoría destino.
     */
    public void registroComprarComidaPiscifactoria(int cantidadComida, String tipoComida, int costoComida, String nombrePiscifactoria) {
        logger.logComprarComidaPiscifactoria(cantidadComida, tipoComida, nombrePiscifactoria);
        transcriptor.transcribirComprarComidaPiscifactoria(cantidadComida, tipoComida, costoComida, nombrePiscifactoria);
    }

    /**
     * Registra y transcribe la compra de comida para el almacén central.
     * @param cantidadComida Cantidad de comida comprada.
     * @param tipoComida Tipo de comida comprada.
     * @param costoComida Costo en monedas.
     */
    public void registroComprarComidaAlmacenCentral(int cantidadComida, String tipoComida, int costoComida) {
        logger.logComprarComidaAlmacenCentral(cantidadComida, tipoComida);
        transcriptor.transcribirComprarComidaAlmacenCentral(cantidadComida, tipoComida, costoComida);
    }

    /**
     * Registra y transcribe la compra de peces y su ubicación.
     * @param nombrePez Nombre del pez comprado.
     * @param sexoPez Sexo del pez (true = macho, false = hembra).
     * @param costePez Costo del pez en monedas.
     * @param numeroTanque Número del tanque donde se añade.
     * @param nombrePiscifactoria Nombre de la piscifactoría destino.
     */
    public void registroComprarPeces(String nombrePez, Boolean sexoPez, int costePez, int numeroTanque, String nombrePiscifactoria) {
        logger.logComprarPeces(nombrePez, sexoPez, numeroTanque, nombrePiscifactoria);
        transcriptor.transcribirComprarPeces(nombrePez, sexoPez, costePez, numeroTanque, nombrePiscifactoria);
    }

    /**
     * Registra y transcribe la venta de peces y las monedas obtenidas.
     * @param numeroPecesVendidos Cantidad de peces vendidos.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     * @param monedasGanadas Cantidad de monedas ganadas por la venta.
     */
    public void registroVenderPeces(int numeroPecesVendidos, String nombrePiscifactoria, int monedasGanadas) {
        logger.logVenderPeces(numeroPecesVendidos, nombrePiscifactoria);
        transcriptor.transcribirVenderPeces(numeroPecesVendidos, nombrePiscifactoria, monedasGanadas);
    }

    /**
     * Registra y transcribe la limpieza de un tanque.
     * @param numeroTanque Número del tanque limpiado.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     */
    public void registroLimpiarTanque(int numeroTanque, String nombrePiscifactoria) {
        logger.logLimpiarTanque(numeroTanque, nombrePiscifactoria);
        transcriptor.transcribirLimpiarTanque(numeroTanque, nombrePiscifactoria);
    }

    /**
     * Registra y transcribe el vaciado de un tanque.
     * @param numeroTanque Número del tanque vaciado.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     */
    public void registroVaciarTanque(int numeroTanque, String nombrePiscifactoria) {
        logger.logVaciarTanque(numeroTanque, nombrePiscifactoria);
        transcriptor.transcribirVaciarTanque(numeroTanque, nombrePiscifactoria);
    }

    /**
     * Registra y transcribe la compra de una piscifactoría.
     * @param tipoPiscifactoria Tipo de piscifactoría comprada.
     * @param nombrePiscifactoria Nombre de la nueva piscifactoría.
     * @param costePiscifactoria Costo en monedas de la compra.
     */
    public void registroComprarPiscifactoria(String tipoPiscifactoria, String nombrePiscifactoria, int costePiscifactoria) {
        logger.logComprarPiscifactoria(tipoPiscifactoria, nombrePiscifactoria);
        transcriptor.transcribirComprarPiscifactoria(tipoPiscifactoria, nombrePiscifactoria, costePiscifactoria);
    }

    /**
     * Registra y transcribe la compra de un tanque adicional.
     * @param numeroTanque Número del tanque comprado.
     * @param nombrePiscifactoria Nombre de la piscifactoría donde se añade.
     */
    public void registroComprarTanque(int numeroTanque, String nombrePiscifactoria) {
        logger.logComprarTanque(nombrePiscifactoria);
        transcriptor.transcribirComprarTanque(numeroTanque, nombrePiscifactoria);
    }

    /** Registra y transcribe la compra del almacén central. */
    public void registroComprarAlmacenCentral() {
        logger.logComprarAlmacenCentral();
        transcriptor.transcribirComprarAlmacenCentral();
    }

    /**
     * Registra y transcribe la mejora de una piscifactoría.
     * @param nombrePiscifactoria Nombre de la piscifactoría mejorada.
     * @param capacidadComida Nueva capacidad total de comida.
     * @param costoMejoraPiscifactoria Costo de la mejora en monedas.
     */
    public void registroMejorarPiscifactoria (String nombrePiscifactoria, int capacidadComida, int costoMejoraPiscifactoria){
        logger.logMejorarPiscifactoria(nombrePiscifactoria);
        transcriptor.transcribirMejorarPiscifactoria(nombrePiscifactoria, capacidadComida, costoMejoraPiscifactoria);
    }

    /**
     * Registra y transcribe la mejora del almacén central.
     * @param incrementoCapacidad Incremento de la capacidad de comida.
     * @param capacidadComida Nueva capacidad total de comida.
     * @param costoMejoraAlmacenCentral Costo de la mejora en monedas.
     */
    public void registroMejorarAlmacenCentral(int incrementoCapacidad, int capacidadComida, int costoMejoraAlmacenCentral) {
        logger.logMejorarAlmacenCentral(incrementoCapacidad, capacidadComida);
        transcriptor.transcribirMejorarAlmacenCentral(incrementoCapacidad, capacidadComida, costoMejoraAlmacenCentral);
    }

    /**
     * Registra y transcribe el final del día con detalles financieros.
     * @param dia Día finalizado.
     * @param pecesDeRio Cantidad de peces de río.
     * @param pecesDeMar Cantidad de peces de mar.
     * @param totalMonedasGanadas Total de monedas ganadas en el día.
     * @param monedasActuales Monedas actuales después del día.
     */
    public void registroFinDelDia(int dia, int pecesDeRio, int pecesDeMar, int totalMonedasGanadas, int monedasActuales) {
        logger.logFinDelDia(dia);
        transcriptor.transcribirFinDelDia(dia, pecesDeRio, pecesDeMar, totalMonedasGanadas, monedasActuales);
    }

    /**
     * Registra y transcribe la adición de peces mediante una opción oculta.
     * @param nombrePiscifactoria Nombre de la piscifactoría afectada.
     */
    public void registroOpcionOcultaPeces(String nombrePiscifactoria) {
        logger.logOpcionOcultaPeces(nombrePiscifactoria);
        transcriptor.transcribirOpcionOcultaPeces(nombrePiscifactoria);
    }

    /**
     * Registra y transcribe la adición de monedas mediante una opción oculta.
     * @param monedasActuales Cantidad total de monedas tras la adición.
     */
    public void registroOpcionOcultaMonedas(int monedasActuales) {
        logger.logOpcionOcultaMonedas();
        transcriptor.transcribirOpcionOcultaMonedas(monedasActuales);
    }

    /**
     * Registra y transcribe la creación de una recompensa.
     * @param nombreRecompensa Nombre de la recompensa creada.
     */
    public void registroCrearRecompensa(String nombreRecompensa) {
        logger.logCrearRecompensa();
        transcriptor.transcribirCrearRecompensa(nombreRecompensa);
    }

    /**
     * Registra y transcribe el uso de una recompensa.
     * @param nombreRecompensa Nombre de la recompensa utilizada.
     */
    public void registroUsarRecompensa(String nombreRecompensa) {
        logger.logUsarRecompensa(nombreRecompensa);
        transcriptor.transcribirUsarRecompensa(nombreRecompensa);
    }

    /**
     * Registra y transcribe la generación de un pedido.
     * @param referenciaPedido Referencia única del pedido generado.
     */
    public void registroGenerarPedidos(String referenciaPedido) {
        logger.logGenerarPedidos(referenciaPedido);
        transcriptor.transcribirGenerarPedidos(referenciaPedido);
    }

    /**
     * Registra y transcribe el envío de un pedido.
     * @param nombrePez Nombre del pez enviado.
     * @param referenciaPedido Referencia única del pedido enviado.
     */
    public void registroPedidoEnviado(String nombrePez, String referenciaPedido) {
        logger.logPedidoEnviado(nombrePez, referenciaPedido);
        transcriptor.transcribirPedidoEnviado(nombrePez, referenciaPedido);
    }

    /**
     * Registra y transcribe el envío de una cantidad de peces de un tipo determinado con una referencia de pedido.
     * @param cantidadPeces Cantidad de peces enviados.
     * @param nombrePez Nombre del pez enviado.
     * @param referenciaPedido Referencia única del pedido enviado.
     */
    public void registroEnviadosConReferencia(int cantidadPeces, String nombrePez, String referenciaPedido) {
        logger.logEnviadosConReferencia(cantidadPeces, nombrePez, referenciaPedido);
        transcriptor.transcribirEnviadosConReferencia(cantidadPeces, nombrePez, referenciaPedido);
    }

    /** Registra la compra de una granja de fitoplancton. */
    public void registroCompradaGranjaFitoplancton() {
        logger.logCompraGranjaFitoplancton();
        transcriptor.transcribirCompraGranjaFitoplancton();
    }

    /** Registra la compra de una granja de langostinos. */
    public void registroCompradaGranjaLangostinos() {
        logger.logCompraGranjaLangostinos();
        transcriptor.transcribirCompraGranjaLangostinos();
    }

    /**
     * Registra la mejora de la granja de fitoplancton añadiendo un nuevo tanque.
     * 
     * @param totalTanques Número total de tanques después de la mejora.
     */
    public void registroMejoradaGranjaFitoplancton(int totalTanques) {
        logger.logMejoraGranjaFitoplancton(totalTanques);
        transcriptor.transcribirMejoraGranjaFitoplancton(totalTanques);
    }

    /**
     * Registra la mejora de la granja de langostinos añadiendo un nuevo tanque.
     * 
     * @param totalTanques Número total de tanques después de la mejora.
     */
    public void registroMejoradaGranjaLangostinos(int totalTanques) {
        logger.logMejoraGranjaLangostinos(totalTanques);
        transcriptor.transcribirMejoraGranjaLangostinos(totalTanques);
    }

    /* ↓↓↓ Métodos que no tienen trancripción. ↓↓↓ */

    /**
     * Registra un mensaje de error en el sistema de logs.
     * @param message Mensaje de error a registrar.
     */
    public void registroLogError(String message) {
        logger.logError(message);
    }

    /** Registra el cierre de la partida en el sistema de logs. */
    public void registroCierrePartida() {
        logger.logCierrePartida();
    }

    /** Registra la acción de guardar el estado actual del sistema. */
    public void registroGuardarSistema() {
        logger.logGuardarSistema();
    }

    /** Registra la acción de cargar un estado previamente guardado del sistema. */
    public void registroCargarSistema() {
        logger.logCargarSistema();
    }

    /** Cierra el sistema de logs de errores, liberando los recursos utilizados. */
     public void closeLogError() {
        logger.close();
    }
}