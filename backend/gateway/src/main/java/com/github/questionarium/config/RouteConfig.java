package com.github.questionarium.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

        @Bean
        RouteLocator defaultRouteLocator(RouteLocatorBuilder builder) {

                return builder.routes()

                                // Rotas para microserviço SAGA
                                .route(r -> r.path("/auth/register")
                                                .filters(f -> f.setPath("/saga/register"))
                                                .uri("http://localhost:15000"))
                                .route(r -> r.path("/auth/**").uri("http://localhost:14001"))

                                // Rotas gerenciadas diretamente pelo Gateway
                                .route("gateway-service", r -> r
                                                .path("/user/data")
                                                .filters(f -> f.circuitBreaker(cb -> cb
                                                                .setName("userCircuitBreaker")
                                                                .setFallbackUri("forward:/fallback/users")))
                                                .uri("forward:/user/data"))

                                .route("gateway-serivce", r -> r.path("/assessment/model/**").filters(f ->

                                f.circuitBreaker(cb -> cb.setName(("assessmentCircuitBreaker"))
                                                .setFallbackUri("forward:/fallback/assessment")))
                                                .uri("forward:/assessment/model")

                                )

                                // Auth Service
                                .route("auth-service", r -> r
                                                .path("/auth/**")
                                                .filters(f -> f.circuitBreaker(cb -> cb
                                                                .setName("authCircuitBreaker")
                                                                .setFallbackUri("forward:/fallback/auth")))
                                                .uri("http://localhost:14001"))

                                // User Service
                                .route("user-service", r -> r
                                                .path("/users", "/users/**")
                                                .filters(f -> f.circuitBreaker(cb -> cb
                                                                .setName("userCircuitBreaker")
                                                                .setFallbackUri("forward:/fallback/users")))
                                                .uri("http://localhost:14002"))

                                // Question Service
                                .route("question-service", r -> r
                                                .path("/questions/**")
                                                .filters(f -> f.circuitBreaker(cb -> cb
                                                                .setName("questionCircuitBreaker")
                                                                .setFallbackUri("forward:/fallback/questions")))
                                                .uri("http://localhost:14004"))

                                // Assessment Service
                                .route("assessment-service", r -> r
                                                .path(
                                                                "/assessment-headers/**",
                                                                "/assessment-models/**",
                                                                "/applied-assessments/**",
                                                                "/record-assessments/**",
                                                                "/pdf/**",
                                                                "/report/**")
                                                .filters(f -> f.circuitBreaker(cb -> cb
                                                                .setName("assessmentCircuitBreaker")
                                                                .setFallbackUri("forward:/fallback/assessment")))
                                                .uri("http://localhost:14005"))

                                // AI Service
                                .route("ai-service", r -> r
                                                .path("/ai/**")
                                                .uri("http://localhost:14011"))

                                // Detecção Service
                                .route("deteccao-service", r -> r
                                                .path("/deteccao/**")
                                                .uri("http://localhost:14010"))

                                .build();
        }

}
