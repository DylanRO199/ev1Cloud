package cl.duoc.vidasalud.bff.client;

import cl.duoc.vidasalud.bff.dto.CatalogDtos;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class CatalogClient extends DownstreamClientSupport {
    private final RestClient restClient;

    public CatalogClient(@Qualifier("catalogRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<CatalogDtos.MedicalService> listServices() {
        try {
            return restClient.get().uri("/api/catalog/services").retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientResponseException ex) {
            throw translate(ex);
        }
    }

    public List<CatalogDtos.Box> listBoxes() {
        try {
            return restClient.get().uri("/api/catalog/boxes").retrieve().body(new ParameterizedTypeReference<>() {});
        } catch (RestClientResponseException ex) {
            throw translate(ex);
        }
    }
}
