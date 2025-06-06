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
                                .route("auth", r -> r.path("/auth/**")
                                                .uri("http://localhost:14001"))

                                // 2) Qualquer rota /users/** é encaminhada para UserService (porta 14002)
                                .route("user-service", r -> r.path("/users/**")
                                .uri("http://localhost:14002"))

                                // 3) Qualquer rota /questions/** é encaminhada para QuestionService (porta 14004)
                                .route("question-service", r -> r.path("/questions/**")
                                                .uri("http://localhost:14004"))

                                .build();
        }
}
