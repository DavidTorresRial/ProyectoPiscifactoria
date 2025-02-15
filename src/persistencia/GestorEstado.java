package persistencia;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import commons.Simulador;
import edificios.AlmacenCentral;
import estadisticas.Estadisticas;
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

    /**
     * Guarda el estado actual del simulador en un archivo JSON en la carpeta "saves".
     * 
     * @param simulador El objeto del simulador que se va a guardar.
     */
    public static void guardarEstado(Simulador simulador) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Crear estructura principal con LinkedHashMap para preservar el orden
        Map<String, Object> estado = new LinkedHashMap<>();

        estado.put("implementados", simulador.getPecesImplementados());
        estado.put("empresa", simulador.getNombreEntidad());
        estado.put("dia", simulador.getDia());
        estado.put("monedas", Simulador.monedas.getMonedas());
        estado.put("orca", Simulador.estadisticas.exportarDatos(simulador.getPecesImplementados()));

        // Almacén
        Map<String, Object> almacenMap = new LinkedHashMap<>();
        AlmacenCentral almacenCentral = Simulador.almacenCentral;
        almacenMap.put("disponible", almacenCentral != null && almacenCentral.getCapacidadAlmacen() > 0);
        almacenMap.put("capacidad", almacenCentral != null ? almacenCentral.getCapacidadAlmacen() : 200);
        almacenMap.put("comida", Map.of(
                "vegetal", almacenCentral != null ? almacenCentral.getCantidadComidaVegetal() : 0,
                "animal", almacenCentral != null ? almacenCentral.getCantidadComidaAnimal() : 0));
        estado.put("edificios", Map.of("almacen", almacenMap));

        // Piscifactorías
        List<Map<String, Object>> piscifactoriasList = new ArrayList<>();
        for (Piscifactoria piscifactoria : simulador.getPiscifactorias()) {
            Map<String, Object> piscifactoriaMap = new LinkedHashMap<>();
            piscifactoriaMap.put("nombre", piscifactoria.getNombre());
            piscifactoriaMap.put("tipo", piscifactoria instanceof PiscifactoriaDeRio ? 0 : 1);
            piscifactoriaMap.put("capacidad", piscifactoria.getCapacidadMaximaComida());
            piscifactoriaMap.put("comida", Map.of(
                    "vegetal", piscifactoria.getComidaVegetalActual(),
                    "animal", piscifactoria.getComidaAnimalActual()));

            // Tanques
            List<Map<String, Object>> tanquesList = new ArrayList<>();
            for (Tanque tanque : piscifactoria.getTanques()) {
                Map<String, Object> tanqueMap = new LinkedHashMap<>();
                if (!tanque.getPeces().isEmpty()) {
                    tanqueMap.put("pez", tanque.getPeces().get(0).getNombre());
                    tanqueMap.put("num", tanque.getNumeroTanque());
                    tanqueMap.put("datos", Map.of(
                            "vivos", tanque.getPeces().size(),
                            "maduros", tanque.getMaduros(),
                            "fertiles", tanque.getFertiles()));

                    // Peces
                    List<Map<String, Object>> pecesList = new ArrayList<>();
                    for (Pez pez : tanque.getPeces()) {
                        Map<String, Object> pezMap = new LinkedHashMap<>();
                        pezMap.put("edad", pez.getEdad());
                        pezMap.put("sexo", pez.isSexo());
                        pezMap.put("vivo", pez.isVivo());
                        pezMap.put("maduro", pez.isMaduro());
                        pezMap.put("fertil", pez.isFertil());
                        pezMap.put("ciclo", pez.getDatos().getCiclo());
                        pezMap.put("alimentado", pez.isAlimentado());
                        pecesList.add(pezMap);
                    }
                    tanqueMap.put("peces", pecesList);
                } else {
                    tanqueMap.put("pez", "Sin peces");
                    tanqueMap.put("num", tanque.getNumeroTanque());
                    tanqueMap.put("datos", "No disponible");
                }
                tanquesList.add(tanqueMap);
            }
            piscifactoriaMap.put("tanques", tanquesList);
            piscifactoriasList.add(piscifactoriaMap);
        }
        estado.put("piscifactorias", piscifactoriasList);

        try (FileWriter writer = new FileWriter("saves/" + simulador.getNombreEntidad() + ".save")) {
            gson.toJson(estado, writer);
            Simulador.registro.registroGuardarSistema();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga los datos de una partida desde un archivo y los aplica al simulador.
     *
     * @param simulador      El simulador donde se cargarán los datos.
     * @param archivoPartida El archivo que contiene la partida guardada.
     */
    public static void load(Simulador simulador, String archivoPartida) {
        try {
            // Leer el archivo JSON
            FileReader reader = new FileReader("saves/" + archivoPartida + ".save");
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            // Cargar empresa
            simulador.setNombreEntidad(jsonObject.has("empresa") && !jsonObject.get("empresa").isJsonNull()
                    ? jsonObject.get("empresa").getAsString()
                    : "Empresa desconocida");

            // Cargar día
            simulador.setDia(jsonObject.has("dia") && !jsonObject.get("dia").isJsonNull()
                    ? jsonObject.get("dia").getAsInt()
                    : 0);

            // Cargar monedas
            Simulador.monedas.ganarMonedas(jsonObject.has("monedas") && !jsonObject.get("monedas").isJsonNull()
                    ? jsonObject.get("monedas").getAsInt()
                    : 0);

            // Cargar Almacén
            if (jsonObject.has("edificios") && !jsonObject.get("edificios").isJsonNull()) {
                JsonObject edificios = jsonObject.getAsJsonObject("edificios");
                if (edificios.has("almacen") && !edificios.get("almacen").isJsonNull()) {
                    JsonObject almacen = edificios.getAsJsonObject("almacen");

                    // Verificar si el almacén está disponible
                    boolean disponible = almacen.get("disponible").getAsBoolean();
                    if (disponible) {
                        int capacidadAlmacen = almacen.get("capacidad").getAsInt();
                        JsonObject comida = almacen.getAsJsonObject("comida");
                        int comidaVegetal = comida.get("vegetal").getAsInt();
                        int comidaAnimal = comida.get("animal").getAsInt();
                        Simulador.almacenCentral = new AlmacenCentral(capacidadAlmacen, comidaAnimal, comidaVegetal);
                    } else {
                        Simulador.almacenCentral = null;
                    }
                }
            }

            // Procesar 'orca' y crear el objeto Estadisticas
            if (jsonObject.has("orca") && !jsonObject.get("orca").isJsonNull()) {
                String orcaData = jsonObject.get("orca").getAsString();
                simulador.setEstadisticas(new Estadisticas(simulador.getPecesImplementados(), orcaData));
            }

            // Cargar piscifactorías
            if (jsonObject.has("piscifactorias") && !jsonObject.get("piscifactorias").isJsonNull()) {
                simulador.getPiscifactorias().clear();
                JsonArray piscifactoriasArray = jsonObject.getAsJsonArray("piscifactorias");

                for (JsonElement piscifactoriaElement : piscifactoriasArray) {
                    JsonObject piscifactoriaJson = piscifactoriaElement.getAsJsonObject();

                    // Crear piscifactoría
                    String nombre = piscifactoriaJson.get("nombre").getAsString();
                    int tipo = piscifactoriaJson.get("tipo").getAsInt();

                    int capacidadMaxima = piscifactoriaJson.get("capacidad").getAsInt();

                    JsonObject comida = piscifactoriaJson.getAsJsonObject("comida");
                    int comidaVegetal = comida.get("vegetal").getAsInt();
                    int comidaAnimal = comida.get("animal").getAsInt();

                    Piscifactoria piscifactoria = (tipo == 0)
                            ? new PiscifactoriaDeRio(nombre, capacidadMaxima, comidaVegetal, comidaAnimal)
                            : new PiscifactoriaDeMar(nombre, capacidadMaxima, comidaVegetal, comidaAnimal);

                    // Cargar tanques y peces
                    piscifactoria.getTanques().clear();
                    JsonArray tanquesArray = piscifactoriaJson.getAsJsonArray("tanques");

                    for (JsonElement tanqueElement : tanquesArray) {
                        JsonObject tanqueJson = tanqueElement.getAsJsonObject();
                        Tanque tanque = new Tanque(
                                tanqueJson.get("num").getAsInt(),
                                piscifactoria instanceof PiscifactoriaDeRio ? 25 : 100);

                        // Cargar peces directamente aquí
                        if (tanqueJson.has("peces") && !tanqueJson.get("peces").isJsonNull()) {
                            JsonArray pecesArray = tanqueJson.getAsJsonArray("peces");
                            for (JsonElement pezElement : pecesArray) {
                                JsonObject pezJson = pezElement.getAsJsonObject();

                                // Crear pez según el tipo y sexo
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

                                tanque.getPeces().add(pez);
                            }
                        }

                        piscifactoria.getTanques().add(tanque);
                    }

                    simulador.getPiscifactorias().add(piscifactoria);
                }
            }

            System.out.println("\nPartida cargada: " + archivoPartida);
            Simulador.registro.registroCargarSistema();
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
    }
}