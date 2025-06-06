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

    /**
     * @param userId  agora é String, pois o ID do AuthUser é gerado pelo Mongo (ObjectId)
     * @param email   ou login do usuário
     * @param role    papel do usuário (USER, ADMIN)
     */
    public String generateToken(String userId, String email, Role role) {
        Date now = new Date();

        // 'expiration' vem em segundos (por exemplo, "86400" = 1 dia)
        long expSeconds = Long.parseLong(expiration);
        // converte para milissegundos
        long expMillis = expSeconds * 1000L;
        Date exp = new Date(now.getTime() + expMillis);

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("questionarium-auth")
            .subject(userId)
            .issuedAt(now.toInstant())
            .expiresAt(exp.toInstant())
            .claim("email", email)
            .claim("role", role.getAsString())
            .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        return jwtEncoder
            .encode(JwtEncoderParameters.from(jwsHeader, claims))
            .getTokenValue();
    }

    @Value("${jwt.expiration}")
    private String expiration;
}
