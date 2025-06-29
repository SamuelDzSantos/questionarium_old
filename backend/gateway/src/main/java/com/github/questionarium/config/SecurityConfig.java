package com.github.questionarium.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoggingFilter loggingFilter;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/login", "/auth/register", "/auth/token/**", "/pdf/**").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().authenticated())
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(loggingFilter, SecurityWebFiltersOrder.AUTHORIZATION)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }

    @Bean
    WebClient webClient() {
        return WebClient.create();
    }

}
