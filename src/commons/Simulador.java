package commons;

import java.util.ArrayList;
import java.util.List;

import helpers.InputHelper;
import helpers.MenuHelper;

import piscifactorias.Piscifactoria;
import piscifactorias.tipos.PiscifactoriaDeRio;
import piscifactorias.tipos.PiscifactoriaDeMar;

import propiedades.AlmacenPropiedades;
import propiedades.PecesDatos;
import propiedades.PecesProps;
import estadisticas.Estadisticas;

import tanque.Tanque;

import peces.Pez;
import peces.tipos.doble.*;
import peces.tipos.mar.*;
import peces.tipos.rio.*;

public class Simulador {

    /** Días transcurridos en la simulación, comenzando en 0. */
    private int dias = 0;

    /** Lista de piscifactorías en el sistema. */
    private ArrayList<Piscifactoria> piscifacorias = new ArrayList<>();

    /** Nombre de la entidad o partida en la simulación. */
    private String nombreEntidad;

    /** Nombre de la piscifactoría. */
    private String nombrePiscifactoria;

    /**
     * Sistema de estadísticas para registrar la cría, venta y ganancias de los
     * peces.
     */
    private Estadisticas estadisticas = new Estadisticas(new String[] {
            AlmacenPropiedades.SALMON_ATLANTICO.getNombre(),
            AlmacenPropiedades.TRUCHA_ARCOIRIS.getNombre(),
            AlmacenPropiedades.ARENQUE_ATLANTICO.getNombre(),
            AlmacenPropiedades.BESUGO.getNombre(),
            AlmacenPropiedades.LENGUADO_EUROPEO.getNombre(),
            AlmacenPropiedades.LUBINA_RAYADA.getNombre(),
            AlmacenPropiedades.ROBALO.getNombre(),
            AlmacenPropiedades.CARPA_PLATEADA.getNombre(),
            AlmacenPropiedades.PEJERREY.getNombre(),
            AlmacenPropiedades.PERCA_EUROPEA.getNombre(),
            AlmacenPropiedades.SALMON_CHINOOK.getNombre(),
            AlmacenPropiedades.TILAPIA_NILO.getNombre()
    });

    /** Sistema de monedas para manejar transacciones. */
    private SistemaMonedas monedas;

    /** Almacén central de comida para abastecer las piscifactorías. */
    private AlmacenCentral almacenCentral = new AlmacenCentral();

    /** Utilidad para manejar la entrada de datos del usuario. */
    private InputHelper inputHelper = new InputHelper();

    /** Utilidad para gestionar los menús de la simulación. */
    private MenuHelper menuHelper = new MenuHelper();

    /** Metodo que inicializa todo el sistema. */
    public void init() {
        nombreEntidad = inputHelper.readString("Ingrese el nombre de la entidad/empresa/partida: ");
        System.out.println();
        nombrePiscifactoria = inputHelper.readString("Ingrese el nombre de la primera Piscifactoria: ");

        monedas = new SistemaMonedas(100000);

        // Añadimos la nueva PiscifactoriaDeRio directamente a la lista
        piscifacorias.add(new PiscifactoriaDeRio(nombrePiscifactoria, monedas));
        piscifacorias.get(0).setComidaAnimalActual(piscifacorias.get(0).getCapacidadTotal());
        piscifacorias.get(0).setComidaVegetalActual(piscifacorias.get(0).getCapacidadTotal());
    }

    /** Método que muestra el texto del menú. */
    public void menu() {
        menuHelper.clearOptions(); // Limpiar opciones previas

        menuHelper.addOption(1, "Estado general");
        menuHelper.addOption(2, "Estado piscifactoría");
        menuHelper.addOption(3, "Estado tanque");
        menuHelper.addOption(4, "Informes");
        menuHelper.addOption(5, "Icitopedia");
        menuHelper.addOption(6, "Pasar día");
        menuHelper.addOption(7, "Comprar Comida");
        menuHelper.addOption(8, "Comprar peces");
        menuHelper.addOption(9, "Vender peces");
        menuHelper.addOption(10, "Limpiar tanque");
        menuHelper.addOption(11, "Vaciar tanque");
        menuHelper.addOption(12, "Mejorar");
        menuHelper.addOption(13, "Pasar varios dias");
        menuHelper.addOption(14, "Salir");

        menuHelper.showMenu();
    }

