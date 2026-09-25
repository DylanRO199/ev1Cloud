package cl.duoc.vidasalud.catalog.dto;

import java.math.BigDecimal;

public record MedicalServiceResponse(Long id, String name, String description, BigDecimal price, boolean active) {}
