package br.com.questionarium.gateway.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Value("${key.secret}")
	private String secret;

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				// 1) API stateless
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(reactiveCorsConfigurationSource()))

				// 2) Desabilita httpBasic e formLogin via DSL
				.httpBasic(httpBasic -> httpBasic.disable())
				.formLogin(formLogin -> formLogin.disable())

				// 3) Autorizações: libera apenas os públicos, roteia todo o resto
				.authorizeExchange(exchanges -> exchanges
						.pathMatchers(HttpMethod.POST, "/auth/login", "/users", "/users/confirm")
						.permitAll()
						.pathMatchers(HttpMethod.GET, "/users/email")
						.permitAll()
						.anyExchange()
						.permitAll());

		return http.build();
	}

	@Bean
	public CorsConfigurationSource reactiveCorsConfigurationSource() {
		CorsConfiguration cfg = new CorsConfiguration();
		cfg.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
		cfg.setAllowedHeaders(Arrays.asList("*"));
		cfg.setAllowCredentials(true);
		cfg.setExposedHeaders(Arrays.asList("Authorization"));

		UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
		src.registerCorsConfiguration("/**", cfg);
		return src;
	}

	@Bean
	public ReactiveJwtDecoder jwtDecoder() {
		SecretKey secretKey = new SecretKeySpec(
				secret.getBytes(StandardCharsets.UTF_8),
				"HmacSHA256");
		return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
	}
}
