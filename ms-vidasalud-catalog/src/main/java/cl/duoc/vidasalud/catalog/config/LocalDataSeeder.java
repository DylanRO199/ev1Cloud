package cl.duoc.vidasalud.catalog.config;

import cl.duoc.vidasalud.catalog.domain.Box;
import cl.duoc.vidasalud.catalog.domain.MedicalService;
import cl.duoc.vidasalud.catalog.repository.BoxRepository;
import cl.duoc.vidasalud.catalog.repository.MedicalServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;

@Configuration
@Profile("local")
public class LocalDataSeeder {
    @Bean
    CommandLineRunner seed(MedicalServiceRepository services, BoxRepository boxes) {
        return args -> {
            if (services.count() > 0) return;

            MedicalService medicina = new MedicalService();
            medicina.setName("Medicina general");
            medicina.setDescription("Atención médica general");
            medicina.setPrice(new BigDecimal("25000"));
            medicina.setActive(true);
            medicina = services.save(medicina);

            MedicalService dental = new MedicalService();
            dental.setName("Evaluación dental");
            dental.setDescription("Evaluación odontológica inicial");
            dental.setPrice(new BigDecimal("18000"));
            dental.setActive(true);
            dental = services.save(dental);

            Box box1 = new Box();
            box1.setName("Box 01");
            box1.setServiceId(medicina.getId());
            box1.setAvailableSlots(8);
            box1.setActive(true);
            boxes.save(box1);

            Box box2 = new Box();
            box2.setName("Box Dental 01");
            box2.setServiceId(dental.getId());
            box2.setAvailableSlots(6);
            box2.setActive(true);
            boxes.save(box2);
        };
    }
}
