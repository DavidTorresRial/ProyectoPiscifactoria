package tanque;

import java.util.ArrayList;
import java.util.List;

import commons.Simulador;
import helpers.InputHelper;
import helpers.MenuHelper;
import peces.Pez;
import peces.propiedades.Carnivoro;
import peces.propiedades.CarnivoroActivo;
import peces.propiedades.Filtrador;
import peces.propiedades.Omnivoro;
import peces.tipos.doble.Dorada;
import peces.tipos.doble.SalmonAtlantico;
import peces.tipos.doble.TruchaArcoiris;
import peces.tipos.mar.ArenqueDelAtlantico;
import peces.tipos.mar.Besugo;
import peces.tipos.mar.LenguadoEuropeo;
import peces.tipos.mar.LubinaRayada;
import peces.tipos.mar.Robalo;
import peces.tipos.rio.CarpaPlateada;
import peces.tipos.rio.Pejerrey;
import peces.tipos.rio.PercaEuropea;
import peces.tipos.rio.SalmonChinook;
import peces.tipos.rio.TilapiaDelNilo;
import piscifactoria.Piscifactoria;
import propiedades.AlmacenPropiedades;

/**
 * Representa un tanque de crias con capacidades de gestión las crias y la reproducción.
 */
public class TanqueCria {

    private List<Pez> padres = new ArrayList<>();

    /**
     * Muestra el estado actual del tanque, incluyendo el tipo de pez, 
     * el estado de su ciclo y la lista de padres y crías.
     */
    public void mostrarEstado() {
        if (!padres.isEmpty()) {
            Pez primerPadre = padres.get(0);
            String especie = primerPadre.getNombre();
            int ciclo = primerPadre.getDatos().getCiclo();
            int madurez = 0;
            
            System.out.println("\nTipo de pez: " + especie);
            System.out.println("Ciclo: " + ciclo);
            System.out.println("Madurez: " + madurez);
            
            System.out.println("\nPadres:");
            for (Pez padre : padres) {
                System.out.println("- " + padre.getNombre());
            }
        } else {
            System.out.println("\nEl tanque de cría está vacío.");
        }
    }

    /**
     * Establece la pareja de padres si pertenecen a la misma especie.
     * 
     * @param macho  Pez macho.
     * @param hembra Pez hembra.
     * @return true si se estableció la pareja, false en caso contrario.
     */
    public boolean establecerPadres(Pez macho, Pez hembra) {
        if (macho.getNombre().equals(hembra.getNombre())) {
            padres.clear();
            padres.add(macho);
            padres.add(hembra);
            return true;
        }
        return false;
    }

    /**
     * Devuelve la lista de padres.
     * 
     * @return lista de padres.
     */
    public List<Pez> getPadres() {
        return padres;
    }

