package cl.duoc.vidasalud.catalog.controller;

import cl.duoc.vidasalud.catalog.dto.*;
import cl.duoc.vidasalud.catalog.service.CatalogService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {
    private final CatalogService service;

    public CatalogController(CatalogService service) { this.service = service; }

    @GetMapping("/services")
    public List<MedicalServiceResponse> listServices() { return service.listServices(); }

    @PostMapping("/services")
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalServiceResponse createService(@Valid @RequestBody MedicalServiceRequest request) { return service.createService(request); }

    @PutMapping("/services/{id}")
    public MedicalServiceResponse updateService(@PathVariable Long id, @Valid @RequestBody MedicalServiceRequest request) {
        return service.updateService(id, request);
    }

    @GetMapping("/boxes")
    public List<BoxResponse> listBoxes() { return service.listBoxes(); }

    @PostMapping("/boxes")
    @ResponseStatus(HttpStatus.CREATED)
    public BoxResponse createBox(@Valid @RequestBody BoxRequest request) { return service.createBox(request); }
}
