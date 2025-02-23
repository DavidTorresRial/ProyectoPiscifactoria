package piscifactoria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import commons.Simulador;
import helpers.InputHelper;
import helpers.MenuHelper;
import peces.Pez;
import peces.propiedades.Carnivoro;
import peces.propiedades.CarnivoroActivo;
import peces.propiedades.Filtrador;
import peces.propiedades.Omnivoro;
import registros.Registros;
import tanque.Tanque;
import tanque.TanqueCria;
import tanque.TanqueHuevos;

/** Clase abstracta que representa una piscifactoría que gestiona tanques de peces. */
public abstract class Piscifactoria {

    /** El nombre de la piscifactoría. */
    protected String nombre;

    /** Lista para almacenar los tanques en la piscifactoria. */
    protected List<Tanque> tanques = new ArrayList<>();

    /** Lista para almacenar los tanques de cria en la piscifactoria. */
    protected List<TanqueCria> tanquesCria = new ArrayList<>();

    /** Lista para almacenar los tanques de huevos en la piscifactoria. */
    protected List<TanqueHuevos> tanquesHuevos = new ArrayList<>();


    /** Número máximo de tanques permitidos en la piscifactoría. */
    protected final int numeroMaximoTanques = 10;
    
    /** Cantidad actual de comida animal. */
    private int cantidadComidaAnimal;

    /** Cantidad actual de comida vegetal. */
    private int cantidadComidaVegetal;

    /** Capacidad máxima para ambos tipos de comida. */
    protected int capacidadMaximaComida;

    /** Cantidad máxima de tanques de cria. */
    protected int CANTIDAD_MAXIMA_TANQUES_CRIA = 3;

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
     * @param nombre Nombre de la piscifactoría.
     * @param capacidadMaximaComida Capacidad máxima de comida que puede almacenar.
     * @param cantidadComidaAnimal Cantidad de comida animal disponible.
     * @param cantidadComidaVegetal Cantidad de comida vegetal disponible.
     */
    public Piscifactoria(String nombre, int capacidadMaximaComida, int cantidadComidaAnimal, int cantidadComidaVegetal) {
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
        System.out.println("Peces alimentados: " + totalAlimentados + " / " + totalVivos + " (" + porcentajeAlimentados + "%)");
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
     * Hace avanzar el ciclo de vida en la piscifactoría, alimentando a los peces y actualizando sus estados.
     * 
     * @return Un arreglo con el número de peces vendidos y las monedas ganadas durante el día.
     */
    public int[] nextDay() {
        int pecesVendidos = 0;
        int monedasGanadas = 0;
    
        for (Tanque tanque : tanques) {
            alimentarPeces(tanque);
            int[] resultadoTanque = tanque.nextDay(tanquesHuevos.isEmpty() ? false : true);
            pecesVendidos += resultadoTanque[0];
            monedasGanadas += resultadoTanque[1];
        }
        if (tanquesCria.size() > 0) {
            for (TanqueCria tanqueCria : tanquesCria) {
                tanqueCria.pasarDia(this);
            }
        }
        redistribuirCrias();
        return new int[]{pecesVendidos, monedasGanadas};
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
                        this.cantidadComidaAnimal -= pez.alimentar(this.cantidadComidaAnimal, this.cantidadComidaVegetal);
                    } else {
                        this.cantidadComidaVegetal -= pez.alimentar(this.cantidadComidaAnimal, this.cantidadComidaVegetal);
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
     * Vende los peces adultos y vivos de un tanque, registrando ganancias y estadísticas.
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
                System.out.println("\nVendidos " + pecesVendidos + " peces de la piscifactoría de forma manual por " + totalDinero + " monedas.");
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
     * Gestiona la interacción del usuario para administrar los tanques de cría y de huevos de la piscifactoría.
     * Este método muestra un menú con las opciones para gestionar los tanques de cría y de huevos, y
     * permite al usuario seleccionar una de ellas. Dependiendo de la opción seleccionada, se llamará
     * a los métodosgestionarTanquesCria ogestionarTanquesHuevos respectivamente.
     */
    public void gestionarPiscifactoria() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=================== Gestiónar piscifactoria ===================");
            String[] opcionesMenuPrincipal = { "Gestionar tanque de cria", "Gestionar tanque de huevos" };
            MenuHelper.mostrarMenuCancelar(opcionesMenuPrincipal);

            int opcionPrincipal = InputHelper.solicitarNumero(0, opcionesMenuPrincipal.length);

            switch (opcionPrincipal) {
                case 1:
                    gestionarTanquesCria();
                    break;
                case 2:
                    gestionarTanquesHuevos();
                    break;
                case 0:
                    salir = true;
            }
        }
    }