    /** Método que muestra la lista de piscifactorías actuales en forma de menú. */
    public void menuPisc() {
        menuHelper.clearOptions(); // Limpiar opciones previas

        System.out.println("\nSeleccione una opción:");
        System.out.println("--------------- Piscifactorías ---------------");
        System.out.println("[Peces vivos / Peces totales / Espacio total]");
        System.out.println();

        for (int i = 0; i < piscifacorias.size(); i++) {
            Piscifactoria piscifactoria = piscifacorias.get(i);
            String optionText = piscifactoria.getNombre() + " [" + piscifactoria.getTotalVivos() + "/"
                    + piscifactoria.getTotalPeces() + "/" + piscifactoria.getCapacidadTotal() + "]";
            menuHelper.addOption(i + 1, optionText);
        }
        menuHelper.addOption(0, "Cancelar");

        menuHelper.showMenu();
    }

    public Piscifactoria selectPisc() {
        menuPisc();
        int selection = inputHelper.readInt("Ingrese su selección: ");

        if (selection < 1 || selection > piscifacorias.size()) {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
            return selectPisc(); // Llamada recursiva hasta que se haga una selección válida
        }

        return piscifacorias.get(selection - 1); // Ajustamos el índice
    }

    public Tanque selectTank(Piscifactoria piscifactoria) {
        menuHelper.clearOptions(); // Limpiar opciones previas

        System.out.println("\nSeleccione un Tanque:");
        List<Tanque> tanques = piscifactoria.getTanques();

        for (int i = 0; i < tanques.size(); i++) {
            Tanque tanque = tanques.get(i);
            String tipoPez = tanque.getTipoPezActual() != null ? tanque.getTipoPezActual().getSimpleName()
                    : "Tanque sin tipo de pez asignado";
            String optionText = "Tanque " + (i + 1) + " [Tipo de pez: " + tipoPez + "]";
            menuHelper.addOption(i + 1, optionText);
        }

        menuHelper.addOption(0, "Cancelar");
        menuHelper.showMenu();

        int selection = inputHelper.readInt("Ingrese su selección: ");
        System.out.println();

        if (selection < 1 || selection > tanques.size()) {
            System.out.println("Selección no válida. Inténtalo de nuevo.");
            return selectTank(piscifactoria); // Llamada recursiva hasta que se haga una selección válida
        }

        return tanques.get(selection - 1); // Ajustamos el índice
    }

    /** Método que muestra el estado de las piscifactorías. */
    public void showGeneralStatus() {
        System.out.println("\n=================== " + nombreEntidad + " ===================");
        System.out.println("Día actual: " + dias);
        System.out.println("Monedas disponibles: " + monedas.getMonedas());

        for (Piscifactoria piscifactoria : piscifacorias) {
            piscifactoria.showStatus();
        }

        if (almacenCentral != null) {
            almacenCentral.mostrarEstado();
        } else {
            System.out.println("\nNo hay Almacén Central disponible.");
        }
        System.out.println("======================================================");
    }

    public void showSpecificStatus() {
        // Obtener la piscifactoría seleccionada
        Piscifactoria piscifactoria = selectPisc();

        // Verificar si se canceló la selección
        if (piscifactoria == null) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Si se seleccionó una piscifactoría, mostrar su estado
        piscifactoria.showTankStatus();
    }

