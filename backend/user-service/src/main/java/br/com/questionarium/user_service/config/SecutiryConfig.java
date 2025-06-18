package br.com.questionarium.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecutiryConfig {
    
    @Value("${spring.security.oauth2.resourceserver.jwt.secret}")
    private String jwtSecret;
}
