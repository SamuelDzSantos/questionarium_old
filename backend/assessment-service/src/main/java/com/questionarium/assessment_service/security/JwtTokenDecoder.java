package com.questionarium.assessment_service.security;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenDecoder {

    /**
     * Recupera o ID do usuário autenticado a partir do token JWT.
     * 
     * @return userId como Long
     * @throws IllegalStateException se não houver usuário autenticado ou claim
     *                               ausente/inválida
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se existe Authentication e se o principal é um Jwt
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            Object raw = jwt.getClaim("userId");
            log.debug(">>> raw userId claim = {} (tipo {})", raw, raw != null ? raw.getClass().getSimpleName() : null);

            if (raw == null) {
                throw new IllegalStateException("Claim 'userId' ausente no token JWT");
            }
            if (raw instanceof Number) {
                return ((Number) raw).longValue();
            }
            if (raw instanceof String) {
                try {
                    return Long.parseLong((String) raw);
                } catch (NumberFormatException ex) {
                    throw new IllegalStateException(
                            "Claim 'userId' não é um número válido: " + raw, ex);
                }
            }
            throw new IllegalStateException(
                    "Claim 'userId' está em formato inesperado: " + raw.getClass().getSimpleName());
        }

        throw new IllegalStateException(
                "Nenhum usuário autenticado ou claim 'userId' ausente");
    }

    /**
     * Verifica se o usuário autenticado possui a role ADMIN.
     * 
     * @return true se tiver ROLE_ADMIN
     * @throws IllegalStateException se não houver usuário autenticado ou claim
     *                               ausente/inválida
     */
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se existe Authentication e se o principal é um Jwt
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            // Supondo que o Auth Service emita uma lista de roles em "role"
            Object rawRole = jwt.getClaim("role");

            // Se vier como lista de strings
            if (rawRole instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) rawRole;
                return roles.contains("ADMIN") || roles.contains("ROLE_ADMIN");
            }
            // Se vier como única string
            if (rawRole instanceof String) {
                String role = (String) rawRole;
                return "ADMIN".equals(role) || "ROLE_ADMIN".equals(role);
            }
            throw new IllegalStateException(
                    "Claim 'role' em formato inesperado: " + rawRole.getClass().getSimpleName());
        }

        throw new IllegalStateException(
                "Nenhum usuário autenticado ou claim 'role' ausente");
    }

}
