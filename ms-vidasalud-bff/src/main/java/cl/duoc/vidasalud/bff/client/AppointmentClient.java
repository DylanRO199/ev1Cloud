package cl.duoc.vidasalud.bff.client;

import cl.duoc.vidasalud.bff.dto.AppointmentDtos;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class AppointmentClient extends DownstreamClientSupport {
    private final RestClient restClient;

    public AppointmentClient(@Qualifier("appointmentsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<AppointmentDtos.Response> list(String status, String patientId) {
        try {
            return restClient.get()
                    .uri(uriBuilder -> {
                        var builder = uriBuilder.path("/api/appointments");
                        if (status != null && !status.isBlank()) builder.queryParam("status", status);
                        if (patientId != null && !patientId.isBlank()) builder.queryParam("patientId", patientId);
                        return builder.build();
                    })
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (RestClientResponseException ex) {
            throw translate(ex);
        }
    }

    public AppointmentDtos.Response findById(Long id) {
        try {
            return restClient.get().uri("/api/appointments/{id}", id).retrieve().body(AppointmentDtos.Response.class);
        } catch (RestClientResponseException ex) {
            throw translate(ex);
        }
    }

    public AppointmentDtos.Response create(AppointmentDtos.CreateRequest request) {
        try {
            return restClient.post().uri("/api/appointments").body(request).retrieve().body(AppointmentDtos.Response.class);
        } catch (RestClientResponseException ex) {
            throw translate(ex);
        }
    }

    public AppointmentDtos.Response updateStatus(Long id, AppointmentDtos.StatusRequest request) {
        try {
            return restClient.put().uri("/api/appointments/{id}/status", id).body(request).retrieve().body(AppointmentDtos.Response.class);
        } catch (RestClientResponseException ex) {
            throw translate(ex);
        }
    }
}
