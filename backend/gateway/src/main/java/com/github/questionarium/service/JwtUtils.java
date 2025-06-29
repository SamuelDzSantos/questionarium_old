package com.github.questionarium.service;

import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Service;

import com.github.questionarium.types.JwtDecodeResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    private final ReactiveJwtDecoder jwtDecoder;

    public Mono<Long> getUserId(String token) {
        return jwtDecoder.decode(token)
                .map(jwt -> {
                    return (Long) jwt.getClaims().get("id");
                });
    }

    public Mono<JwtDecodeResult> getDecodeResult(String token) {

        return jwtDecoder.decode(token)
                .map(jwt -> {
                    Long userId = (Long) jwt.getClaims().get("id");
                    log.info("Role encontrada no JWT: {}", jwt.getClaim("scope").toString());
                    Boolean isAdmin = jwt.getClaim("scope").toString().equals("ADMIN");
                    JwtDecodeResult result = new JwtDecodeResult(userId, isAdmin);
                    return result;
                });
    }

}