    public void showTankStatus() {
        // Obtener la piscifactoría seleccionada
        Piscifactoria piscifactoria = selectPisc();

        // Verificar si se canceló la selección
        if (piscifactoria == null) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Paso 2: Selecciona un tanque dentro de la piscifactoría
        Tanque tanqueSeleccionado = selectTank(piscifactoria);

        // Cancelar si se selecciona null
        if (tanqueSeleccionado == null) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Paso 3: Muestra el estado de todos los peces en el tanque seleccionado
        System.out.println("\nEstado de los peces en el tanque " + tanqueSeleccionado.getNumeroTanque() + ":");

        // Verificar si hay peces en el tanque
        if (tanqueSeleccionado.getPeces().isEmpty()) {
            System.out.println("Este tanque no contiene peces actualmente.");
        } else {
            for (int i = 0; i < tanqueSeleccionado.getPeces().size(); i++) {
                tanqueSeleccionado.getPeces().get(i).showStatus();
            }
        }
    }

    /**
     * Muestra un desglose de las estadísticas por cada tipo de pez.
     */
    public void showStats() {
        System.out.println("\n=============== Estadísticas ===============");
        estadisticas.mostrar();
        System.out.println("===========================================");
    }

    public void showIctio() {
        // Llama al método selectFish para obtener el pez seleccionado
        Pez pezSeleccionado = selectFish(true);

        // Si el pez seleccionado es nulo, significa que la operación fue cancelada
        if (pezSeleccionado == null) {
            return; // Salimos del método si la selección fue cancelada
        }

        // Mostrar la información del pez seleccionado
        System.out.println("\nInformación del pez seleccionado:");
        System.out.println("Nombre: " + pezSeleccionado.getDatos().getNombre());
        System.out.println("Nombre científico: " + pezSeleccionado.getDatos().getCientifico());
        System.out.println("Tipo: " + pezSeleccionado.getDatos().getTipo());
        System.out.println("Coste: " + pezSeleccionado.getDatos().getCoste() + " unidades");
        System.out.println("Monedas: " + pezSeleccionado.getDatos().getMonedas());
        System.out.println("Huevos: " + pezSeleccionado.getDatos().getHuevos());
        System.out.println("Ciclo: " + pezSeleccionado.getDatos().getCiclo() + " días");
        System.out.println("Madurez: " + pezSeleccionado.getDatos().getMadurez() + " días");
        System.out.println("Óptimo: " + pezSeleccionado.getDatos().getOptimo() + " unidades");

        // Mostrar propiedades
        PecesProps[] propiedades = pezSeleccionado.getDatos().getPropiedades();
        System.out.print("Propiedades: ");
        for (int i = 0; i < propiedades.length; i++) {
            System.out.print(propiedades[i]);
            if (i < propiedades.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        System.out.println("Tipo de cría: " + pezSeleccionado.getDatos().getPiscifactoria());
    }

    public void nextDay() {
        int totalPecesVendidos = 0;
        int totalMonedasGanadas = 0;

        for (Piscifactoria piscifactoria : piscifacorias) {
            piscifactoria.nextDay(); // Llama al método nextDay de cada piscifactoría

            // TODO implementar logica para que vaya sumando los peces vendidos y las
            // monedas ganadas a un total
        }
        // Muestra los resultados totales
        System.out.println(totalPecesVendidos + " peces vendidos por un total de " + totalMonedasGanadas + " monedas.");
    }

    /**
     * Método para seleccionar un pez mediante un menú.
     * 
     * @param permitirHembras Indica si se permiten hembras en la selección.
     * @return El pez seleccionado o null si se cancela.
     */
    public Pez selectFish(boolean permitirHembras) {
        menuHelper.clearOptions(); // Limpiar opciones previas

        // Array de los peces específicos
        PecesDatos[] pecesEspecificos = {
                AlmacenPropiedades.SALMON_ATLANTICO,
                AlmacenPropiedades.TRUCHA_ARCOIRIS,
                AlmacenPropiedades.ARENQUE_ATLANTICO,
                AlmacenPropiedades.BESUGO,
                AlmacenPropiedades.LENGUADO_EUROPEO,
                AlmacenPropiedades.LUBINA_EUROPEA,
                AlmacenPropiedades.ROBALO,
                AlmacenPropiedades.CARPA_PLATEADA,
                AlmacenPropiedades.PEJERREY,
                AlmacenPropiedades.PERCA_EUROPEA,
                AlmacenPropiedades.SALMON_CHINOOK,
                AlmacenPropiedades.TILAPIA_NILO
        };

        // Agregar opciones de peces al menú
        for (int i = 0; i < pecesEspecificos.length; i++) {
            PecesDatos pez = pecesEspecificos[i];
            menuHelper.addOption(i + 1, pez.getNombre());
        }

        // Opción para cancelar
        menuHelper.addOption(0, "Cancelar");

        // Mostrar el menú
        menuHelper.showMenu();

        // Leer la selección del usuario
        int seleccion = inputHelper.readInt("Seleccione un pez:");

        // Validar la selección y asignar el sexo basado en permitirHembras
        switch (seleccion) {
            case 1:
                return new SalmonAtlantico(permitirHembras ? false : true); // false es hembra
            case 2:
                return new TruchaArcoiris(permitirHembras ? false : true);
            case 3:
                return new ArenqueDelAtlantico(permitirHembras ? false : true);
            case 4:
                return new Besugo(permitirHembras ? false : true);
            case 5:
                return new LenguadoEuropeo(permitirHembras ? false : true);
            case 6:
                return new LubinaRayada(permitirHembras ? false : true);
            case 7:
                return new Robalo(permitirHembras ? false : true);
            case 8:
                return new CarpaPlateada(permitirHembras ? false : true);
            case 9:
                return new Pejerrey(permitirHembras ? false : true);
            case 10:
                return new PercaEuropea(permitirHembras ? false : true);
            case 11:
                return new SalmonChinook(permitirHembras ? false : true);
            case 12:
                return new TilapiaDelNilo(permitirHembras ? false : true);
            case 0:
                System.out.println("Operación cancelada.");
                return null; // O puedes lanzar una excepción o manejarlo de otra manera
            default:
                System.out.println("Selección inválida. Por favor, intente de nuevo.");
                return null; // O puedes lanzar una excepción o manejarlo de otra manera
        }
    }

    public void addFish() {
        // Seleccionar una piscifactoría
        Piscifactoria piscifactoria = selectPisc(); // Ahora retorna el objeto directamente
        if (piscifactoria == null) {
            System.out.println("Operación cancelada al seleccionar piscifactoría.");
            return; // Salir si se cancela
        }

        // Seleccionar un tanque dentro de la piscifactoría
        Tanque tanqueSeleccionado = selectTank(piscifactoria); // Ahora retorna el objeto directamente
        if (tanqueSeleccionado == null) {
            System.out.println("Operación cancelada al seleccionar tanque.");
            return; // Salir si se cancela
        }

        // Contar hembras y machos en el tanque
        int hembras = tanqueSeleccionado.getHembras();
        int machos = tanqueSeleccionado.getMachos();

        // Seleccionar un pez según la lógica de sexo
        Pez pezSeleccionado = selectFish(hembras <= machos);
        if (pezSeleccionado == null) {
            System.out.println("No se ha seleccionado ningún pez.");
            return; // Si no se seleccionó un pez, salimos del método
        }

        // Verificar si hay espacio en el tanque
        if (tanqueSeleccionado.addPez(pezSeleccionado)) { // Intenta añadir el pez
            System.out.println("Pez añadido al tanque correctamente.");
            // Mostrar estado del tanque
            tanqueSeleccionado.showCapacity(); // Mostrar capacidad del tanque
        } else {
            System.out.println("No se pudo añadir el pez al tanque " + tanqueSeleccionado.getNumeroTanque()
                    + " de la piscifactoría. Tanque lleno o tipo de pez no permitido.");
        }
    }

    // Método para limpiar un tanque de todos los peces muertos
    public void cleanTank() {
        Tanque tanque = selectTank(selectPisc()); // Seleccionar un tanque
        List<Pez> peces = tanque.getPeces(); // Asumiendo que el tanque tiene un método para obtener sus peces

        // Usamos un bucle for en reversa para eliminar peces muertos
        for (int i = peces.size() - 1; i >= 0; i--) {
            Pez pez = peces.get(i);
            if (!pez.isVivo()) { // Si el pez está muerto
                peces.remove(i); // Elimina el pez del tanque
            }
        }
        System.out.println("Todos los peces muertos han sido eliminados del tanque.");
    }

    // Método para vaciar un tanque de todos los peces, independientemente de su
    // estado
    public void emptyTank() {
        Tanque tanque = selectTank(selectPisc());
        tanque.getPeces().clear(); // Elimina todos los peces del tanque
        System.out.println("El tanque ha sido vaciado.");
    }


    /**
     * Método que cuenta las piscifactorías de río.
     * @return El número de piscifactorías de río.
     */
    public int contarPiscifactoriaDeRio() {
        int contador = 0;
        for (Piscifactoria p : piscifacorias) {
            if (p instanceof PiscifactoriaDeRio) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Método que cuenta las piscifactorías de mar.
     * @return El número de piscifactorías de mar.
     */
    public int contarPiscifactoriaDeMar() {
        int contador = 0;
        for (Piscifactoria p : piscifacorias) {
            if (p instanceof PiscifactoriaDeMar) {
                contador++;
            }
        }
        return contador;
    }

    public void upgrade() {
        while (true) {
            menuHelper.clearOptions();
    
            menuHelper.addOption(1, "Comprar edificios");
            menuHelper.addOption(2, "Mejorar edificios");
            menuHelper.addOption(3, "Cancelar");
    
            menuHelper.showMenu();
            int opcion = inputHelper.readInt("Ingrese su opción:");
    
            switch (opcion) {
                case 1:
                    // Comprar edificios
                    menuHelper.clearOptions();
                    menuHelper.addOption(1, "Piscifactoría");
    
                    // Solo mostrar la opción de comprar almacén central si no está construido
                    if (almacenCentral != null && !almacenCentral.isConstruido()) {
                        menuHelper.addOption(2, "Almacén central");
                    }
    
                    menuHelper.showMenu();
                    int edificioAComprar = inputHelper.readInt("Seleccione el edificio a comprar:");
    
                    if (edificioAComprar == 1) {
                        // Compra de Piscifactoría
                        String nombrePiscifactoria = inputHelper.readString("Ingrese el nombre de la piscifactoría:");
    
                        // Costo de la piscifactoría de río
                        int costoPiscifactoríaRio = 500 * (contarPiscifactoriaDeRio() + 1); // Incrementamos el contador
                        // Costo de la piscifactoría de mar
                        int costoPiscifactoríaMar = 2000 * (contarPiscifactoriaDeMar() + 1); // Incrementamos el contador
    
                        menuHelper.clearOptions();
                        menuHelper.addOption(1, "Piscifactoría de río (" + costoPiscifactoríaRio + " monedas)");
                        menuHelper.addOption(2, "Piscifactoría de mar (" + costoPiscifactoríaMar + " monedas)");
    
                        menuHelper.showMenu();
                        int tipoSeleccionado = inputHelper.readInt("Seleccione el tipo de piscifactoría (1 o 2):");
    
                        Piscifactoria nuevaPiscifactoria = null;
    
                        if (tipoSeleccionado == 1) {
                            // Verificar si hay suficientes monedas y realizar la compra
                            if (getMonedas().gastarMonedas(costoPiscifactoríaRio)) {
                                nuevaPiscifactoria = new PiscifactoriaDeRio(nombrePiscifactoria, monedas);
                                System.out.println("Piscifactoría de Río '" + nombrePiscifactoria + "' comprada.");
                            } else {
                                System.out.println("No tienes suficientes monedas para comprar la piscifactoría de río.");
                            }
                        } else if (tipoSeleccionado == 2) {
                            // Verificar si hay suficientes monedas y realizar la compra
                            if (getMonedas().gastarMonedas(costoPiscifactoríaMar)) {
                                nuevaPiscifactoria = new PiscifactoriaDeMar(nombrePiscifactoria, monedas);
                                System.out.println("Piscifactoría de Mar '" + nombrePiscifactoria + "' comprada.");
                            } else {
                                System.out.println("No tienes suficientes monedas para comprar la piscifactoría de mar.");
                            }
                        } else {
                            System.out.println("Tipo de piscifactoría no válido. Operación cancelada.");
                            return; // O manejar la cancelación según corresponda
                        }
    
                        // Añadir la nueva piscifactoría a la lista de piscifactorías, si fue creada
                        if (nuevaPiscifactoria != null) {
                            piscifacorias.add(nuevaPiscifactoria);
                        }
    
                    } else if (edificioAComprar == 2) {
                        // Solo se permite construir el almacén central si no está construido
                        if (almacenCentral != null && !almacenCentral.isConstruido()) {
                            if (getMonedas().gastarMonedas(2000)) {
                                almacenCentral.construir();
                                System.out.println("Almacén central construido.");
                            } else {
                                System.out.println("No tienes suficientes monedas para construir el almacén central.");
                            }
                        } else {
                            System.out.println("El almacén central ya está construido.");
                        }
                    } else {
                        System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                    }
                    break;
    
                case 2:
                    // Mejorar edificios
                    menuHelper.clearOptions();
                    menuHelper.addOption(1, "Piscifactoría");
                    menuHelper.addOption(2, "Almacén central");
    
                    menuHelper.showMenu();
                    int edificioAMejorar = inputHelper.readInt("Seleccione el edificio a mejorar:");
    
                    if (edificioAMejorar == 1) {
                        // Mejorar piscifactoría
                        menuHelper.clearOptions();
                        menuHelper.addOption(1, "Comprar tanque");
                        menuHelper.addOption(2, "Aumentar almacén de comida");
    
                        menuHelper.showMenu();
                        int mejoraPiscifactoria = inputHelper.readInt("Seleccione la mejora:");
    
                        if (mejoraPiscifactoria == 1) {
                            // Comprar tanque
                            int cantidadTanques = selectPisc().getTanques().size(); // Método que cuenta los tanques en la piscifactoría
                            int costoTanque = 150 * cantidadTanques;
                            
                            if (getMonedas().gastarMonedas(costoTanque)) {
                                // Implementar lógica para comprar un tanque
                                System.out.println("Tanque comprado para la piscifactoría.");
                            } else {
                                System.out.println("No tienes suficientes monedas para comprar el tanque.");
                            }
                        } else if (mejoraPiscifactoria == 2) {
                            // Aumentar almacén de comida
                            int costoAumento = 50;
                            if (getMonedas().gastarMonedas(costoAumento)) {
                                almacenCentral.aumentarCapacidad(25); // Aumenta 25 unidades
                                System.out.println("Almacén de comida aumentado en la piscifactoría.");
                            } else {
                                System.out.println("No tienes suficientes monedas para aumentar el almacén de comida.");
                            }
                        } else {
                            System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                        }
                    } else if (edificioAMejorar == 2) {
                        // Mejorar almacén central
                        if (almacenCentral != null && !almacenCentral.isConstruido()) {
                            System.out.println("El almacén central no está construido. No se puede mejorar.");
                        } else {
                            menuHelper.clearOptions();
                            menuHelper.addOption(1, "Aumentar capacidad");
    
                            menuHelper.showMenu();
                            int mejoraAlmacenCentral = inputHelper.readInt("Seleccione la mejora:");
    
                            if (mejoraAlmacenCentral == 1) {
                                int costoMejora = 200; // Costo de mejora
                                if (getMonedas().gastarMonedas(costoMejora)) {
                                    almacenCentral.aumentarCapacidad(50); // Aumenta 50 unidades
                                } else {
                                    System.out.println("No tienes suficientes monedas para aumentar la capacidad del almacén central.");
                                }
                            } else {
                                System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                            }
                        }
                    } else {
                        System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                    }
                    break;
    
                case 3:
                    // Cancelar
                    System.out.println("Operación cancelada.");
                    return; // Salir del método
    
                default:
                    System.out.println("Opción no válida. Por favor, intenta de nuevo.");
                    break;
            }
        }
    }
    
    
    
    
    public static void main(String[] args) {
        InputHelper inputHelper = new InputHelper(); // Crear una instancia de InputHelper
        Simulador simulador = new Simulador();

        simulador.init(); // Inicializa la simulación

        boolean running = true; // Controla el bucle principal
        while (running) {
            System.out.println();
            simulador.menu(); // Muestra el menú de opciones

            // Usar InputHelper para leer la selección del usuario
            int option = inputHelper.readInt("Ingrese su opción: ");

            switch (option) {
                case 1:
                    simulador.showGeneralStatus(); // Muestra el estado general
                    break;
                case 2:
                    simulador.showSpecificStatus(); // Muestra el estado de la piscifactoría seleccionada
                    break;
                case 3:
                    simulador.showTankStatus(); // Muestra el estado de un tanque específico
                    break;
                case 4:
                    simulador.showStats();
                    break;
                case 5:
                    simulador.showIctio();
                    break;
                case 6:
                    simulador.nextDay(); // Lógica para pasar al siguiente día
                    break;
                case 7:
                    // addFood(); // TODO implement
                    break;
                case 8:
                    simulador.addFish();
                    break;
                case 9:
                    //sell(); // TODO implement
                    break;
                case 10:
                    simulador.cleanTank();
                    break;
                case 11:
                    simulador.emptyTank();
                    break;
                case 12:
                    simulador.upgrade();
                    break;
                case 13: 
                    // Pasar varios días
                    break;
                case 98:
                    // Lógica para agregar peces gratuitos a una piscifactoría
                    break;
                case 99:
                    simulador.getMonedas().ganarMonedas(1000);
                    System.out.println("\nSe han añadido 1000 monedas para pruebas.");
                    break;
                case 14: // Opción para salir
                    running = false; // Termina el bucle
                    System.out.println("\nSaliendo del simulador.");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente de nuevo.");
                    break;
            }
        }
    }

    /**
     * @return int return the dias
     */
    public int getDias() {
        return dias;
    }

    /**
     * @param dias the dias to set
     */
    public void setDias(int dias) {
        this.dias = dias;
    }

    /**
     * @return String return the nombreEntidad
     */
    public String getNombreEntidad() {
        return nombreEntidad;
    }

    /**
     * @param nombreEntidad the nombreEntidad to set
     */
    public void setNombreEntidad(String nombreEntidad) {
        this.nombreEntidad = nombreEntidad;
    }

    /**
     * @return List<Piscifactoria> return the piscifacorias
     */
    public List<Piscifactoria> getPiscifacorias() {
        return piscifacorias;
    }

    /**
     * @param piscifacorias the piscifacorias to set
     */
    public void setPiscifacorias(ArrayList<Piscifactoria> piscifacorias) {
        this.piscifacorias = piscifacorias;
    }

    /**
     * @return SistemaMonedas return the monedas
     */
    public SistemaMonedas getMonedas() {
        return monedas;
    }

    /**
     * @param monedas the monedas to set
     */
    public void setMonedas(SistemaMonedas monedas) {
        this.monedas = monedas;
    }

    /**
     * @return InputHelper return the inputHelper
     */
    public InputHelper getInputHelper() {
        return inputHelper;
    }

    /**
     * @param inputHelper the inputHelper to set
     */
    public void setInputHelper(InputHelper inputHelper) {
        this.inputHelper = inputHelper;
    }
}