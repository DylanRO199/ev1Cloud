package cl.duoc.vidasalud.catalog.controller;

import cl.duoc.vidasalud.catalog.dto.BoxResponse;
import cl.duoc.vidasalud.catalog.service.CatalogService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/catalog")
public class InternalCatalogController {
    private final CatalogService service;

    public InternalCatalogController(CatalogService service) { this.service = service; }

    @PostMapping("/boxes/{id}/reserve")
    public BoxResponse reserve(@PathVariable Long id) {
        return service.reserveBox(id);
    }
}
