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



    /**
     * Registra el inicio de la simulación con el nombre de la partida.
     *
     * @param nombrePartida Nombre de la partida.
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

    void transcribirnextDay(int dia, int pecesDeRio, int pecesDeMar, int totalMonedasGanadas, int monedas){
        transcribir("Fin del día " + dia + ".");
        transcribir("Peces actuales: " + pecesDeRio + " de río y " + pecesDeMar + " de mar.");
        transcribir(totalMonedasGanadas + " monedas ganadas por un total de " + monedas + ".");
        transcribir("-------------------------" + "\n>>> Inicio del día " + (dia + 1) + ".");
    }

    void transcribirAddFoodPiscifactoria(int cantidadComida, String tipoComida, int costo, String piscifactoria) {
        transcribir(cantidadComida + " de comida de tipo " + tipoComida + " comprada por " + costo + " monedas. Se almacena en la piscifactoria " + piscifactoria + ".");   
    }

    void transcribirAddFoodAlmacenCentral(int cantidadComida, String tipoComida, int costo) {
        transcribir(cantidadComida + " de comida de tipo " + tipoComida + " comprada por " + costo + " monedas. Se almacena en el almacén central.");
    }

    void transcribirAddFish(String pezSeleccionadonombre, Boolean pezSeleccionadoSexo, int coste, int numeroTanque, String nombrePiscifactoria){
        transcribir(pezSeleccionadonombre + (pezSeleccionadoSexo ? " (M)" : " (H)") + " comprado por " 
            + coste + " monedas. Añadido al tanque " + numeroTanque 
            + " de la piscifactoria " + nombrePiscifactoria);
    }

    /**
     * Registra la limpieza de un tanque en una piscifactoría.
     *
     * @param tanque Número del tanque limpiado.
     * @param piscifactoria Nombre de la piscifactoría.
     */
    void transcribirLimpiarTanque(int tanque, String piscifactoria) {
        transcribir("Limpiado el tanque " + tanque + " de la piscifactoría " + piscifactoria + ".");
    }

    /**
     * Registra el vaciado de un tanque en una piscifactoría.
     *
     * @param tanque Número del tanque vaciado.
     * @param piscifactoria Nombre de la piscifactoría.
     */
    void transcribirVaciarTanque(int tanque, String piscifactoria) {
        transcribir("Vaciado el tanque " + tanque + " de la piscifactoría " + piscifactoria + ".");
    }

    void transcribirCompraEdificios(){
        transcribir("Comprado el almacén central.");

    }

    void transcribirAddPiscifactoria(String tipoPiscifactoria, String nombrePiscifactoria, int costoPiscifactoría){
        transcribir("Comprada la piscifactoria de " + tipoPiscifactoria + nombrePiscifactoria + " por " + costoPiscifactoría + " monedas.");
    }

    /**
     * Registra la adición de peces mediante la opción oculta a una piscifactoría.
     *
     * @param nombrePiscifactoria Nombre de la piscifactoría a la que se añaden los peces.
     */
    void transcribirOpcionOcultaPeces(String nombrePiscifactoria) {
        transcribir("Añadidos peces mediante la opción oculta a la piscifactoría " + nombrePiscifactoria + ".");
    }

    /**
     * Registra la adición de monedas mediante la opción oculta.
     */
    void transcribirOpcionOcultaMonedas(int monedas) {
        transcribir("Añadidas 1000 monedas mediante la opción oculta. Monedas Actuales " + monedas);
    }
    
}
