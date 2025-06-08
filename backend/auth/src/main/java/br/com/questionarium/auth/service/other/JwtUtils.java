package br.com.questionarium.auth.service.other;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import br.com.questionarium.types.Role;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private String expiration;

    /**
     * @param mongoUserId    ID do AuthUser (String, vindo do MongoDB)
     * @param userIdPostgres ID do User do serviço User (Long, do PostgreSQL)
     * @param email          e-mail ou login
     * @param role           papel do usuário (USER, ADMIN)
     */
    public String generateToken(String mongoUserId, Long userIdPostgres, String email, Role role) {
        Date now = new Date();

        // 'expiration' vem em segundos
        long expSeconds = Long.parseLong(expiration);
        long expMillis = expSeconds * 1000L;
        Date exp = new Date(now.getTime() + expMillis);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(mongoUserId)
                .claim("role", role.name())
                .claim("userId", userIdPostgres) // ✅ nome correto do claim!
                .issuer("questionarium-auth")
                .issuedAt(now.toInstant())
                .expiresAt(exp.toInstant())
                .claim("email", email)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }
}
