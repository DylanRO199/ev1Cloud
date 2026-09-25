package cl.duoc.vidasalud.bff.controller;

import cl.duoc.vidasalud.bff.client.AppointmentClient;
import cl.duoc.vidasalud.bff.dto.AppointmentDtos;
import cl.duoc.vidasalud.bff.security.UserContext;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentClient client;

    public AppointmentController(AppointmentClient client) {
        this.client = client;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','PACIENTE')")
    public List<AppointmentDtos.Response> list(@RequestParam(required = false) String status,
                                               Authentication authentication) {
        String patientId = UserContext.hasRole(authentication, "PACIENTE")
                && !UserContext.hasRole(authentication, "ADMIN")
                && !UserContext.hasRole(authentication, "RECEPCIONISTA")
                ? UserContext.subject(authentication)
                : null;
        return client.list(status, patientId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','PACIENTE')")
    public AppointmentDtos.Response findById(@PathVariable Long id, Authentication authentication) {
        AppointmentDtos.Response item = client.findById(id);
        if (UserContext.hasRole(authentication, "PACIENTE")
                && !UserContext.subject(authentication).equals(item.patientId())) {
            throw new org.springframework.security.access.AccessDeniedException("Atención no disponible para el usuario.");
        }
        return item;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA','PACIENTE')")
    public AppointmentDtos.Response create(@Valid @RequestBody AppointmentDtos.CreateRequest request,
                                            Authentication authentication) {
        if (UserContext.hasRole(authentication, "PACIENTE")
                && !UserContext.hasRole(authentication, "ADMIN")
                && !UserContext.hasRole(authentication, "RECEPCIONISTA")) {
            request = new AppointmentDtos.CreateRequest(
                    UserContext.subject(authentication),
                    UserContext.name(authentication),
                    UserContext.email(authentication),
                    request.serviceId(),
                    request.scheduledAt());
        } else if (request.patientId() == null || request.patientId().isBlank()) {
            request = new AppointmentDtos.CreateRequest(
                    request.patientEmail(), request.patientName(), request.patientEmail(),
                    request.serviceId(), request.scheduledAt());
        }
        return client.create(request);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public AppointmentDtos.Response updateStatus(@PathVariable Long id,
                                                  @Valid @RequestBody AppointmentDtos.StatusRequest request) {
        return client.updateStatus(id, request);
    }
}