    /**
     * Simula el pase de un día en el tanque de cría, intentando reproducir peces
     * si se cumplen las condiciones de fertilidad y hay espacio disponible en
     * los tanques.
     * 
     * @param pisc Piscifactoría donde se encuentra el tanque de cría.
     */
    public void pasarDia(Piscifactoria pisc) {
        boolean alimentoSuficiente = true;
        for (Pez padre : padres) {
            if (padre instanceof Omnivoro) {
                if (pisc.getComidaAnimalActual() >= 1 && pisc.getComidaVegetalActual() >= 1) {
                    pisc.setCantidadComidaAnimal(pisc.getComidaAnimalActual() - 1);
                    pisc.setCantidadComidaVegetal(pisc.getComidaVegetalActual() - 1);
                    padre.setAlimentado(true);
                } else {
                    alimentoSuficiente = false;
                }
            }
            else if (padre instanceof Carnivoro || padre instanceof CarnivoroActivo) {
                if (pisc.getComidaAnimalActual() >= 2) {
                    pisc.setCantidadComidaAnimal(pisc.getComidaAnimalActual() - 2);
                    padre.setAlimentado(true);
                } else {
                    alimentoSuficiente = false;
                }
            }
            else if (padre instanceof Filtrador) {
                if (pisc.getComidaVegetalActual() >= 2) {
                    pisc.setCantidadComidaVegetal(pisc.getComidaVegetalActual() - 2);
                    padre.setAlimentado(true);
                } else {
                    alimentoSuficiente = false;
                }
            }
            else {
                if (pisc.getComidaAnimalActual() >= 2) {
                    pisc.setCantidadComidaAnimal(pisc.getComidaAnimalActual() - 2);
                    padre.setAlimentado(true);
                } else {
                    alimentoSuficiente = false;
                }
            }
        }
        if (!alimentoSuficiente) {
            System.out.println("\nNo hay suficiente comida para alimentar a los peces del tanque de cría. La reproducción se cancela hoy.");
            return;
        }
        
        for (Pez padre : padres) {
            if (!padre.isSexo()) {
                if (padre.getEdad() >= padre.getDatos().getMadurez() && !padre.isMaduro()) {
                    padre.setFertil(true);
                    padre.setMaduro(true);
                } else if (padre.getEdad() >= padre.getDatos().getMadurez()) {
                    padre.setCiclo(padre.getCiclo() - 1);
                    if (padre.getCiclo() <= 0) {
                        padre.setFertil(true);
                        padre.setCiclo(padre.getDatos().getCiclo());
                    }
                } else {
                    padre.setFertil(false);
                }
            }
            else if (padre.getEdad() >= padre.getDatos().getMadurez()) {
                padre.setFertil(true);
                padre.setMaduro(true);
            }
        }
        
        boolean hayMachoFertil = false;
        boolean hayHembraFertil = false;
        for (Pez padre : padres) {
            if (padre.isSexo() && padre.isFertil()) {
                hayMachoFertil = true;
            }
            if (!padre.isSexo() && padre.isFertil()) {
                hayHembraFertil = true;
            }
        }
        if (!hayMachoFertil || !hayHembraFertil) {
            System.out.println("\nNo se cumplen las condiciones de fertilidad para la reproducción en el tanque de cría.");
            return;
        }
        
        Tanque tanqueDestino = null;
        for (Tanque tanque : pisc.getTanques()) {
            if (tanque.getPeces().size() < tanque.getCapacidad() && !tanque.getPeces().isEmpty()) {
                if (tanque.getPeces().get(0).getNombre().equals(padres.get(0).getNombre())) {
                    tanqueDestino = tanque;
                    break;
                }
            }
        }
        if (tanqueDestino == null) {
            System.out.println("\nNo hay tanques disponibles con peces compatibles para alojar las crías. La reproducción se cancela hoy.");
            return;
        }
        
        int numeroHuevos = padres.get(0).getDatos().getHuevos() * 2;
        int espacioDisponible = tanqueDestino.getCapacidad() - tanqueDestino.getPeces().size();
        int huevosProcesables = Math.min(espacioDisponible, numeroHuevos);
        int criasReproducidas = 0;
        
        for (int i = 0; i < huevosProcesables; i++) {
            boolean nuevoSexo = (tanqueDestino.getHembras() <= tanqueDestino.getMachos()) ? false : true;
            Pez nuevaCria = padres.get(0).clonar(nuevoSexo);
            if (tanqueDestino.addFish(nuevaCria)) {
                criasReproducidas++;
                Simulador.estadisticas.registrarNacimiento(nuevaCria.getNombre());
            }
        }
        if (numeroHuevos > espacioDisponible) {
            System.out.println("\nEl tanque destino se ha llenado. Algunas crías no pudieron ser alojadas.");
        }
        
        for (Pez padre : padres) {
            padre.setFertil(false);
            padre.setAlimentado(false);
        }
        
        System.out.println("\nReproducción completada: " + criasReproducidas 
                + " crías añadidas al tanque " + tanqueDestino.getNumeroTanque() + ".");

        Simulador.registro.registroPasarDiaCrias(criasReproducidas, pisc.getNombre());
    }
    
