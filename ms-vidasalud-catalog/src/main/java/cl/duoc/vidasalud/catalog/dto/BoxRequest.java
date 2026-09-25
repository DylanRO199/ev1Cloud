package cl.duoc.vidasalud.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoxRequest(@NotBlank String name, @NotNull Long serviceId, @Min(0) int availableSlots, boolean active) {}
