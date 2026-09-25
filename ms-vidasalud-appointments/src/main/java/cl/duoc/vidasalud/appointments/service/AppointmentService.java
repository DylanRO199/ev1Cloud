package cl.duoc.vidasalud.appointments.service;

import cl.duoc.vidasalud.appointments.client.CatalogClient;
import cl.duoc.vidasalud.appointments.domain.Appointment;
import cl.duoc.vidasalud.appointments.domain.AppointmentStatus;
import cl.duoc.vidasalud.appointments.dto.AppointmentCreateRequest;
import cl.duoc.vidasalud.appointments.dto.AppointmentResponse;
import cl.duoc.vidasalud.appointments.dto.StatusUpdateRequest;
import cl.duoc.vidasalud.appointments.exception.AppointmentNotFoundException;
import cl.duoc.vidasalud.appointments.exception.InvalidStatusTransitionException;
import cl.duoc.vidasalud.appointments.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class AppointmentService {
    private static final Map<AppointmentStatus, Set<AppointmentStatus>> TRANSITIONS = Map.of(
            AppointmentStatus.SOLICITADA, EnumSet.of(AppointmentStatus.CONFIRMADA, AppointmentStatus.CANCELADA),
            AppointmentStatus.CONFIRMADA, EnumSet.of(AppointmentStatus.EN_ESPERA, AppointmentStatus.CANCELADA),
            AppointmentStatus.EN_ESPERA, EnumSet.of(AppointmentStatus.EN_ATENCION, AppointmentStatus.CANCELADA),
            AppointmentStatus.EN_ATENCION, EnumSet.of(AppointmentStatus.CERRADA),
            AppointmentStatus.CERRADA, EnumSet.noneOf(AppointmentStatus.class),
            AppointmentStatus.CANCELADA, EnumSet.noneOf(AppointmentStatus.class)
    );

    private final AppointmentRepository repository;
    private final CatalogClient catalogClient;

    public AppointmentService(AppointmentRepository repository, CatalogClient catalogClient) {
        this.repository = repository;
        this.catalogClient = catalogClient;
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> list(AppointmentStatus status, String patientId) {
        List<Appointment> items;
        if (patientId != null && status != null) {
            items = repository.findByPatientIdAndStatusOrderByScheduledAtDesc(patientId, status);
        } else if (patientId != null) {
            items = repository.findByPatientIdOrderByScheduledAtDesc(patientId);
        } else if (status != null) {
            items = repository.findByStatusOrderByScheduledAtDesc(status);
        } else {
            items = repository.findAllByOrderByScheduledAtDesc();
        }
        return items.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AppointmentResponse findById(Long id) {
        return toResponse(findEntity(id));
    }

    public AppointmentResponse create(AppointmentCreateRequest request) {
        Appointment item = new Appointment();
        item.setPatientId(request.patientId());
        item.setPatientName(request.patientName());
        item.setPatientEmail(request.patientEmail());
        item.setServiceId(request.serviceId());
        item.setScheduledAt(request.scheduledAt());
        item.setStatus(AppointmentStatus.SOLICITADA);
        return toResponse(repository.save(item));
    }

    public AppointmentResponse updateStatus(Long id, StatusUpdateRequest request) {
        Appointment item = findEntity(id);
        AppointmentStatus current = item.getStatus();
        AppointmentStatus target = request.status();

        if (!TRANSITIONS.getOrDefault(current, Set.of()).contains(target)) {
            throw new InvalidStatusTransitionException(current, target);
        }

        if (target == AppointmentStatus.CONFIRMADA) {
            Long boxId = request.boxId() != null ? request.boxId() : item.getBoxId();
            if (boxId == null) {
                throw new IllegalArgumentException("Se debe seleccionar un box para confirmar la atención.");
            }
            catalogClient.reserveBox(boxId);
            item.setBoxId(boxId);
        }

        item.setStatus(target);
        return toResponse(repository.save(item));
    }

    private Appointment findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    private AppointmentResponse toResponse(Appointment item) {
        return new AppointmentResponse(
                item.getId(), item.getPatientId(), item.getPatientName(), item.getPatientEmail(),
                item.getServiceId(), item.getBoxId(), item.getScheduledAt(), item.getStatus(),
                item.getCreatedAt(), item.getUpdatedAt());
    }
}
