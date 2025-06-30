package com.github.questionarium.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.github.questionarium.types.UserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtEncoder jwtEncoder;

    public String generateToken(Long id, String email, UserRole role) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("questionarium")
                .issuedAt(now)
                .expiresAt(now.plus(24, ChronoUnit.HOURS))
                .subject(email)
                .claim("id", id)
                .claim("scope", role == UserRole.USER ? "USER" : "ADMIN")
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader,
                claims)).getTokenValue();
    }

}
