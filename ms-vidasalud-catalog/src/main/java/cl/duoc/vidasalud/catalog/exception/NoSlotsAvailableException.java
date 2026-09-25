package cl.duoc.vidasalud.catalog.exception;

public class NoSlotsAvailableException extends RuntimeException {
    public NoSlotsAvailableException(Long id) { super("El box " + id + " no tiene cupos disponibles."); }
}
