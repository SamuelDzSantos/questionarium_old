package br.com.questionarium.question_service.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            Object userIdClaim = jwt.getClaim("userId"); // 🚀 usando "userId" como você pediu
            if (userIdClaim != null) {
                return Long.parseLong(userIdClaim.toString());
            }
        }
        throw new IllegalStateException("Nenhum usuário autenticado ou reivindicação de userId ausente");
    }
}
