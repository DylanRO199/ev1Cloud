package cl.duoc.vidasalud.appointments.repository;

import cl.duoc.vidasalud.appointments.domain.Appointment;
import cl.duoc.vidasalud.appointments.domain.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByOrderByScheduledAtDesc();
    List<Appointment> findByStatusOrderByScheduledAtDesc(AppointmentStatus status);
    List<Appointment> findByPatientIdOrderByScheduledAtDesc(String patientId);
    List<Appointment> findByPatientIdAndStatusOrderByScheduledAtDesc(String patientId, AppointmentStatus status);
}
