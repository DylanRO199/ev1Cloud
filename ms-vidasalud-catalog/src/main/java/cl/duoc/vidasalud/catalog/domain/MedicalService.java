package cl.duoc.vidasalud.catalog.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "medical_services")
public class MedicalService {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
    @Column(nullable = false)
    private boolean active = true;

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
