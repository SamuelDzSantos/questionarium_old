package com.github.questionarium.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class JwtEncoders {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    ReactiveJwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), "AES");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), "AES");
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(secretKey);
        return new NimbusJwtEncoder(immutableSecret);
    }
}
