package cl.duoc.vidasalud.bff.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public final class UserContext {
    private UserContext() {}

    public static String subject(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        String oid = token.getToken().getClaimAsString("oid");
        return oid != null && !oid.isBlank() ? oid : token.getToken().getSubject();
    }

    public static String name(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        String name = token.getToken().getClaimAsString("name");
        return name != null ? name : authentication.getName();
    }

    public static String email(Authentication authentication) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
        String email = token.getToken().getClaimAsString("preferred_username");
        return email != null ? email : authentication.getName();
    }

    public static boolean hasRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}
