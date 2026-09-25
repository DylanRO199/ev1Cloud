package cl.duoc.vidasalud.appointments.dto;

import cl.duoc.vidasalud.appointments.domain.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(@NotNull AppointmentStatus status, Long boxId) {}
