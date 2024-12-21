package persistencia;

import java.io.FileReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import commons.AlmacenCentral;
import commons.Simulador;

import estadisticas.Estadisticas;

import piscifactoria.Piscifactoria;
import piscifactoria.PiscifactoriaDeMar;
import piscifactoria.PiscifactoriaDeRio;

import tanque.Tanque;
import peces.Pez;

import peces.tipos.doble.*;
import peces.tipos.rio.*;
import peces.tipos.mar.*;

public class CargarEstado {
    public static void cargar(Simulador simulador, String archivoPartida) {
        try {
            // Leer el archivo JSON
            FileReader reader = new FileReader("saves/" + archivoPartida + ".save");
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            // Cargar empresa
            simulador.setNombreEntidad(jsonObject.has("empresa") && !jsonObject.get("empresa").isJsonNull()
                    ? jsonObject.get("empresa").getAsString()
                    : "Empresa desconocida");

            // Cargar día y monedas
            simulador.setDia(jsonObject.has("dia") && !jsonObject.get("dia").isJsonNull()
                    ? jsonObject.get("dia").getAsInt()
                    : 0);
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
                        AlmacenCentral almacenCentral = new AlmacenCentral();
                        almacenCentral.setCantidadComidaVegetal(comidaVegetal);
                        almacenCentral.setCantidadComidaAnimal(comidaAnimal);
                        almacenCentral.setCapacidadAlmacen(capacidadAlmacen);
                    }
                }
            }
            
            // Procesar 'orca' y crear el objeto Estadisticas
            if (jsonObject.has("orca") && !jsonObject.get("orca").isJsonNull()) {
                String orcaData = jsonObject.get("orca").getAsString();
                Simulador.estadisticas = new Estadisticas(simulador.getPecesImplementados(), orcaData);
            }
            
            // Cargar piscifactorías
            if (jsonObject.has("piscifactorias") && !jsonObject.get("piscifactorias").isJsonNull()) {
                simulador.getPiscifactorias().clear(); // Limpia la lista antes de cargar
                JsonArray piscifactoriasArray = jsonObject.getAsJsonArray("piscifactorias");

                for (JsonElement piscifactoriaElement : piscifactoriasArray) {
                    JsonObject piscifactoriaJson = piscifactoriaElement.getAsJsonObject();

                    // Crear piscifactoría
                    String nombre = piscifactoriaJson.get("nombre").getAsString();
                    int tipo = piscifactoriaJson.get("tipo").getAsInt();
                    Piscifactoria piscifactoria = (tipo == 0)
                            ? new PiscifactoriaDeRio(nombre)
                            : new PiscifactoriaDeMar(nombre);

                    // Configurar capacidades
                    int capacidadMaxima = piscifactoriaJson.get("capacidad").getAsInt();
                    piscifactoria.setCapacidadMaximaComida(capacidadMaxima);

                    // Configurar cantidades de comida
                    JsonObject comida = piscifactoriaJson.getAsJsonObject("comida");
                    int comidaVegetal = comida.get("vegetal").getAsInt();
                    int comidaAnimal = comida.get("animal").getAsInt();
                    piscifactoria.setCantidadComidaVegetal(comidaVegetal);
                    piscifactoria.setCantidadComidaAnimal(comidaAnimal);

                    // Cargar tanques y peces
                    piscifactoria.getTanques().clear(); // Limpia tanques existentes para evitar duplicados
                    JsonArray tanquesArray = piscifactoriaJson.getAsJsonArray("tanques");

                    for (JsonElement tanqueElement : tanquesArray) {
                        JsonObject tanqueJson = tanqueElement.getAsJsonObject();
                        Tanque tanque;
                        if (piscifactoria instanceof PiscifactoriaDeRio) {
                            tanque = new Tanque(
                                    tanqueJson.get("num").getAsInt(),
                                    25
                            );
                        } else if (piscifactoria instanceof PiscifactoriaDeMar) {
                            tanque = new Tanque(
                                    tanqueJson.get("num").getAsInt(),
                                    100
                            );
                        } else {
                            tanque = null;
                        }

                        // Cargar peces directamente aquí
                        if (tanqueJson.has("peces") && !tanqueJson.get("peces").isJsonNull()) {
                            JsonArray pecesArray = tanqueJson.getAsJsonArray("peces");
                            for (JsonElement pezElement : pecesArray) {
                                JsonObject pezJson = pezElement.getAsJsonObject();

                                // Crear pez según el tipo y sexo
                                String tipoPez = tanqueJson.has("pez") ? tanqueJson.get("pez").getAsString() : "Desconocido";
                                boolean sexo = pezJson.get("sexo").getAsBoolean();
                                Pez pez = switch (tipoPez) {
                                    case "Dorada" -> new Dorada(sexo);
                                    case "Salm\u00f3n atl\u00e1ntico" -> new SalmonAtlantico(sexo);
                                    case "Trucha arco\u00edris" -> new TruchaArcoiris(sexo);
                                    case "Carpa plateada" -> new CarpaPlateada(sexo);
                                    case "Pejerrey" -> new Pejerrey(sexo);
                                    case "Perca europea" -> new PercaEuropea(sexo);
                                    case "Salm\u00f3n chinook" -> new SalmonChinook(sexo);
                                    case "Tilapia del Nilo" -> new TilapiaDelNilo(sexo);
                                    case "Arenque del Atl\u00e1ntico" -> new ArenqueDelAtlantico(sexo);
                                    case "Besugo" -> new Besugo(sexo);
                                    case "Lenguado Europeo" -> new LenguadoEuropeo(sexo);
                                    case "Lubina Rayada" -> new LubinaRayada(sexo);
                                    case "R\u00f3balo" -> new Robalo(sexo);
                                    default -> null;
                                };

                                if (pez != null) {
                                    pez.setEdad(pezJson.get("edad").getAsInt());
                                    pez.setVivo(pezJson.get("vivo").getAsBoolean());
                                    pez.setFertil(pezJson.get("fertil").getAsBoolean());
                                    pez.setCiclo(pezJson.get("ciclo").getAsInt());
                                    pez.setAlimentado(pezJson.get("alimentado").getAsBoolean());
                                    tanque.getPeces().add(pez);
                                }
                            }
                        }

                        piscifactoria.getTanques().add(tanque);
                    }

                    simulador.getPiscifactorias().add(piscifactoria);
                }
            }

            System.out.println("\nPartida cargada: " + archivoPartida);
            Simulador.logger.log("Sistema cargado.");
        } catch (Exception e) {
            System.err.println("Error al cargar el archivo: " + e.getMessage());
        }
    }
}