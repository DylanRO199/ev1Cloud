package cl.duoc.vidasalud.bff.dto;

import java.math.BigDecimal;

public final class CatalogDtos {
    private CatalogDtos() {}

    public record MedicalService(Long id, String name, String description, BigDecimal price, boolean active) {}
    public record Box(Long id, String name, Long serviceId, int availableSlots, boolean active) {}
}
