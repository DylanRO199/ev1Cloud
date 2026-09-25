package cl.duoc.vidasalud.appointments.exception;

public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(Long id) {
        super("No se encontró la atención " + id + ".");
    }
}
