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
import java.util.ArrayList;
import java.util.List;

import commons.Simulador;
import estadisticas.Estadisticas;
import edificios.AlmacenCentral;
import edificios.GranjaFitoplancton;
import edificios.GranjaLangostinos;
import edificios.TanqueLangostinos;
import peces.Pez;
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
import piscifactoria.PiscifactoriaDeMar;
import piscifactoria.PiscifactoriaDeRio;
import tanque.Tanque;

public class GestorEstado {

    /** Guarda el estado actual del simulador en un archivo JSON utilizando JsonObject. */
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
        root.addProperty("monedas", Simulador.monedas != null ? Simulador.monedas.getMonedas() : 0);
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
                // Se recorre la lista de tanques externos
                for (TanqueLangostinos tanque : langostinos.getTanques()) {
                    if (tanque != null) {
                        JsonObject tanqueJson = new JsonObject();
                        tanqueJson.addProperty("comida", tanque.getRacionesLocal());
                        tanqueJson.addProperty("descanso", tanque.getDiasPenalizacion());
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
                    // Usamos 0 para Río y 1 para Mar
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
                                // Guardamos el identificador real del tanque
                                tanqueObj.addProperty("num", tanque.getNumeroTanque());
                                
                                // Guardamos el tipo de pez (suponiendo que el primer pez define el tipo)
                                String pezNombre = "";
                                if (tanque.getPeces() != null && !tanque.getPeces().isEmpty()) {
                                    Pez primerPez = tanque.getPeces().get(0);
                                    if (primerPez != null && primerPez.getDatos() != null) {
                                        pezNombre = primerPez.getDatos().getNombre();
                                    }
                                }
                                tanqueObj.addProperty("pez", pezNombre);
                                
                                // Datos del tanque
                                JsonObject datosObj = new JsonObject();
                                datosObj.addProperty("vivos", tanque.getPeces() != null ? tanque.getVivos() : 0);
                                datosObj.addProperty("maduros", tanque.getPeces() != null ? tanque.getMaduros() : 0);
                                datosObj.addProperty("fertiles", tanque.getPeces() != null ? tanque.getFertiles() : 0);
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
            Simulador.instance.registro.registroGuardarSistema();
        } catch (IOException e) {
            Simulador.instance.registro.registroLogError("Error al guardar la partida: " + e.getMessage());
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
        File file = new File("saves/" + archivoPartida + ".save");
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
            if (Simulador.monedas != null) {
                Simulador.monedas.ganarMonedas(monedas);
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
                    if (disponible) {
                        JsonArray tanquesArray = langostinosObj.getAsJsonArray("tanques");
                        List<TanqueLangostinos> tanquesList = new ArrayList<>();
                        for (JsonElement elem : tanquesArray) {
                            JsonObject tanqueObj = elem.getAsJsonObject();
                            int comida = tanqueObj.get("comida").getAsInt();
                            int descanso = tanqueObj.get("descanso").getAsInt();
                            // Creamos el tanque sin referencia a la granja (se asignará posteriormente)
                            TanqueLangostinos tanque = new TanqueLangostinos(comida, descanso);
                            tanquesList.add(tanque);
                        }
                        // Se crea la granja usando el constructor que ya tienes
                        GranjaLangostinos granjaLangostinos = new GranjaLangostinos(muertos, tanquesList);
                        // Aseguramos que cada tanque tenga asignada la referencia a su granja
                        for (TanqueLangostinos tanque : tanquesList) {
                            tanque.setGranja(granjaLangostinos);
                        }
                        simulador.granjaLangostinos = granjaLangostinos;
                    } else {
                        simulador.granjaLangostinos = null;
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
                    
                    int comidaVegetal = 0, comidaAnimal = 0;
                    if (piscObj.has("comida")) {
                        JsonObject comidaPisc = piscObj.getAsJsonObject("comida");
                        comidaVegetal = comidaPisc.get("vegetal").getAsInt();
                        comidaAnimal = comidaPisc.get("animal").getAsInt();
                    }
                    
                    Piscifactoria pisc;
                    if (tipo == 0) {
                        pisc = new PiscifactoriaDeRio(nombre, capacidad, comidaVegetal, comidaAnimal);
                    } else {
                        pisc = new PiscifactoriaDeMar(nombre, capacidad, comidaVegetal, comidaAnimal);
                    }
                    
                    pisc.getTanques().clear();
                    JsonArray tanquesArray = piscObj.getAsJsonArray("tanques");
                    for (JsonElement tanqueElem : tanquesArray) {
                        JsonObject tanqueJson = tanqueElem.getAsJsonObject();

                        Tanque tanque = new Tanque(
                                tanqueJson.get("num").getAsInt(),
                                pisc instanceof PiscifactoriaDeRio ? 25 : 100);
                        
                        if (tanqueJson.has("peces") && !tanqueJson.get("peces").isJsonNull()) {
                            JsonArray pecesArray = tanqueJson.getAsJsonArray("peces");
                            for (JsonElement pezElement : pecesArray) {
                                JsonObject pezJson = pezElement.getAsJsonObject();
                                // Recuperar el tipo de pez desde la propiedad "pez" del tanque
                                String tipoPez = tanqueJson.has("pez") ? tanqueJson.get("pez").getAsString() : "Desconocido";
                                boolean sexo = pezJson.get("sexo").getAsBoolean();
                                int edad = pezJson.get("edad").getAsInt();
                                boolean vivo = pezJson.get("vivo").getAsBoolean();
                                boolean fertil = pezJson.get("fertil").getAsBoolean();
                                int ciclo = pezJson.get("ciclo").getAsInt();
                                boolean alimentado = pezJson.get("alimentado").getAsBoolean();
                                
                                Pez pez = switch (tipoPez) {
                                    case "Dorada" -> new Dorada(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Salmón atlántico" -> new SalmonAtlantico(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Trucha arcoíris" -> new TruchaArcoiris(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Carpa plateada" -> new CarpaPlateada(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Pejerrey" -> new Pejerrey(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Perca europea" -> new PercaEuropea(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Salmón chinook" -> new SalmonChinook(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Tilapia del Nilo" -> new TilapiaDelNilo(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Arenque del Atlántico" -> new ArenqueDelAtlantico(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Besugo" -> new Besugo(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Lenguado Europeo" -> new LenguadoEuropeo(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Lubina Rayada" -> new LubinaRayada(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    case "Róbalo" -> new Robalo(sexo, edad, vivo, fertil, ciclo, alimentado);
                                    default -> null;
                                };

                                if (pez != null) {
                                    tanque.getPeces().add(pez);
                                }
                            }
                        }
                        pisc.getTanques().add(tanque);
                    }
                    simulador.getPiscifactorias().add(pisc);
                }
            }
            
            Simulador.instance.registro.registroCargarSistema();
        } catch (IOException e) {
            Simulador.instance.registro.registroLogError("Error al cargar la partida: " + e.getMessage());
        } catch (Exception e) {
            Simulador.instance.registro.registroLogError("Error al cargar la partida: " + e.getMessage());
        }
    }
}