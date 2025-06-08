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
                                // 1) Qualquer rota /auth/** é encaminhada para AuthService (porta 14001)
                                // com um Circuit Breaker configurado para fallback
                                .route("auth", r -> r.path("/auth/**")
                                                .filters(f -> f.circuitBreaker(cb -> cb
                                                                .setName("authCircuitBreaker")
                                                                .setFallbackUri("forward:/fallback/auth")))
                                                .uri("http://localhost:14001"))

                                // User Service
                                .route("user-service", r -> r.path("/users/**")
                                                .filters(f -> f.circuitBreaker(cb -> cb
                                                                .setName("userCircuitBreaker")
                                                                .setFallbackUri("forward:/fallback/users")))
                                                .uri("http://localhost:14002"))

                                // Question Service
                                .route("question-service", r -> r.path("/questions", "/questions/**")
                                                .filters(f -> f.circuitBreaker(cb -> cb
                                                                .setName("questionCircuitBreaker")
                                                                .setFallbackUri("forward:/fallback/questions")))
                                                .uri("http://localhost:14004"))

                                .build();
        }
}
