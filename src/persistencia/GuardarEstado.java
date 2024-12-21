package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import commons.Simulador;
import piscifactoria.Piscifactoria;
import piscifactoria.PiscifactoriaDeRio;
import tanque.Tanque;
import peces.Pez;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GuardarEstado {
    public static void guardar(Simulador simulador) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, Object> estado = new HashMap<>();

        estado.put("implementados", simulador.getPecesImplementados());
        estado.put("empresa", simulador.getNombreEntidad());
        estado.put("dia", simulador.getDia());
        estado.put("monedas", Simulador.monedas.getMonedas());
        estado.put("orca", "0:0,0,0;0:0,0,0"); // Asigna el valor correspondiente

        // Edificios - Almacén
        Map<String, Object> almacenMap = new HashMap<>();
        almacenMap.put("disponible", Simulador.almacenCentral != null && Simulador.almacenCentral.getCapacidadAlmacen() > 0);
        almacenMap.put("capacidad", Simulador.almacenCentral != null ? Simulador.almacenCentral.getCapacidadAlmacen() : 200);
        almacenMap.put("comida", Map.of(
                "vegetal", Simulador.almacenCentral != null ? Simulador.almacenCentral.getCantidadComidaVegetal() : 0,
                "animal", Simulador.almacenCentral != null ? Simulador.almacenCentral.getCantidadComidaAnimal() : 0
        ));
        estado.put("edificios", Map.of("almacen", almacenMap));

        // Piscifactorías
        List<Map<String, Object>> piscifactoriasList = new ArrayList<>();
        for (Piscifactoria piscifactoria : simulador.getPiscifactorias()) {
            Map<String, Object> piscifactoriaMap = new HashMap<>();
            piscifactoriaMap.put("nombre", piscifactoria.getNombre());
            piscifactoriaMap.put("tipo", piscifactoria instanceof PiscifactoriaDeRio ? 0 : 1);
            piscifactoriaMap.put("capacidad", piscifactoria.getCapacidadMaximaComida());
            piscifactoriaMap.put("comida", Map.of(
                    "vegetal", piscifactoria.getComidaVegetalActual(),
                    "animal", piscifactoria.getComidaAnimalActual()
            ));

            // Tanques
            List<Map<String, Object>> tanquesList = new ArrayList<>();
            for (Tanque tanque : piscifactoria.getTanques()) {
                Map<String, Object> tanqueMap = new HashMap<>();
                if (!tanque.getPeces().isEmpty()) {
                    tanqueMap.put("pez", tanque.getPeces().get(0).getNombre());
                    tanqueMap.put("num", tanque.getNumeroTanque());
                    tanqueMap.put("datos", Map.of(
                            "vivos", tanque.getPeces().size(),
                            "maduros", tanque.getMaduros(),
                            "fertiles", tanque.getFertiles()
                    ));

                    // Peces
                    List<Map<String, Object>> pecesList = new ArrayList<>();
                    for (Pez pez : tanque.getPeces()) {
                        Map<String, Object> pezMap = new HashMap<>();
                        pezMap.put("edad", pez.getEdad());
                        pezMap.put("sexo", pez.isSexo());
                        pezMap.put("vivo", pez.isVivo());
                        pezMap.put("maduro", pez.isMaduro());
                        pezMap.put("fertil", pez.isFertil());
                        pezMap.put("ciclo", pez.getCiclo());
                        pezMap.put("alimentado", pez.isAlimentado());
                        pecesList.add(pezMap);
                    }
                    tanqueMap.put("peces", pecesList);
                }
                tanquesList.add(tanqueMap);
            }
            piscifactoriaMap.put("tanques", tanquesList);
            piscifactoriasList.add(piscifactoriaMap);
        }
        estado.put("piscifactorias", piscifactoriasList);

        try (FileWriter writer = new FileWriter("saves/holaaaaaaaaaa.save")) {
            gson.toJson(estado, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}