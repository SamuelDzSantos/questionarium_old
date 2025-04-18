package com.example.scg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.scg.filters.JwtAuthenticationFilter;

@Configuration
public class RouteConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${services.auth.host}")
    private String authServiceUrl;

    @Value("${services.user.host}")
    private String userServiceUrl;

    @Value("${services.email.host}")
    private String emailServiceUrl;

    @Value("${services.question.host}")
    private String questionServiceUrl;

    @Value("${services.assessment.host}")
    private String assessmentServiceUrl;

    @Value("${services.report.host}")
    private String reportServiceUrl;

    @Value("${services.answer-sheet.host}")
    private String answerSheetServiceUrl;

    @Value("${services.ai.host}")
    private String aiServiceUrl;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Rotas públicas (sem autenticação)
                .route("auth_register", r -> r
                        .path("/auth/register")
                        .or()
                        .path("/auth/register/**")
                        .uri(authServiceUrl))
                .route("auth_login", r -> r
                        .path("/auth/login")
                        .or()
                        .path("/auth/login/**")
                        .uri(authServiceUrl))
                .route("auth_refresh", r -> r
                        .path("/auth/refresh")
                        .uri(authServiceUrl))
                .route("auth_forgot_password", r -> r
                        .path("/auth/forgot-password")
                        .or()
                        .path("/auth/forgot-password/**")
                        .uri(authServiceUrl))
                .route("auth_reset_password", r -> r
                        .path("/auth/reset-password")
                        .or()
                        .path("/auth/reset-password/**")
                        .uri(authServiceUrl))
                .route("auth_verify", r -> r
                        .path("/auth/verify/**")
                        .uri(authServiceUrl))
                
                // Rotas de serviço Auth (protegidas)
                .route("auth_service_protected", r -> r
                        .path("/auth/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri(authServiceUrl))
                
                // Rotas de serviço User
                .route("user_service", r -> r
                        .path("/users/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri(userServiceUrl))
                
                // Rotas de serviço Email
                .route("email_service", r -> r
                        .path("/email/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri(emailServiceUrl))
                
                // Rotas de serviço Question
                .route("question_service", r -> r
                        .path("/questions/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri(questionServiceUrl))
                
                // Rotas de serviço Assessment
                .route("assessment_service", r -> r
                        .path("/assessments/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri(assessmentServiceUrl))
                
                // Rotas de serviço Report
                .route("report_service", r -> r
                        .path("/reports/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri(reportServiceUrl))
                
                // Rotas de serviço Answer-Sheet
                .route("answer_sheet_service", r -> r
                        .path("/answer-sheets/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri(answerSheetServiceUrl))
                
                // Rotas de serviço AI
                .route("ai_service", r -> r
                        .path("/ai/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri(aiServiceUrl))
                
                .build();
    }
}