package cl.duoc.vidasalud.appointments.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CatalogClient {
    private final RestClient restClient;

    public CatalogClient(RestClient.Builder builder, @Value("${services.catalog.base-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public void reserveBox(Long boxId) {
        try {
            restClient.post().uri("/internal/catalog/boxes/{id}/reserve", boxId).retrieve().toBodilessEntity();
        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No fue posible reservar el cupo del box.", ex);
        }
    }
}
