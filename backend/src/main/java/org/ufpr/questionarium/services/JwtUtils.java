package org.ufpr.questionarium.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtUtils {

    private static final int expireInMs = 60 * 1000;

    private final Algorithm algorithm;

    JwtUtils(@Value("${api.security.token.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(String subject) {
        try {
            String token = JWT.create()
                    .withIssuer("questionarium")
                    .withSubject(subject)
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(System.currentTimeMillis() + expireInMs))
                    .sign(algorithm);
            return token;
        } catch (Exception _) {
            throw new RuntimeException("Erro ao criar token JWT!");
        }
    }

    public Boolean validateToken(String token) {
        if (getUsername(token) != null && isExpired(token))
            return true;
        return false;
    }

    public String getUsername(String token) {
        return JWT.require(algorithm).withIssuer("questionarium").build().verify(token).getSubject();
    }

    public Boolean isExpired(String token) {
        return JWT.require(algorithm).withIssuer("questionarium")
                .build().verify(token)
                .getExpiresAt()
                .after(new Date(System.currentTimeMillis()));
    }

}
