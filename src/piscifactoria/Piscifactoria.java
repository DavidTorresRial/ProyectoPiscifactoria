package piscifactoria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import commons.Simulador;
import helpers.InputHelper;
import helpers.MenuHelper;
import peces.Pez;
import peces.propiedades.Carnivoro;
import peces.propiedades.CarnivoroActivo;
import peces.propiedades.Filtrador;
import peces.propiedades.Omnivoro;
import peces.tipos.doble.Dorada;
import tanque.Tanque;
import tanque.TanqueDeCria;
import tanque.TanqueDeHuevos;

/**
 * Clase abstracta que representa una piscifactoría que gestiona tanques de
 * peces.
 */
public abstract class Piscifactoria {

    /** El nombre de la piscifactoría. */
    protected String nombre;

    /** Lista para almacenar los tanques en la piscifactoria. */
    protected List<Tanque> tanques = new ArrayList<>();

    /** Número máximo de tanques permitidos en la piscifactoría. */
    protected final int numeroMaximoTanques = 10;

    /** Cantidad actual de comida animal. */
    private int cantidadComidaAnimal;

    /** Cantidad actual de comida vegetal. */
    private int cantidadComidaVegetal;

    /** Capacidad máxima para ambos tipos de comida. */
    protected int capacidadMaximaComida;

    /** Agrega las listas para almacenar los tanques de cría y de huevos */
    protected List<TanqueDeCria> tanquesCria = new ArrayList<>();

    protected List<TanqueDeHuevos> tanquesHuevos = new ArrayList<>();

    /**
     * Constructor para crear una nueva piscifactoría.
     *
     * @param nombre El nombre de la piscifactoría.
     */
    public Piscifactoria(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Constructor para crear una piscifactoría con valores personalizados.
     *
     * @param nombre                Nombre de la piscifactoría.
     * @param capacidadMaximaComida Capacidad máxima de comida que puede almacenar.
     * @param cantidadComidaAnimal  Cantidad de comida animal disponible.
     * @param cantidadComidaVegetal Cantidad de comida vegetal disponible.
     */
    public Piscifactoria(String nombre, int capacidadMaximaComida, int cantidadComidaAnimal,
            int cantidadComidaVegetal) {
        this.nombre = nombre;
        this.capacidadMaximaComida = capacidadMaximaComida;
        this.cantidadComidaAnimal = cantidadComidaAnimal;
        this.cantidadComidaVegetal = cantidadComidaVegetal;
    }

    /** Muestra toda la información de la piscifactoría. */
    public void showStatus() {
        System.out.println("\n=============== " + nombre + " ===============");

        int totalPeces = getTotalPeces();
        int capacidadTotal = getCapacidadTotal();
        int totalVivos = getTotalVivos();
        int totalAlimentados = getTotalAlimentados();
        int totalAdultos = getTotalMaduros();
        int totalHembras = getTotalHembras();
        int totalMachos = getTotalMachos();
        int totalFertiles = getTotalFertiles();

        int porcentajeOcupacion = (capacidadTotal > 0) ? (totalPeces * 100) / capacidadTotal : 0;
        int porcentajeVivos = (totalPeces > 0) ? (totalVivos * 100) / totalPeces : 0;
        int porcentajeAlimentados = (totalVivos > 0) ? (totalAlimentados * 100) / totalVivos : 0;
        int porcentajeAdultos = (totalVivos > 0) ? (totalAdultos * 100) / totalVivos : 0;

        System.out.println("Tanques: " + tanques.size());
        System.out.println("Ocupación: " + totalPeces + " / " + capacidadTotal + " (" + porcentajeOcupacion + "%)");
        System.out.println("Peces vivos: " + totalVivos + " / " + totalPeces + " (" + porcentajeVivos + "%)");
        System.out.println(
                "Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + porcentajeAlimentados + "%)");
        System.out.println("Peces adultos: " + totalAdultos + " / " + totalVivos + " (" + porcentajeAdultos + "%)");
        System.out.println("Hembras / Machos: " + totalHembras + " / " + totalMachos);
        System.out.println("Fértiles: " + totalFertiles + " / " + totalVivos);

        showFood();
    }

    /** Muestra el estado de cada tanque en la piscifactoría. */
    public void showTankStatus() {
        tanques.forEach(Tanque::showStatus);
    }

    /**
     * Muestra la información de los peces de un tanque determinado.
     *
     * @param numeroTanque El número del tanque a mostrar.
     */
    public void showFishStatus(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1);
        tanque.showFishStatus();
    }

