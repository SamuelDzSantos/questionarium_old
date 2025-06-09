package com.questionarium.assessment_service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    /** Retorna o userId que veio no JWT */
    public Long extractUserId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaim("userId");
    }

    /** Retorna se o usuário atual tem role ADMIN */
    public boolean isAdmin(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaimAsStringList("authorities")
                .contains("ROLE_ADMIN");
    }
}
