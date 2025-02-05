package registros;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import propiedades.AlmacenPropiedades;

/** Clase para gestionar transcripciones detalladas de acciones en el sistema. */
class Transcriptor {

    /** Instancia única de la clase Transcriptor. */
    private static Transcriptor instance;

    /** Nombre del archivo de transcripción. */
    private String transcripcionPartidaPath;

    /**
     * Constructor privado para inicializar el archivo de transcripción.
     * 
     * @param nombrePartida El nombre de la partida que se usará como nombre del archivo.
     */
    private Transcriptor(String nombrePartida) {
        this.transcripcionPartidaPath = "transcripciones/" + nombrePartida + ".tr";
    }

    /**
     * Obtiene la instancia única del transcriptor.
     * 
     * @param nombrePartida El nombre de la partida para el archivo de transcripción.
     * @return La instancia única del Transcriptor.
     */
    public static Transcriptor getInstance(String nombrePartida) {
        if (instance == null) {
            instance = new Transcriptor(nombrePartida);
        }
        return instance;
    }

    /**
     * Registra una acción detallada en la transcripción.
     * 
     * @param mensaje el mensaje que será registrado en el archivo de transcripción.
     */
    public void transcribir(String mensaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(transcripcionPartidaPath, true))) {
            writer.write(mensaje);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en la transcripción: " + e.getMessage());
        }
    }

    /**
     * Transcribe el inicio de la partida con detalles iniciales.
     * @param nombrePartida Nombre de la partida.
     * @param monedas Cantidad de monedas iniciales.
     * @param nombrePiscifactoria Nombre de la piscifactoría inicial.
     * @param dia Día en que inicia la simulación.
     */
    void transcribirInicioPartida(String nombrePartida, int monedas, String nombrePiscifactoria, int dia) {
        transcribir("Inicio de la simulación " + nombrePartida + ".");
        transcribir("Dinero inicial: " + monedas + " monedas.");
        transcribir("\n========= Peces =========\n" +
                "Rio:\n" +
                "  -" + AlmacenPropiedades.CARPA_PLATEADA.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.PEJERREY.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.PERCA_EUROPEA.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.SALMON_CHINOOK.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.TILAPIA_NILO.getNombre() + "\n" +
                "\nMar:\n" +
                "  -" + AlmacenPropiedades.ARENQUE_ATLANTICO.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.BESUGO.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.LENGUADO_EUROPEO.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.LUBINA_RAYADA.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.ROBALO.getNombre() + "\n" +
                "\nDoble:\n" +
                "  -" + AlmacenPropiedades.DORADA.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.SALMON_ATLANTICO.getNombre() + "\n" +
                "  -" + AlmacenPropiedades.TRUCHA_ARCOIRIS.getNombre());
        transcribir("-------------------------" + "\n>>> Inicio del día " + (dia + 1) + ".");
        transcribir("Piscifactoría inicial: " + nombrePiscifactoria + ".");
    }

    /**
     * Transcribe la compra de comida en una piscifactoría.
     * @param cantidadComida Cantidad de comida comprada.
     * @param tipoComida Tipo de comida comprada.
     * @param costoComida Costo en monedas.
     * @param nombrePiscifactoria Nombre de la piscifactoría destino.
     */
    void transcribirComprarComidaPiscifactoria(int cantidadComida, String tipoComida, int costoComida, String nombrePiscifactoria) {
        transcribir(cantidadComida + " de comida de tipo " + tipoComida + " comprada por " + costoComida + " monedas. Se almacena en la piscifactoria " + nombrePiscifactoria + ".");   
    }

    /**
     * Transcribe la compra de comida para el almacén central.
     * @param cantidadComida Cantidad de comida comprada.
     * @param tipoComida Tipo de comida comprada.
     * @param costoComida Costo en monedas.
     */
    void transcribirComprarComidaAlmacenCentral(int cantidadComida, String tipoComida, int costoComida) {
        transcribir(cantidadComida + " de comida de tipo " + tipoComida + " comprada por " + costoComida + " monedas. Se almacena en el almacén central.");
    }

    /**
     * Transcribe la compra de peces y su ubicación.
     * @param nombrePez Nombre del pez comprado.
     * @param sexoPez Sexo del pez (true = macho, false = hembra).
     * @param costePez Costo del pez en monedas.
     * @param numeroTanque Número del tanque donde se añade.
     * @param nombrePiscifactoria Nombre de la piscifactoría destino.
     */
    void transcribirComprarPeces(String nombrePez, Boolean sexoPez, int costePez, int numeroTanque, String nombrePiscifactoria){
        transcribir(nombrePez + (sexoPez ? " (M)" : " (H)") + " comprado por " + costePez + " monedas. Añadido al tanque " + numeroTanque 
            + " de la piscifactoria " + nombrePiscifactoria);
    }

    /**
     * Transcribe la venta de peces y las monedas obtenidas.
     * @param numeroPecesVendidos Cantidad de peces vendidos.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     * @param monedasGanadas Cantidad de monedas ganadas por la venta.
     */
    void transcribirVenderPeces(int numeroPecesVendidos, String nombrePiscifactoria, int monedasGanadas){
        transcribir("Vendidos " + numeroPecesVendidos + " peces de la piscifactoría " + nombrePiscifactoria + " de forma manual por " + monedasGanadas + " monedas.");
    }

    /**
     * Transcribe la limpieza de un tanque.
     * @param numeroTanque Número del tanque limpiado.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     */
    void transcribirLimpiarTanque(int numeroTanque, String nombrePiscifactoria) {
        transcribir("Limpiado el tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Transcribe el vaciado de un tanque.
     * @param numeroTanque Número del tanque vaciado.
     * @param nombrePiscifactoria Nombre de la piscifactoría.
     */
    void transcribirVaciarTanque(int numeroTanque, String nombrePiscifactoria) {
        transcribir("Vaciado el tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Transcribe la compra de una piscifactoría.
     * @param tipoPiscifactoria Tipo de piscifactoría comprada.
     * @param nombrePiscifactoria Nombre de la nueva piscifactoría.
     * @param costoPiscifactoria Costo en monedas de la compra.
     */
    void transcribirComprarPiscifactoria(String tipoPiscifactoria, String nombrePiscifactoria, int costoPiscifactoria){
        transcribir("Comprada la piscifactoria de " + tipoPiscifactoria + nombrePiscifactoria + " por " + costoPiscifactoria + " monedas.");
    }

    /**
     * Transcribe la compra de un tanque adicional.
     * @param numeroTanque Número del tanque comprado.
     * @param nombrePiscifactoria Nombre de la piscifactoría donde se añade.
     */
    void transcribirComprarTanque(int numeroTanque, String nombrePiscifactoria){
        transcribir("Comprado un tanque número " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Transcribe la compra del almacén central.
     */
    void transcribirComprarAlmacenCentral(){
        transcribir("Comprado el almacén central.");
    }

    /**
     * Transcribe la mejora de una piscifactoría.
     * @param nombrePiscifactoria Nombre de la piscifactoría mejorada.
     * @param capacidadComida Nueva capacidad total de comida.
     * @param costoMejoraPiscifactoria Costo de la mejora en monedas.
     */
    void transcribirMejorarPiscifactoria(String nombrePiscifactoria, int capacidadComida, int costoMejoraPiscifactoria){
        transcribir("Mejorada la piscifactoría " + nombrePiscifactoria + " aumentando su capacidad de comida hasta un total de " + capacidadComida + " por " + costoMejoraPiscifactoria + " monedas.");
    }

    /**
     * Transcribe la mejora del almacén central.
     * @param incrementoCapacidad Incremento de la capacidad de comida.
     * @param capacidadComida Nueva capacidad total de comida.
     * @param costoMejoraAlmacenCentral Costo de la mejora en monedas.
     */
    void transcribirMejorarAlmacenCentral(int incrementoCapacidad, int capacidadComida, int costoMejoraAlmacenCentral){
        transcribir("Mejorando el almacén central, aumentando su capacidad de comida en " + incrementoCapacidad + " unidades hasta " + capacidadComida + " por " + costoMejoraAlmacenCentral + " monedas.");
    }

    /**
     * Transcribe el final del día con detalles financieros.
     * @param dia Día finalizado.
     * @param pecesDeRio Cantidad de peces de río.
     * @param pecesDeMar Cantidad de peces de mar.
     * @param totalMonedasGanadas Total de monedas ganadas en el día.
     * @param monedasActuales Monedas actuales después del día.
     */
    void transcribirFinDelDia(int dia, int pecesDeRio, int pecesDeMar, int totalMonedasGanadas, int monedasActuales){
        transcribir("Fin del día " + dia + ".");
        transcribir("Peces actuales: " + pecesDeRio + " de río y " + pecesDeMar + " de mar.");
        transcribir(totalMonedasGanadas + " monedas ganadas por un total de " + monedasActuales + ".");
        transcribir("-------------------------" + "\n>>> Inicio del día " + (dia + 1) + ".");
    }

    /**
     * Transcribe la adición de peces mediante una opción oculta.
     * @param nombrePiscifactoria Nombre de la piscifactoría afectada.
     */
    void transcribirOpcionOcultaPeces(String nombrePiscifactoria) {
        transcribir("Añadidos peces mediante la opción oculta a la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Transcribe la adición de monedas mediante una opción oculta.
     * @param monedasActuales Cantidad total de monedas tras la adición.
     */
    void transcribirOpcionOcultaMonedas(int monedasActuales) {
        transcribir("Añadidas 1000 monedas mediante la opción oculta. Monedas Actuales " + monedasActuales);
    }

    /**
     * Transcribe la creación de una recompensa.
     * @param nombreRecompensa Nombre de la recompensa creada.
     */
    void transcribirCrearRecompensa(String nombreRecompensa) {
        transcribir("Recompensa " + nombreRecompensa + " creada.");
    }

    /**
     * Transcribe el uso de una recompensa.
     * @param nombreRecompensa Nombre de la recompensa utilizada.
     */
    void transcribirUsarRecompensa(String nombreRecompensa) {
        transcribir("Recompensa " + nombreRecompensa + " usada.");
    }
}