package cl.duoc.vidasalud.catalog.repository;

import cl.duoc.vidasalud.catalog.domain.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long> {
    List<Box> findAllByOrderByNameAsc();
}