    /**
     * Muestra la ocupación de un tanque determinado.
     *
     * @param numeroTanque El número del tanque a mostrar.
     */
    public void showCapacity(int numeroTanque) {
        Tanque tanque = tanques.get(numeroTanque - 1);
        tanque.showCapacity(this);
    }

    /** Muestra el estado actual del depósito de comida de la piscifactoría. */
    public void showFood() {
        System.out.println("Depósito de comida de la piscifactoría " + nombre + ":");

        System.out.println("Comida vegetal al " + (cantidadComidaVegetal * 100 / capacidadMaximaComida)
                + "% de su capacidad. [" + cantidadComidaVegetal + "/" + capacidadMaximaComida + "]");
        System.out.println("Comida animal al " + (cantidadComidaAnimal * 100 / capacidadMaximaComida)
                + "% de su capacidad. [" + cantidadComidaAnimal + "/" + capacidadMaximaComida + "]");
    }

    /**
     * Hace avanzar el ciclo de vida en la piscifactoría, alimentando a los peces y
     * actualizando sus estados.
     * 
     * @return Un arreglo con el número de peces vendidos y las monedas ganadas
     *         durante el día.
     */
    public int[] nextDay() {
        int pecesVendidos = 0;
        int monedasGanadas = 0;

        for (Tanque tanque : tanques) {
            alimentarPeces(tanque);
            int[] resultadoTanque = tanque.nextDay();
            pecesVendidos += resultadoTanque[0];
            monedasGanadas += resultadoTanque[1];
        }

        // Actualizar tanques de cría
        for (TanqueDeCria tc : tanquesCria) {
            tc.nextDay(true);
        }
        // Transferir crías desde tanques de huevos a tanques
        transferirCriasDesdeHuevos();

        return new int[] { pecesVendidos, monedasGanadas };
    }

    /** Mejora el almacén de comida aumentando su capacidad máxima. */
    public abstract void upgradeFood();

    /** Agrega un tanque, verifica monedas y el límite de tanques. */
    public abstract void addTanque();

    /**
     * Alimenta a los peces en un tanque específico.
     *
     * @param tanque El tanque del que se alimentarán los peces.
     */
    private void alimentarPeces(Tanque tanque) {

        for (Pez pez : tanque.getPeces()) {
            if (pez.isVivo()) {
                if (pez instanceof Omnivoro) {
                    if (this.cantidadComidaAnimal >= this.cantidadComidaVegetal) {
                        this.cantidadComidaAnimal -= pez.alimentar(this.cantidadComidaAnimal,
                                this.cantidadComidaVegetal);
                    } else {
                        this.cantidadComidaVegetal -= pez.alimentar(this.cantidadComidaAnimal,
                                this.cantidadComidaVegetal);
                    }
                } else if (pez instanceof Filtrador) {
                    this.cantidadComidaVegetal -= pez.alimentar(this.cantidadComidaAnimal, this.cantidadComidaVegetal);
                } else if (pez instanceof Carnivoro) {
                    this.cantidadComidaAnimal -= pez.alimentar(this.cantidadComidaAnimal, this.cantidadComidaVegetal);
                } else if (pez instanceof CarnivoroActivo) {
                    this.cantidadComidaAnimal -= pez.alimentar(this.cantidadComidaAnimal, this.cantidadComidaVegetal);
                }
            }
        }
    }

    /**
     * Vende los peces adultos y vivos de un tanque, registrando ganancias y
     * estadísticas.
     *
     * @param tanqueSeleccionado el tanque del cual se venden los peces.
     */
    public static void sellFish(Tanque tanqueSeleccionado) {
        if (tanqueSeleccionado != null) {
            int pecesVendidos = 0;
            int totalDinero = 0;

            Iterator<Pez> iterator = tanqueSeleccionado.getPeces().iterator();

            while (iterator.hasNext()) {
                Pez pez = iterator.next();

                if (pez.getEdad() >= pez.getDatos().getMadurez() && pez.isVivo()) {
                    Simulador.monedas.ganarMonedas(pez.getDatos().getMonedas());
                    Simulador.estadisticas.registrarVenta(pez.getNombre(), pez.getDatos().getMonedas());

                    totalDinero += (pez.getDatos().getMonedas() / 2);
                    pecesVendidos++;

                    iterator.remove();
                }
            }
            if (pecesVendidos > 0) {
                System.out.println("\nVendidos " + pecesVendidos + " peces de la piscifactoría de forma manual por "
                        + totalDinero + " monedas.");
            } else {
                System.out.println("\nNo hay peces adultos para vender.");
            }
        }
    }

