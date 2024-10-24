package peces.propiedades;

public interface Activo extends Carnivoro {
    boolean comerCarneActivo(int comidaAnimalDisponible); // Devuelve true si consume comida
}
