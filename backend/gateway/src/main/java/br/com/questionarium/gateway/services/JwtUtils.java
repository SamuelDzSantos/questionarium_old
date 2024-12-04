package dev.questionarium.services;

import java.util.Date;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtEncoder jwtEncoder;

    private String expiration = "86400";

    public String generate(String email, Long user_id) {

        Date now = new Date();

        Long expirationInMiliss = Long.parseLong(expiration) * 5 * 1000;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("viagem")
                .subject(email)
                .expiresAt(new Date(now.getTime() + expirationInMiliss).toInstant())
                .issuedAt(now.toInstant())
                .claim("role", "USER")
                .claim("user_id", user_id)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();

    }
}
