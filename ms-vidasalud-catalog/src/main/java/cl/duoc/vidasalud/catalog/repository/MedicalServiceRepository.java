package cl.duoc.vidasalud.catalog.repository;

import cl.duoc.vidasalud.catalog.domain.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {
    List<MedicalService> findAllByOrderByNameAsc();
}
