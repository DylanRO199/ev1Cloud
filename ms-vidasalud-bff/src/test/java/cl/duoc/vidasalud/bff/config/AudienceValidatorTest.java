package cl.duoc.vidasalud.bff.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AudienceValidatorTest {
    @Test
    void acceptsExpectedAudience() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user")
                .audience(List.of("api://vidasalud"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();

        assertThat(new AudienceValidator("api://vidasalud").validate(jwt).hasErrors()).isFalse();
    }

    @Test
    void rejectsDifferentAudience() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("user")
                .audience(List.of("api://other"))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(300))
                .build();

        assertThat(new AudienceValidator("api://vidasalud").validate(jwt).hasErrors()).isTrue();
    }
}
