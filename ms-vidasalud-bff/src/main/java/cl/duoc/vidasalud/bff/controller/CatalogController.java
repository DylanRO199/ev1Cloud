package cl.duoc.vidasalud.bff.controller;

import cl.duoc.vidasalud.bff.client.CatalogClient;
import cl.duoc.vidasalud.bff.dto.CatalogDtos;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
    private final CatalogClient client;

    public CatalogController(CatalogClient client) {
        this.client = client;
    }

    @GetMapping("/services")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','PACIENTE')")
    public List<CatalogDtos.MedicalService> listServices() {
        return client.listServices();
    }

    @GetMapping("/boxes")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public List<CatalogDtos.Box> listBoxes() {
        return client.listBoxes();
    }
}
