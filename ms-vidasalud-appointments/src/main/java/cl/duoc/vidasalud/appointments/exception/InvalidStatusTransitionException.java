package cl.duoc.vidasalud.appointments.exception;

import cl.duoc.vidasalud.appointments.domain.AppointmentStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(AppointmentStatus current, AppointmentStatus target) {
        super("No se permite cambiar el estado de " + current + " a " + target + ".");
    }
}
