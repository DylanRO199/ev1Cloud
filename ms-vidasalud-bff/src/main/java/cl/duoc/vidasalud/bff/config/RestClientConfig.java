package cl.duoc.vidasalud.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean("appointmentsRestClient")
    RestClient appointmentsRestClient(RestClient.Builder builder,
                                      @Value("${services.appointments.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean("catalogRestClient")
    RestClient catalogRestClient(RestClient.Builder builder,
                                 @Value("${services.catalog.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
