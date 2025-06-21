package br.com.questionarium.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
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
                                "/record-assessments/**")
                        .filters(f -> f.circuitBreaker(cb -> cb
                                .setName("assessmentCircuitBreaker")
                                .setFallbackUri("forward:/fallback/assessment")))
                        .uri("http://localhost:14005"))

                .build();
    }
}