    /**
     * Método para añadir comida animal al almacén.
     * 
     * @param cantidad La cantidad de comida animal a añadir. Debe ser positiva.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public boolean añadirComidaAnimal(int cantidad) {
        int nuevaCantidad = cantidadComidaAnimal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadMaximaComida) {
            cantidadComidaAnimal = nuevaCantidad;
            return true;
        } else {
            System.out.println("\nNo se puede añadir la cantidad de comida animal: excede la capacidad.");
            return false;
        }
    }

    /**
     * Método para añadir comida vegetal al almacén.
     * 
     * @param cantidad La cantidad de comida vegetal a añadir. Debe ser positiva.
     * @return true si se añadió la comida, false si no se pudo añadir.
     */
    public boolean añadirComidaVegetal(int cantidad) {
        int nuevaCantidad = cantidadComidaVegetal + cantidad;
        if (cantidad >= 0 && nuevaCantidad <= capacidadMaximaComida) {
            cantidadComidaVegetal = nuevaCantidad;
            return true;
        } else {
            System.out.println("\nNo se puede añadir la cantidad de comida vegetal: excede la capacidad.");
            return false;
        }
    }

    /**
     * Gestiona las operaciones relacionadas con los tanques de cría en una
     * piscifactoría.
     * Permite al usuario realizar acciones como mostrar el estado de los tanques,
     * comprar nuevos tanques de cría, y vaciar tanques existentes.
     *
     * @param pisc La piscifactoría en la que se administrarán los tanques de cría.
     */

    public void gestionarTanquesCria() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Gestión de Tanques de Cría ---");
            String[] opciones = { "Mostrar estado",
                    "Comprar tanque de cría (" + tanque.TanqueDeCria.COSTO_TANQUE_CRIAS + " monedas)",
                    "Vaciar tanque de cría" };
            MenuHelper.mostrarMenuCancelar(opciones);
            int opcion = InputHelper.solicitarNumero(0, opciones.length);

            switch (opcion) {
                case 1:
                    if (getTanquesCria().isEmpty()) {
                        System.out.println("No hay tanques de cría.");
                    } else {
                        int idx = 1;
                        for (tanque.TanqueDeCria tc : getTanquesCria()) {
                            System.out.println("Tanque " + idx + ": " + tc.estado());
                            idx++;
                        }
                    }
                    break;

                case 2:
                    comprarTanqueCria();
                    break;

                case 3:
                    if (getTanquesCria().isEmpty()) {
                        System.out.println("No hay tanques de cría para vaciar.");
                    } else {
                        int idx = 1;
                        for (tanque.TanqueDeCria tc : getTanquesCria()) {
                            System.out.println(idx + ". " + tc.estado());
                            idx++;
                        }
                        int seleccion = InputHelper.solicitarNumero(1, getTanquesCria().size());
                        String confirm = InputHelper.readString("¿Está seguro que desea vaciar este tanque? (S/N): ")
                                .toUpperCase();
                        if (confirm.equals("S")) {
                            getTanquesCria().get(seleccion - 1).vaciarTanque();
                            System.out.println("Tanque de cría vaciado.");
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                    }
                    break;
                case 0:
                    salir = true;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }
    }
 
