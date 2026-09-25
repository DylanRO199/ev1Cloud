package cl.duoc.vidasalud.appointments.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.OffsetDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String patientId;

    @Column(nullable = false, length = 120)
    private String patientName;

    @Column(nullable = false, length = 180)
    private String patientEmail;

    @Column(nullable = false)
    private Long serviceId;

    private Long boxId;

    @Column(nullable = false)
    private OffsetDateTime scheduledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AppointmentStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
        if (status == null) status = AppointmentStatus.SOLICITADA;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getPatientEmail() { return patientEmail; }
    public void setPatientEmail(String patientEmail) { this.patientEmail = patientEmail; }
    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }
    public Long getBoxId() { return boxId; }
    public void setBoxId(Long boxId) { this.boxId = boxId; }
    public OffsetDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(OffsetDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
