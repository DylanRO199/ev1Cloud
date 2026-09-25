package cl.duoc.vidasalud.catalog.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "clinical_boxes")
public class Box {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 80)
    private String name;
    @Column(nullable = false)
    private Long serviceId;
    @Column(nullable = false)
    private int availableSlots;
    @Column(nullable = false)
    private boolean active = true;
    @Version
    private Long version;

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public int getAvailableSlots() { return availableSlots; }
    public void setAvailableSlots(int availableSlots) { this.availableSlots = availableSlots; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
