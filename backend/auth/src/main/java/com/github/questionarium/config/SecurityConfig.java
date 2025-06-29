package com.github.questionarium.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return passwordEncoder;
    }

    @Bean
    JwtEncoder jwtEncoder() {
        System.out.println();
        System.out.println(jwtSecret);
        SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), "AES");
        JWKSource<SecurityContext> jwkSource = new ImmutableSecret<>(secretKey);
        return new NimbusJwtEncoder(jwkSource);
    }
}