    /**
     * Gestiona la interacción del usuario para administrar los tanques de cría.
     * Este método muestra un menú con los tanques de cría disponibles, permite seleccionar uno
     * y, dependiendo de si el tanque está vacío o no, ofrece opciones para mostrar el estado,
     * comprar peces o vaciar el tanque.
     */
    public void gestionarTanquesCria() {
        boolean salir = false;
        TanqueCria tanqueCria;
        System.out.println("\n=================== Gestión de Tanques de Cría ===================");
        for (int i = 0; i < tanquesCria.size(); i++) {
            System.out.println((i + 1) + ". Tanque de Cría " + (i + 1));
        }
        int tanqueCriaSeleccionado = InputHelper.solicitarNumero(0, tanquesCria.size());
        if (tanqueCriaSeleccionado != 0) {
            tanqueCria = tanquesCria.get(tanqueCriaSeleccionado - 1);
        } else {
            tanqueCria = null;
        }

        if (tanqueCria != null) {
            while (!salir) {
                System.out.println("\n=================== Gestión de Tanques de Cría ===================");
                boolean vacio = tanqueCria.getPadres().isEmpty();
                String[] opcionesMenuPrincipal = vacio ? new String[] { "Estado", "Comprar peces" } : new String[] {"Estado", "Vaciar tanque" };
    
                MenuHelper.mostrarMenuCancelar(opcionesMenuPrincipal);
    
                int opcionPrincipal = InputHelper.solicitarNumero(0, opcionesMenuPrincipal.length);
    
                switch (opcionPrincipal) {
                    case 1:
                        tanqueCria.mostrarEstado();
                        break;
                    case 2:
                        if (vacio) {
                            tanqueCria.comprarPeces(this instanceof PiscifactoriaDeRio);
                        } else {
                            tanqueCria.getPadres().clear();
                        }
                        break;
                    case 0:
                        salir = true;
                }
            }
        }
    }

    /**
     * Permite gestionar el tanque de huevos de la piscifactoría.
     * 
     * @author David, Fran, Marcos.
     */
    public void gestionarTanquesHuevos() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=================== Gestión de Tanques de Huevos ===================");
            String[] opcionesMenuPrincipal = { "Listar ", "Vaciar tanque" };
            MenuHelper.mostrarMenuCancelar(opcionesMenuPrincipal);

            int opcionPrincipal = InputHelper.solicitarNumero(0, opcionesMenuPrincipal.length);

            switch (opcionPrincipal) {
                case 1:
                    listar();
                    break;
                case 2:
                    vaciarTanqueHuevos();
                    break;
                case 0:
                    salir = true;
            }
        }
    }

    /**
     * Muestra la lista de peces en los tanques de huevos de la piscifactoría.
     * Si no hay tanques de huevos, muestra un mensaje indicando que no hay.
     */
    public void listar() {
        if (tanquesHuevos.isEmpty()) {
            System.out.println("No hay tanques de huevos en la piscifactoría.");
        } else {
            for (int i = 0; i < tanquesHuevos.size(); i++) {
                System.out.println("Tanque de Huevos " + (i + 1) + ":");
                tanquesHuevos.get(i).listarPeces();
            }
        }
    }

    /**
     * Vacía todos los tanques de huevos de la piscifactoría.
     */
    public void vaciarTanqueHuevos() {
        if (tanquesHuevos.isEmpty()) {
            System.out.println("\nNo hay tanques de huevo en la piscifactoría: " + nombre);
        } else{
            for (TanqueHuevos tanque : tanquesHuevos) {
                tanque.getHuevos().clear();
            }
        }
    }

    /** Comprar un tanque de Cria. */
    public void comprarTanqueCria() {
        if (tanquesCria.size() < CANTIDAD_MAXIMA_TANQUES_CRIA) {
            if (Simulador.monedas.ganarMonedas(500)) {
                tanquesCria.add(new TanqueCria());
                System.out.println("\nAcabas de comprar un tanque de cria por: 500 monedas.");
                Simulador.registro.registroComprarTanquesCrias(nombre);
            } else {
                System.out.println("\nNecesitas 500 monedas para comprarte el tanque de cria numero " + tanquesCria.size() + ":");
            }
        } else {
            System.out.println("\nNo puedes añadir mas tanques de cria a la piscifactoría: " + nombre);
        }
    }

    /** Comprar un tanque de Huevos. */
    public void comprarTanqueHuevos() {
        if (Simulador.monedas.ganarMonedas(1500)) {
            tanquesHuevos.add(new TanqueHuevos());
            System.out.println("\nAcabas de comprar un tanque de huevos por: 1500 monedas.");
            Simulador.registro.registroComprarTanquesHuevos(nombre);
        } else {
            System.out.println("\nNecesitas 1500 monedas para comprarte el tanque de huevos numero " + tanquesHuevos.size() + ":");
        }
    }

    /** Redistribuye las crias si es posible. */
    public void redistribuirCrias() {
        for (TanqueHuevos tanque : tanquesHuevos) {
            tanque.redistribuirCrias(tanques);
        }    
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
     * Devuelve la lista de tanques de cría de la piscifactoría.
     *
     * @return La lista de tanques de cría.
     */

    public List<TanqueCria> getTanquesCria() {
        return tanquesCria;
    }

    /**
     * Devuelve la lista de tanques de huevos de la piscifactoría.
     * 
     * @return La lista de tanques de huevos.
     */
    public List<TanqueHuevos> getTanquesHuevos() {
        return tanquesHuevos;
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
     * Establece la cantidad de comida vegetal, respetando la capacidad máxima permitida.
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
     * Establece la cantidad de comida animal, respetando la capacidad máxima permitida.
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
     * Devuelve una representación en cadena del estado de la piscifactoría.
     * 
     * @return una cadena que representa el estado de la piscifactoría.
     */
    @Override
    public String toString() {
        return "\nInformación de la Piscifactoría: " + nombre +
                "\n  Número de Tanques    : " + tanques.size() +
                "\n  Total de Peces       : " + getTotalPeces() + " (Ocupación: " + ((getCapacidadTotal() > 0) ? (getTotalPeces() * 100) / getCapacidadTotal() : 0) +
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
