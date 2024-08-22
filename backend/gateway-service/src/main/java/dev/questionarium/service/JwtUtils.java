package dev.questionarium.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import dev.questionarium.types.AccessType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class JwtUtils {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private String expiration;

    public String generate(String email, AccessType accessType) {

        Date now = new Date();

        Long expirationInMiliss = accessType == AccessType.ACCESS ? Long.parseLong(expiration) * 1000
                : Long.parseLong(expiration) * 5 * 1000;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("questionarium")
                .subject(email)
                .expiresAt(new Date(now.getTime() + expirationInMiliss).toInstant())
                .issuedAt(now.toInstant())
                .claim("scope", "USER")
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

}
