package registros;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import propiedades.AlmacenPropiedades;

/** Clase para gestionar transcripciones detalladas de acciones en el sistema. */
class Transcriptor {

    /** Instancia única de la clase Transcriptor. */
    private static Transcriptor instancia;

    /** Nombre del archivo de transcripción. */
    private String archivoTranscripcion;

    /**
     * Constructor privado para inicializar el archivo de transcripción.
     * 
     * @param nombrePartida el nombre de la partida que se usará como parte del nombre del archivo.
     */
    private Transcriptor(String nombrePartida) {
        this.archivoTranscripcion = "transcripciones/" + nombrePartida + ".tr";
    }

    /**
     * Obtiene la instancia única del transcriptor.
     * 
     * @param nombrePartida el nombre de la partida para el archivo de transcripción.
     * @return la instancia única del Transcriptor.
     */
    public static Transcriptor getInstance(String nombrePartida) {
        if (instancia == null) {
            instancia = new Transcriptor(nombrePartida);
        }
        return instancia;
    }

    /**
     * Registra una acción detallada en la transcripción.
     * 
     * @param mensaje el mensaje que será registrado en el archivo de transcripción.
     */
    public void transcribir(String mensaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTranscripcion, true))) {
            writer.write(mensaje);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en la transcripción: " + e.getMessage());
        }
    }



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

    void transcribirComprarComidaPiscifactoria(int cantidadComida, String tipoComida, int costoComida, String nombrePiscifactoria) {
        transcribir(cantidadComida + " de comida de tipo " + tipoComida + " comprada por " + costoComida + " monedas. Se almacena en la piscifactoria " + nombrePiscifactoria + ".");   
    }

    void transcribirComprarComidaAlmacenCentral(int cantidadComida, String tipoComida, int costoComida) {
        transcribir(cantidadComida + " de comida de tipo " + tipoComida + " comprada por " + costoComida + " monedas. Se almacena en el almacén central.");
    }

    void transcribirComprarPeces(String nombrePez, Boolean sexoPez, int costePez, int numeroTanque, String nombrePiscifactoria){
        transcribir(nombrePez + (sexoPez ? " (M)" : " (H)") + " comprado por " + costePez + " monedas. Añadido al tanque " + numeroTanque 
            + " de la piscifactoria " + nombrePiscifactoria);
    }

    void transcribirVenderPeces(int numeroPecesVendidos, String nombrePiscifactoria, int monedasGanadas){
        transcribir("Vendidos " + numeroPecesVendidos + " peces de la piscifactoría " + nombrePiscifactoria + " de forma manual por " + monedasGanadas + " monedas.");
    }

    void transcribirLimpiarTanque(int numeroTanque, String nombrePiscifactoria) {
        transcribir("Limpiado el tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    void transcribirVaciarTanque(int numeroTanque, String nombrePiscifactoria) {
        transcribir("Vaciado el tanque " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    void transcribirComprarPiscifactoria(String tipoPiscifactoria, String nombrePiscifactoria, int costoPiscifactoria){
        transcribir("Comprada la piscifactoria de " + tipoPiscifactoria + nombrePiscifactoria + " por " + costoPiscifactoria + " monedas.");
    }

    void transcribirComprarTanque(int numeroTanque, String nombrePiscifactoria){
        transcribir("Comprado un tanque número " + numeroTanque + " de la piscifactoría " + nombrePiscifactoria + ".");
    }

    void transcribirComprarAlmacenCentral(){
        transcribir("Comprado el almacén central.");
    }

    void transcribirMejorarPiscifactoria(String nombrePiscifactoria, int capacidadComida, int costoMejoraPiscifactoria){
        transcribir("Mejorada la piscifactoría " + nombrePiscifactoria + " aumentando su capacidad de comida hasta un total de " + capacidadComida + " por " + costoMejoraPiscifactoria + " monedas.");
    }

    void transcribirMejorarAlmacenCentral(int incrementoCapacidad, int capacidadComida, int costoMejoraAlmacenCentral){
        transcribir("Mejorando el almacén central, aumentando su capacidad de comida en " + incrementoCapacidad + " unidades hasta " + capacidadComida + " por " + costoMejoraAlmacenCentral + " monedas.");
    }

    void transcribirFinDelDia(int dia, int pecesDeRio, int pecesDeMar, int totalMonedasGanadas, int monedasActuales){
        transcribir("Fin del día " + dia + ".");
        transcribir("Peces actuales: " + pecesDeRio + " de río y " + pecesDeMar + " de mar.");
        transcribir(totalMonedasGanadas + " monedas ganadas por un total de " + monedasActuales + ".");
        transcribir("-------------------------" + "\n>>> Inicio del día " + (dia + 1) + ".");
    }

    void transcribirOpcionOcultaPeces(String nombrePiscifactoria) {
        transcribir("Añadidos peces mediante la opción oculta a la piscifactoría " + nombrePiscifactoria + ".");
    }

    void transcribirOpcionOcultaMonedas(int monedasActuales) {
        transcribir("Añadidas 1000 monedas mediante la opción oculta. Monedas Actuales " + monedasActuales);
    }

    void transcribirCrearRecompensa(String nombreRecompensa) {
        transcribir("Recompensa " + nombreRecompensa + " creada.");
    }

    void transcribirUsarRecompensa(String nombreRecompensa) {
        transcribir("Recompensa " + nombreRecompensa + " usada.");
    }
}