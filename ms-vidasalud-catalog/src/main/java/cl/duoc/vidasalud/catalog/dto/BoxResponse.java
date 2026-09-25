package cl.duoc.vidasalud.catalog.dto;

public record BoxResponse(Long id, String name, Long serviceId, int availableSlots, boolean active) {}