    //TODO javadoc
    public void comprarTanqueCria() {
        if (getTanquesCria().size() < 3) {
                        int precio = tanque.TanqueDeCria.COSTO_TANQUE_CRIAS;
                        if (Simulador.monedas.getMonedas() < precio) {
                            System.out.println("No tienes suficientes monedas para comprar un tanque de cría.");
                        } else {
                            List<String> opcionesPeces = new ArrayList<>();
                            for (Tanque tanque : tanques) {
                                List<Pez> pecesTanque = tanque.getPeces();
                                for (Pez pez : pecesTanque) {
                                    opcionesPeces.add(pez.getNombre());
                                }
                            }
                            MenuHelper.mostrarMenu(opcionesPeces.toArray(new String[0]));
                            String especie = opcionesPeces.get(InputHelper.solicitarNumero(0, opcionesPeces.size()));
                            tanque.TanqueDeCria nuevoTanque = new tanque.TanqueDeCria(especie);

                            System.out.println("\nComprando pareja para el tanque de cría de " + especie);
                            
                            boolean comprada = nuevoTanque.comprarPareja();
                            if (comprada) {
                                getTanquesCria().add(nuevoTanque);
                                Simulador.monedas.gastarMonedas(precio);
                                System.out.println("\nTanque de cría para " + especie + " creado y pareja asignada por "
                                        + precio + " monedas.");
                            } else {
                                System.out.println("No se pudo asignar la pareja en el tanque de cría.");
                            }
                        }
                    } else {
                        System.out.println("Ya se han alcanzado los 3 tanques de cría permitidos.");
                    }
    }

    /**
     * Gestiona las operaciones relacionadas con los tanques de huevos en una
     * piscifactoría.
     * 
     * Permite al usuario:
     * - Listar el contenido de los tanques de huevos.
     * - Vaciar todos los tanques de huevos dentro de una piscifactoría
     * seleccionada.
     * - Comprar un nuevo tanque de huevos si tiene suficiente dinero (usa el precio
     * de la clase `TanqueDeHuevos`).
     * 
     * @param pisc la piscifactoría cuyos tanques de huevos se van a gestionar.
     */
    public void gestionarTanquesHuevos() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Gestión de Tanques de Huevos ---");
            String[] opciones = { "Listar contenido", "Vaciar tanques de huevos",
                    "Comprar tanque de huevos (" + tanque.TanqueDeHuevos.COSTO_TANQUE_HUEVOS + " monedas)" };
            MenuHelper.mostrarMenuCancelar(opciones);

