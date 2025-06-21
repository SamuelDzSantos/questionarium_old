package br.com.questionarium.user_service.security.jwt;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenDecoder {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLES = "roles";

    //Recupera o ID do usuário autenticado a partir do token JWT.
    public Long getCurrentUserId() {
        Jwt jwt = extractJwt();
        Object raw = jwt.getClaim(CLAIM_USER_ID);
        log.debug("raw '{}' claim = {} ({})", CLAIM_USER_ID, raw, raw != null ? raw.getClass().getSimpleName() : null);

        if (raw instanceof Number) {
            return ((Number) raw).longValue();
        }
        if (raw instanceof String) {
            try {
                return Long.parseLong((String) raw);
            } catch (NumberFormatException ex) {
                throw new IllegalStateException("Claim '" + CLAIM_USER_ID + "' não é um número válido: " + raw, ex);
            }
        }
        throw new IllegalStateException("Claim '" + CLAIM_USER_ID + "' ausente ou em formato inesperado");
    }

    //Recupera a lista de roles do usuário (ex: ["USER","ADMIN"]).
    public List<String> getRoles() {
        Jwt jwt = extractJwt();
        Object raw = jwt.getClaim(CLAIM_ROLES);
        log.debug("raw '{}' claim = {} ({})", CLAIM_ROLES, raw, raw != null ? raw.getClass().getSimpleName() : null);

        if (raw instanceof List<?>) {
            return ((List<?>) raw).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        if (raw instanceof String) {
            return List.of(raw.toString());
        }
        throw new IllegalStateException("Claim '" + CLAIM_ROLES + "' ausente ou em formato inesperado");
    }

    // Verifica se o usuário autenticado possui ROLE_ADMIN.
    public boolean isAdmin() {
        List<String> roles = getRoles();
        return roles.stream()
                .anyMatch(r -> "ADMIN".equals(r) || "ROLE_ADMIN".equals(r));
    }
     
    private Jwt extractJwt() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        throw new IllegalStateException("Nenhum usuário autenticado ou token JWT inválido");
    }
}
