package cl.duoc.vidasalud.appointments.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record AppointmentCreateRequest(
        @NotBlank String patientId,
        @NotBlank String patientName,
        @NotBlank @Email String patientEmail,
        @NotNull Long serviceId,
        @NotNull @FutureOrPresent OffsetDateTime scheduledAt
) {}
