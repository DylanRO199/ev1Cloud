package cl.duoc.vidasalud.appointments.dto;

import cl.duoc.vidasalud.appointments.domain.AppointmentStatus;

import java.time.Instant;
import java.time.OffsetDateTime;

public record AppointmentResponse(
        Long id,
        String patientId,
        String patientName,
        String patientEmail,
        Long serviceId,
        Long boxId,
        OffsetDateTime scheduledAt,
        AppointmentStatus status,
        Instant createdAt,
        Instant updatedAt
) {}
