package cl.duoc.vidasalud.bff.client;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

abstract class DownstreamClientSupport {
    RuntimeException translate(RestClientResponseException ex) {
        String body = ex.getResponseBodyAsString();
        String message = body == null || body.isBlank() ? "Error del servicio de dominio." : body;
        return new ResponseStatusException(ex.getStatusCode(), message, ex);
    }
}
