package cl.duoc.vidasalud.appointments.service;

import cl.duoc.vidasalud.appointments.client.CatalogClient;
import cl.duoc.vidasalud.appointments.domain.Appointment;
import cl.duoc.vidasalud.appointments.domain.AppointmentStatus;
import cl.duoc.vidasalud.appointments.dto.StatusUpdateRequest;
import cl.duoc.vidasalud.appointments.exception.InvalidStatusTransitionException;
import cl.duoc.vidasalud.appointments.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {
    @Mock AppointmentRepository repository;
    @Mock CatalogClient catalogClient;
    @InjectMocks AppointmentService service;

    @Test
    void rejectsInvalidTransition() {
        Appointment item = appointment(AppointmentStatus.SOLICITADA);
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> service.updateStatus(1L, new StatusUpdateRequest(AppointmentStatus.EN_ATENCION, null)))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void reservesBoxWhenConfirming() {
        Appointment item = appointment(AppointmentStatus.SOLICITADA);
        when(repository.findById(1L)).thenReturn(Optional.of(item));
        when(repository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.updateStatus(1L, new StatusUpdateRequest(AppointmentStatus.CONFIRMADA, 5L));

        verify(catalogClient).reserveBox(5L);
    }

    private Appointment appointment(AppointmentStatus status) {
        Appointment item = new Appointment();
        item.setPatientId("patient-1");
        item.setPatientName("Paciente Demo");
        item.setPatientEmail("paciente@vidasalud.cl");
        item.setServiceId(1L);
        item.setScheduledAt(OffsetDateTime.now().plusDays(1));
        item.setStatus(status);
        return item;
    }
}
