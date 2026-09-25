package cl.duoc.vidasalud.appointments.controller;

import cl.duoc.vidasalud.appointments.domain.AppointmentStatus;
import cl.duoc.vidasalud.appointments.dto.AppointmentCreateRequest;
import cl.duoc.vidasalud.appointments.dto.AppointmentResponse;
import cl.duoc.vidasalud.appointments.dto.StatusUpdateRequest;
import cl.duoc.vidasalud.appointments.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<AppointmentResponse> list(@RequestParam(required = false) AppointmentStatus status,
                                          @RequestParam(required = false) String patientId) {
        return service.list(status, patientId);
    }

    @GetMapping("/{id}")
    public AppointmentResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(@Valid @RequestBody AppointmentCreateRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}/status")
    public AppointmentResponse updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return service.updateStatus(id, request);
    }
}
