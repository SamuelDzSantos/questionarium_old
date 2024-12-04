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
            .route("auth", r -> r.path("/auth/**")
                .uri("http://localhost:14001"))

            .route("user-service", r -> r.path("/user/**")
                .uri("http://localhost:14002"))

            .route("question-service", r -> r.path("/question/**")
                .uri("http://localhost:14004"))
            
            .route("evaluation-service", r -> r.path("/evaluation/**")
                .uri("http://localhost:14005"))
            
            .route("report-service", r -> r.path("/report/**")
                .uri("http://localhost:14006"))

            .build();
    }
}