    /**
     * Permite comprar una pareja de peces para el tanque de cría, según los tipos
     * disponibles en la piscifactoría.
     */
    public void comprarPeces(Boolean esDeRio) {
        if (padres.isEmpty()) {
            String[] opcionesPeces = null;
            if (esDeRio) {
                opcionesPeces = new String[] {
                        "Dorada",
                        "Salmón del Atlántico",
                        "Trucha Arcoíris",
                        "Carpa Plateada",
                        "Pejerrey",
                        "Perca Europea",
                        "Salmón Chinook",
                        "Tilapia del Nilo"
                };
            } else {
                opcionesPeces = new String[] {
                        "Dorada",
                        "Salmon del Atlántico",
                        "Trucha Arcoíris",
                        "Arenque del Atlántico",
                        "Besugo",
                        "Lenguado Europeo",
                        "Lubina Rayada",
                        "Robalo"
                };
            }

            System.out.println("\n======================= Comprar Pareja de Peces =======================");
            MenuHelper.mostrarMenuCancelar(opcionesPeces);

            int opcion = InputHelper.solicitarNumero(0, opcionesPeces.length);
            if (opcion == 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            Pez pezMacho = null;
            Pez pezHembra = null;

            switch (opcion) {
                case 1:
                    if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.DORADA.getCoste() * 2)) {
                        pezMacho = new Dorada(true);
                        pezHembra = new Dorada(false);
                    } else {
                        System.out.println("No tienes monedas suficientes para comprar Dorada.");
                        return;
                    }
                    break;
                case 2:
                    if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.SALMON_ATLANTICO.getCoste() * 2)) {
                        pezMacho = new SalmonAtlantico(true);
                        pezHembra = new SalmonAtlantico(false);
                    } else {
                        System.out.println("No tienes monedas suficientes para comprar Salmón del Atlántico.");
                        return;
                    }
                    break;
                case 3:
                    if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.TRUCHA_ARCOIRIS.getCoste() * 2)) {
                        pezMacho = new TruchaArcoiris(true);
                        pezHembra = new TruchaArcoiris(false);
                    } else {
                        System.out.println("No tienes monedas suficientes para comprar Trucha Arcoíris.");
                        return;
                    }
                    break;
                case 4:
                    if (esDeRio) {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.CARPA_PLATEADA.getCoste() * 2)) {
                            pezMacho = new CarpaPlateada(true);
                            pezHembra = new CarpaPlateada(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Carpa Plateada.");
                            return;
                        }
                    } else {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.ARENQUE_ATLANTICO.getCoste() * 2)) {
                            pezMacho = new ArenqueDelAtlantico(true);
                            pezHembra = new ArenqueDelAtlantico(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Arenque del Atlántico.");
                            return;
                        }
                    }
                    break;
                case 5:
                    if (esDeRio) {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.PEJERREY.getCoste() * 2)) {
                            pezMacho = new Pejerrey(true);
                            pezHembra = new Pejerrey(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Pejerrey.");
                            return;
                        }
                    } else {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.BESUGO.getCoste() * 2)) {
                            pezMacho = new Besugo(true);
                            pezHembra = new Besugo(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Besugo.");
                            return;
                        }
                    }
                    break;
                case 6:
                    if (esDeRio) {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.PERCA_EUROPEA.getCoste() * 2)) {
                            pezMacho = new PercaEuropea(true);
                            pezHembra = new PercaEuropea(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Perca Europea.");
                            return;
                        }
                    } else {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.LENGUADO_EUROPEO.getCoste() * 2)) {
                            pezMacho = new LenguadoEuropeo(true);
                            pezHembra = new LenguadoEuropeo(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Lenguado Europeo.");
                            return;
                        }
                    }
                    break;
                case 7:
                    if (esDeRio) {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.SALMON_CHINOOK.getCoste() * 2)) {
                            pezMacho = new SalmonChinook(true);
                            pezHembra = new SalmonChinook(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Salmón Chinook.");
                            return;
                        }
                    } else {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.LUBINA_RAYADA.getCoste() * 2)) {
                            pezMacho = new LubinaRayada(true);
                            pezHembra = new LubinaRayada(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Lubina Rayada.");
                            return;
                        }
                    }
                    break;
                case 8:
                    if (esDeRio) {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.TILAPIA_NILO.getCoste() * 2)) {
                            pezMacho = new TilapiaDelNilo(true);
                            pezHembra = new TilapiaDelNilo(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Tilapia del Nilo.");
                            return;
                        }
                    } else {
                        if (Simulador.monedas.ganarMonedas(AlmacenPropiedades.ROBALO.getCoste() * 2)) {
                            pezMacho = new Robalo(true);
                            pezHembra = new Robalo(false);
                        } else {
                            System.out.println("No tienes monedas suficientes para comprar Robalo.");
                            return;
                        }
                    }
                    break;
                default:
                    System.out.println("Opción inválida.");
                    return;
            }

            if (this.establecerPadres(pezMacho, pezHembra)) {
                System.out.println("\nPareja de " + pezMacho.getNombre() + " comprada y añadida al tanque de cría.");
            } else {
                System.out.println("Error al establecer la pareja de peces.");
            }

        } else {
            System.out.println("El tanque ya contiene peces. No se puede comprar una nueva pareja.");
        }
    }
}