            int opcion = InputHelper.solicitarNumero(0, opciones.length);
            switch (opcion) {
                case 1:
                    listarContenidoTanquesHuevos();
                    break;
                case 2:
                    vaciarTanquesHuevos();
                    break;
                case 3:
                    comprarTanqueHuevos();
                    break;
                case 0:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Lista el contenido de los tanques de huevos de la piscifactoría.
     * Muestra cuántos peces hay de cada especie en todos los tanques de huevos.
     * 
     * @param pisc Piscifactoría cuyos tanques de huevos se van a listar.
     */
    public void listarContenidoTanquesHuevos() {
        if (getTanquesHuevos().isEmpty()) {
            System.out.println("No hay tanques de huevos en esta piscifactoría.");
            return;
        }
        ArrayList<String> especies = new ArrayList<>();
        ArrayList<Integer> cantidades = new ArrayList<>();

        for (TanqueDeHuevos th : getTanquesHuevos()) {

            Map<String, Integer> especiesTanque = th.contarEspecies();

            for (String especie : especiesTanque.keySet()) {
                int cantidad = especiesTanque.get(especie);
                int indice = especies.indexOf(especie);

                if (indice == -1) {
                    especies.add(especie);
                    cantidades.add(cantidad);
                } else {
                    cantidades.set(indice, cantidades.get(indice) + cantidad);
                }
            }
        }

        if (especies.isEmpty()) {
            System.out.println("Los tanques de huevos están vacíos.");
        } else {
            System.out.println("Contenido de tanques de huevos:");
            for (int i = 0; i < especies.size(); i++) {
                System.out.println(especies.get(i) + ": " + cantidades.get(i));
            }
        }
    }

    /**
     * Vacía todos los tanques de huevos de la piscifactoría si el usuario lo
     * confirma.
     * 
     * @param pisc Piscifactoría cuyos tanques de huevos se van a vaciar.
     */
    public void vaciarTanquesHuevos() {
        if (getTanquesHuevos().isEmpty()) {
            System.out.println("No hay tanques de huevos para vaciar.");
            return;
        }

        String confirm = InputHelper.readString("¿Está seguro de vaciar TODOS los tanques de huevos? (S/N): ")
                .toUpperCase();
        if (confirm.equals("S")) {
            for (tanque.TanqueDeHuevos th : getTanquesHuevos()) {
                th.vaciarTanque();
            }
            System.out.println("Todos los tanques de huevos han sido vaciados.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    /**
     * Permite comprar un nuevo tanque de huevos si el jugador tiene suficientes
     * monedas.
     * 
     * @param pisc Piscifactoría donde se comprará el tanque de huevos.
     */
    public void comprarTanqueHuevos() {
        int precio = tanque.TanqueDeHuevos.COSTO_TANQUE_HUEVOS; // Se usa el precio definido en la clase

        if (Simulador.monedas.getMonedas() < precio) {
            System.out.println("No tienes suficientes monedas para comprar un tanque de huevos.");
            return;
        }

        Simulador.monedas.gastarMonedas(precio);
        getTanquesHuevos().add(new tanque.TanqueDeHuevos());
        System.out.println("Se ha comprado un nuevo tanque de huevos por " + precio + " monedas.");
    }

    /**
     * Devuelve el nombre de la piscifactoría.
     *
     * @return El nombre de la piscifactoría.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la lista de tanques de la piscifactoría.
     *
     * @return La lista de tanques.
     */
    public List<Tanque> getTanques() {
        return this.tanques;
    }

    /**
     * Obtiene el número máximo de tanques que puede tener la piscifactoría.
     *
     * @return El número máximo de tanques permitidos en la piscifactoría.
     */
    public int getNumeroMaximoTanques() {
        return numeroMaximoTanques;
    }

    /**
     * Obtiene la capacidad máxima de comida.
     * 
     * @return La capacidad máxima de comida.
     */
    public int getCapacidadMaximaComida() {
        return capacidadMaximaComida;
    }

    /**
     * Establece un incremento a la capacidad máxima de comida del depósito.
     * 
     * @param num El valor que se sumará a la capacidad máxima de comida.
     * @return La nueva capacidad máxima de comida después de la suma.
     */
    public int setCapacidadMaximaComida(int num) {
        return capacidadMaximaComida = num;
    }

    /**
     * Devuelve la cantidad actual de comida vegetal en la piscifactoría.
     *
     * @return La cantidad actual de comida vegetal.
     */
    public int getComidaVegetalActual() {
        return cantidadComidaVegetal;
    }

    /**
     * Establece la cantidad de comida vegetal, respetando la capacidad máxima
     * permitida.
     *
     * @param cantidadComidaVegetal la cantidad de comida vegetal a asignar.
     */
    public void setCantidadComidaVegetal(int cantidadComidaVegetal) {
        if (cantidadComidaVegetal > capacidadMaximaComida) {
            this.cantidadComidaVegetal = capacidadMaximaComida;
        } else {
            this.cantidadComidaVegetal = cantidadComidaVegetal;
        }
    }

    /**
     * Devuelve la cantidad actual de comida animal en la piscifactoría.
     *
     * @return La cantidad actual de comida animal.
     */
    public int getComidaAnimalActual() {
        return cantidadComidaAnimal;
    }

    /**
     * Establece la cantidad de comida animal, respetando la capacidad máxima
     * permitida.
     *
     * @param cantidadComidaAnimal la cantidad de comida animal a asignar.
     */
    public void setCantidadComidaAnimal(int cantidadComidaAnimal) {
        if (cantidadComidaAnimal > capacidadMaximaComida) {
            this.cantidadComidaVegetal = capacidadMaximaComida;
        } else {
            this.cantidadComidaAnimal = cantidadComidaAnimal;
        }
    }

    /**
     * Devuelve el total de peces en la piscifactoría.
     *
     * @return El total de peces.
     */
    public int getTotalPeces() {
        int totalPeces = 0;
        for (Tanque tanque : tanques) {
            totalPeces += tanque.getPeces().size();
        }
        return totalPeces;
    }

    /**
     * Devuelve la capacidad total de todos los tanques de la piscifactoría.
     *
     * @return La capacidad total de los tanques.
     */
    public int getCapacidadTotal() {
        int capacidadTotal = 0;
        for (Tanque tanque : tanques) {
            capacidadTotal += tanque.getCapacidad();
        }
        return capacidadTotal;
    }

    /**
     * Devuelve el total de machos en la piscifactoría.
     *
     * @return El total de machos.
     */
    public int getTotalMachos() {
        int totalMachos = 0;
        for (Tanque tanque : tanques) {
            totalMachos += tanque.getMachos();
        }
        return totalMachos;
    }

    /**
     * Devuelve el total de hembras en la piscifactoría.
     *
     * @return El total de hembras.
     */
    public int getTotalHembras() {
        int totalHembras = 0;
        for (Tanque tanque : tanques) {
            totalHembras += tanque.getHembras();
        }
        return totalHembras;
    }

    /**
     * Devuelve el total de peces feudiles en la piscifactoría.
     *
     * @return El total de peces feudiles.
     */
    public int getTotalFertiles() {
        int totalFertiles = 0;
        for (Tanque tanque : tanques) {
            totalFertiles += tanque.getFertiles();
        }
        return totalFertiles;
    }

    /**
     * Devuelve el total de peces vivos en la piscifactoría.
     *
     * @return El total de peces vivos.
     */
    public int getTotalVivos() {
        int totalVivos = 0;
        for (Tanque tanque : tanques) {
            totalVivos += tanque.getVivos();
        }
        return totalVivos;
    }

    /**
     * Devuelve el total de peces alimentados en la piscifactoría.
     *
     * @return El total de peces alimentados.
     */
    public int getTotalAlimentados() {
        int totalAlimentados = 0;
        for (Tanque tanque : tanques) {
            totalAlimentados += tanque.getAlimentados();
        }
        return totalAlimentados;
    }

    /**
     * Devuelve el total de peces adultos en la piscifactoría.
     *
     * @return El total de peces adultos.
     */
    public int getTotalMaduros() {
        int totalMaduros = 0;
        for (Tanque tanque : tanques) {
            totalMaduros += tanque.getMaduros();
        }
        return totalMaduros;
    }

    /**
     * Devuelve la lista de tanques de cría.
     *
     * @return una lista de objetos de tipo TanqueDeCria.
     */
    public List<TanqueDeCria> getTanquesCria() {
        return tanquesCria;
    }

    /**
     * Devuelve la lista de tanques de huevos.
     *
     * @return una lista de objetos de tipo TanqueDeHuevos.
     */
    public List<TanqueDeHuevos> getTanquesHuevos() {
        return tanquesHuevos;
    }

    /**
     * Transfiere las crías de los tanques de huevos a los tanques de cría si
     * hay espacio disponible en los tanques de cría. Las crías se transfieren
     * solamente si el primer pez del tanque de cría tiene el mismo nombre que
     * la cría. Si no se puede transferir, la cría permanece en el tanque de
     * huevos.
     */
    public void transferirCriasDesdeHuevos() {
        for (TanqueDeHuevos th : tanquesHuevos) {
            // Crear copia para evitar problemas de concurrencia
            List<Pez> criasPendientes = new ArrayList<>();
            for (Pez cria : criasPendientes) {
                for (Tanque t : tanques) {
                    if (!t.getPeces().isEmpty() &&
                            t.getPeces().getFirst().getNombre().equals(cria.getNombre()) &&
                            t.getPeces().size() < t.getCapacidad()) {
                        t.getPeces().add(cria);
                        th.getCrias().remove(cria);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Devuelve una representación en cadena del estado de la piscifactoría.
     * 
     * @return una cadena que representa el estado de la piscifactoría.
     */
    @Override
    public String toString() {
        return "\nInformación de la Piscifactoría: " + nombre +
                "\n  Número de Tanques    : " + tanques.size() +
                "\n  Total de Peces       : " + getTotalPeces() + " (Ocupación: "
                + ((getCapacidadTotal() > 0) ? (getTotalPeces() * 100) / getCapacidadTotal() : 0) +
                "\n  Peces Vivos          : " + getTotalVivos() +
                "\n  Peces Alimentados    : " + getTotalAlimentados() +
                "\n  Peces Adultos        : " + getTotalMaduros() +
                "\n  Hembras              : " + getTotalHembras() +
                "\n  Machos               : " + getTotalMachos() +
                "\n  Peces Fértiles       : " + getTotalFertiles() +
                "\n  Comida Vegetal       : " + getComidaVegetalActual() + " / " + getCapacidadTotal() +
                "\n  Comida Animal        : " + getComidaAnimalActual() + " / " + getCapacidadTotal();
    }
}
