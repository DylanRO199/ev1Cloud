package cl.duoc.vidasalud.bff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.OffsetDateTime;

public final class AppointmentDtos {
    private AppointmentDtos() {}

    public record CreateRequest(
            String patientId,
            @NotBlank String patientName,
            @NotBlank @Email String patientEmail,
            @NotNull Long serviceId,
            @NotNull @FutureOrPresent OffsetDateTime scheduledAt
    ) {}

    public record StatusRequest(@NotNull String status, Long boxId) {}

    public record Response(
            Long id,
            String patientId,
            String patientName,
            String patientEmail,
            Long serviceId,
            Long boxId,
            OffsetDateTime scheduledAt,
            String status,
            Instant createdAt,
            Instant updatedAt
    ) {}
}
