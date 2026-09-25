package cl.duoc.vidasalud.catalog.service;

import cl.duoc.vidasalud.catalog.domain.Box;
import cl.duoc.vidasalud.catalog.exception.NoSlotsAvailableException;
import cl.duoc.vidasalud.catalog.repository.BoxRepository;
import cl.duoc.vidasalud.catalog.repository.MedicalServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {
    @Mock MedicalServiceRepository serviceRepository;
    @Mock BoxRepository boxRepository;
    @InjectMocks CatalogService service;

    @Test
    void rejectsReservationWithoutSlots() {
        Box box = new Box();
        box.setName("Box 01");
        box.setServiceId(1L);
        box.setAvailableSlots(0);
        box.setActive(true);
        when(boxRepository.findById(1L)).thenReturn(Optional.of(box));

        assertThatThrownBy(() -> service.reserveBox(1L))
                .isInstanceOf(NoSlotsAvailableException.class);
    }
}
