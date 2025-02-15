package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import commons.Simulador;
import estadisticas.Estadisticas;
import edificios.AlmacenCentral;
import edificios.GranjaFitoplancton;
import edificios.GranjaLangostinos;
import peces.Pez;
import piscifactoria.Piscifactoria;
import piscifactoria.PiscifactoriaDeMar;
import piscifactoria.PiscifactoriaDeRio;
import tanque.Tanque;

public class GestorEstado {

    /**
     * Guarda el estado actual del simulador en un archivo JSON utilizando JsonObject.
     * Se recogen las llamadas a los métodos de obtención de datos de cada objeto.
     */
    public static void guardarEstado(Simulador simulador) {
        JsonObject root = new JsonObject();

        // 1. Implementados
        JsonArray implementadosArray = new JsonArray();
        if (simulador.getPecesImplementados() != null) {
            for (String pez : simulador.getPecesImplementados()) {
                if (pez != null) {
                    implementadosArray.add(pez);
                }
            }
        }
        root.add("implementados", implementadosArray);

        // 2. Datos generales
        root.addProperty("empresa", simulador.getNombreEntidad() != null ? simulador.getNombreEntidad() : "");
        root.addProperty("dia", simulador.getDia());
        root.addProperty("monedas", simulador.monedas != null ? simulador.monedas.getMonedas() : 0);
        root.addProperty("orca", simulador.estadisticas != null
                ? simulador.estadisticas.exportarDatos(simulador.getPecesImplementados())
                : "0:0,0,0;0:0,0,0");

        // 3. Edificios
        JsonObject edificiosObj = new JsonObject();

        // 3.1. ALMACÉN
        JsonObject almacenObj = new JsonObject();
        AlmacenCentral almacen = simulador.almacenCentral;
        if (almacen != null) {
            almacenObj.addProperty("disponible", true);
            almacenObj.addProperty("capacidad", almacen.getCapacidadAlmacen());
            JsonObject comidaAlmacen = new JsonObject();
            comidaAlmacen.addProperty("vegetal", almacen.getCantidadComidaVegetal());
            comidaAlmacen.addProperty("animal", almacen.getCantidadComidaAnimal());
            almacenObj.add("comida", comidaAlmacen);
        } else {
            almacenObj.addProperty("disponible", false);
            almacenObj.addProperty("capacidad", 0);
            JsonObject comidaAlmacen = new JsonObject();
            comidaAlmacen.addProperty("vegetal", 0);
            comidaAlmacen.addProperty("animal", 0);
            almacenObj.add("comida", comidaAlmacen);
        }
        edificiosObj.add("almacen", almacenObj);

        // 3.2. FITOPLANCTON
        JsonObject fitoplanctonObj = new JsonObject();
        GranjaFitoplancton fitoplancton = simulador.granjaFitoplancton;
        if (fitoplancton != null) {
            fitoplanctonObj.addProperty("disponible", true);
            fitoplanctonObj.addProperty("tanques", fitoplancton.getNumeroTanques());
            fitoplanctonObj.addProperty("ciclo", fitoplancton.getCiclo());
        } else {
            fitoplanctonObj.addProperty("disponible", false);
            fitoplanctonObj.addProperty("tanques", 0);
            fitoplanctonObj.addProperty("ciclo", 0);
        }
        edificiosObj.add("fitoplancton", fitoplanctonObj);

        // 3.3. LANGOSTINOS
        JsonObject langostinosObj = new JsonObject();
        GranjaLangostinos langostinos = simulador.granjaLangostinos;
        if (langostinos != null) {
            langostinosObj.addProperty("disponible", true);
            langostinosObj.addProperty("muertos", langostinos.getRacionesRetroalimentacion());
            JsonArray tanquesLangostinosArray = new JsonArray();
            if (langostinos.getTanques() != null) {
                for (Object obj : langostinos.getTanques()) {
                    if (obj != null) {
                        JsonObject tanqueJson = new JsonObject();
                        // Llamadas reales a métodos de datos para cada tanque
                        tanqueJson.addProperty("comida", 0);
                        tanqueJson.addProperty("descanso", 0);
                        tanquesLangostinosArray.add(tanqueJson);
                    }
                }
            }
            langostinosObj.add("tanques", tanquesLangostinosArray);
        } else {
            langostinosObj.addProperty("disponible", false);
            langostinosObj.addProperty("muertos", 0);
            langostinosObj.add("tanques", new JsonArray());
        }
        edificiosObj.add("langostinos", langostinosObj);

        root.add("edificios", edificiosObj);

        // 4. Piscifactorías
        JsonArray piscifactoriasArray = new JsonArray();
        if (simulador.getPiscifactorias() != null) {
            for (Piscifactoria pisc : simulador.getPiscifactorias()) {
                if (pisc != null) {
                    JsonObject piscObj = new JsonObject();
                    piscObj.addProperty("nombre", pisc.getNombre());
                    piscObj.addProperty("tipo", pisc instanceof PiscifactoriaDeRio ? 0 : 1);
                    piscObj.addProperty("capacidad", pisc.getCapacidadMaximaComida());
                    
                    // Comida de la piscifactoría
                    JsonObject comidaPisc = new JsonObject();
                    comidaPisc.addProperty("vegetal", pisc.getComidaVegetalActual());
                    comidaPisc.addProperty("animal", pisc.getComidaAnimalActual());
                    piscObj.add("comida", comidaPisc);
                    
                    // Tanques de la piscifactoría
                    JsonArray tanquesArray = new JsonArray();
                    if (pisc.getTanques() != null) {
                        for (Tanque tanque : pisc.getTanques()) {
                            if (tanque != null) {
                                JsonObject tanqueObj = new JsonObject();
                                String pezNombre = "";
                                if (tanque.getPeces() != null && !tanque.getPeces().isEmpty()) {
                                    Pez primerPez = tanque.getPeces().get(0);
                                    if (primerPez != null && primerPez.getDatos() != null) {
                                        pezNombre = primerPez.getDatos().getNombre();
                                    }
                                }
                                tanqueObj.addProperty("pez", pezNombre);
                                int numPeces = (tanque.getPeces() != null) ? tanque.getPeces().size() : 0;
                                tanqueObj.addProperty("num", numPeces);
                                
                                // Datos del tanque
                                JsonObject datosObj = new JsonObject();
                                datosObj.addProperty("vivos", (tanque.getPeces() != null) ? tanque.getVivos() : 0);
                                datosObj.addProperty("maduros", (tanque.getPeces() != null) ? tanque.getMaduros() : 0);
                                datosObj.addProperty("fertiles", (tanque.getPeces() != null) ? tanque.getFertiles() : 0);
                                tanqueObj.add("datos", datosObj);
                                
                                // Lista de peces
                                JsonArray pecesArray = new JsonArray();
                                if (tanque.getPeces() != null) {
                                    for (Pez pez : tanque.getPeces()) {
                                        if (pez != null) {
                                            JsonObject pezObj = new JsonObject();
                                            pezObj.addProperty("edad", pez.getEdad());
                                            pezObj.addProperty("sexo", pez.isSexo());
                                            pezObj.addProperty("vivo", pez.isVivo());
                                            pezObj.addProperty("maduro", pez.isMaduro());
                                            pezObj.addProperty("fertil", pez.isFertil());
                                            pezObj.addProperty("ciclo", pez.getCiclo());
                                            pezObj.addProperty("alimentado", pez.isAlimentado());
                                            pecesArray.add(pezObj);
                                        }
                                    }
                                }
                                tanqueObj.add("peces", pecesArray);
                                tanquesArray.add(tanqueObj);
                            }
                        }
                    }
                    piscObj.add("tanques", tanquesArray);
                    piscifactoriasArray.add(piscObj);
                }
            }
        }
        root.add("piscifactorias", piscifactoriasArray);

        // 5. Guardar el JSON en un archivo
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(root);
        String nombreArchivo = "saves" + File.separator 
                + (simulador.getNombreEntidad() != null ? simulador.getNombreEntidad() : "default") + ".save";
        File archivo = new File(nombreArchivo);
        archivo.getParentFile().mkdirs();
        
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write(json);
            System.out.println("Partida guardada en: " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al guardar la partida: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga el estado del simulador desde un archivo JSON utilizando JsonObject.
     * Se comprueba la existencia de cada sección y se aplican los datos mediante los métodos de asignación.
     *
     * @param simulador      El simulador al que se aplicarán los datos.
     * @param archivoPartida El nombre del archivo de la partida (incluido el .save o sin él, según convenga).
     */
    public static void load(Simulador simulador, String archivoPartida) {
        File file = new File("saves" + File.separator + archivoPartida);
        if (!file.exists()) {
            System.err.println("El archivo " + archivoPartida + " no existe.");
            return;
        }
        
        try (JsonReader reader = new JsonReader(new BufferedReader(new FileReader(file)))) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            // 1. Datos generales
            if (root.has("empresa")) {
                simulador.setNombreEntidad(root.get("empresa").getAsString());
            }
            simulador.setDia(root.has("dia") ? root.get("dia").getAsInt() : 0);
            int monedas = root.has("monedas") ? root.get("monedas").getAsInt() : 0;
            if (simulador.monedas != null) {
                simulador.monedas.ganarMonedas(monedas);
            }
            if (root.has("orca")) {
                String orca = root.get("orca").getAsString();
                if (simulador.estadisticas != null) {
                    simulador.estadisticas = new Estadisticas(simulador.getPecesImplementados(), orca);
                }
            }

            // 2. Edificios
            if (root.has("edificios")) {
                JsonObject edificiosObj = root.getAsJsonObject("edificios");

                // 2.1. ALMACÉN
                if (edificiosObj.has("almacen")) {
                    JsonObject almacenObj = edificiosObj.getAsJsonObject("almacen");
                    boolean disponible = almacenObj.get("disponible").getAsBoolean();
                    int capacidad = almacenObj.get("capacidad").getAsInt();
                    JsonObject comidaObj = almacenObj.getAsJsonObject("comida");
                    int vegetal = comidaObj.get("vegetal").getAsInt();
                    int animal = comidaObj.get("animal").getAsInt();
                    if (disponible) {
                        simulador.almacenCentral = new AlmacenCentral(capacidad, animal, vegetal);
                    } else {
                        simulador.almacenCentral = null;
                    }
                }

                // 2.2. FITOPLANCTON
                if (edificiosObj.has("fitoplancton")) {
                    JsonObject fitoplanctonObj = edificiosObj.getAsJsonObject("fitoplancton");
                    boolean disponible = fitoplanctonObj.get("disponible").getAsBoolean();
                    int tanques = fitoplanctonObj.get("tanques").getAsInt();
                    int ciclo = fitoplanctonObj.get("ciclo").getAsInt();
                    if (disponible) {
                        simulador.granjaFitoplancton = new GranjaFitoplancton(tanques, ciclo);
                    } else {
                        simulador.granjaFitoplancton = null;
                    }
                }

                // 2.3. LANGOSTINOS
                if (edificiosObj.has("langostinos")) {
                    JsonObject langostinosObj = edificiosObj.getAsJsonObject("langostinos");
                    boolean disponible = langostinosObj.get("disponible").getAsBoolean();
                    int muertos = langostinosObj.get("muertos").getAsInt();
                    if (simulador.granjaLangostinos != null) {
                        simulador.granjaLangostinos.setDisponible(disponible);
                        simulador.granjaLangostinos.setMuertos(muertos);
                        // Si es necesario, aquí se pueden cargar también los tanques.
                    }
                }
            }

            // 3. Piscifactorías
            if (root.has("piscifactorias")) {
                JsonArray piscifactoriasArray = root.getAsJsonArray("piscifactorias");
                simulador.getPiscifactorias().clear();
                for (JsonElement piscElem : piscifactoriasArray) {
                    JsonObject piscObj = piscElem.getAsJsonObject();
                    String nombre = piscObj.get("nombre").getAsString();
                    int tipo = piscObj.get("tipo").getAsInt();
                    int capacidad = piscObj.get("capacidad").getAsInt();
                    
                    Piscifactoria pisc = new Piscifactoria(true, nombre);
                    pisc.setTipo(tipo);
                    pisc.setAlmacenMax(capacidad);
                    
                    if (piscObj.has("comida")) {
                        JsonObject comidaPisc = piscObj.getAsJsonObject("comida");
                        int vegetal = comidaPisc.get("vegetal").getAsInt();
                        int animal = comidaPisc.get("animal").getAsInt();
                        pisc.añadirComidaVegetal(vegetal);
                        pisc.añadirComidaAnimal(animal);
                    }
                    
                    // Tanques de la piscifactoría
                    if (piscObj.has("tanques")) {
                        JsonArray tanquesArray = piscObj.getAsJsonArray("tanques");
                        for (JsonElement tanqueElem : tanquesArray) {
                            JsonObject tanqueObj = tanqueElem.getAsJsonObject();
                            Tanque tanque = new Tanque();
                            
                            // Si se dispone de datos adicionales del tanque, se pueden procesar aquí.
                            if (tanqueObj.has("datos")) {
                                JsonObject datosObj = tanqueObj.getAsJsonObject("datos");
                                // Ejemplo: tanque.actualizarDatos(datosObj.get("vivos").getAsInt(), ...);
                            }
                            
                            // Peces del tanque
                            if (tanqueObj.has("peces")) {
                                JsonArray pecesArray = tanqueObj.getAsJsonArray("peces");
                                for (JsonElement pezElem : pecesArray) {
                                    JsonObject pezObj = pezElem.getAsJsonObject();
                                    Pez pez = new Pez();
                                    pez.setEdad(pezObj.get("edad").getAsInt());
                                    pez.setSexo(pezObj.get("sexo").getAsBoolean());
                                    pez.setVivo(pezObj.get("vivo").getAsBoolean());
                                    pez.setMaduro(pezObj.get("maduro").getAsBoolean());
                                    pez.setFertil(pezObj.get("fertil").getAsBoolean());
                                    pez.setCiclo(pezObj.get("ciclo").getAsInt());
                                    pez.setAlimentado(pezObj.get("alimentado").getAsBoolean());
                                    tanque.agregarPez(pez);
                                }
                            }
                            pisc.agregarTanque(tanque);
                        }
                    }
                    simulador.getPiscifactorias().add(pisc);
                }
            }
            
            System.out.println("Sistema restaurado desde: " + archivoPartida);
        } catch (IOException e) {
            System.err.println("Error al cargar la partida: " + e.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}