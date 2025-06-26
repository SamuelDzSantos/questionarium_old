package br.com.questionarium.gateway.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				// API stateless
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(reactiveCorsConfigurationSource()))

				// desativa basic e formLogin
				.httpBasic(basic -> basic.disable())
				.formLogin(form -> form.disable())

				// não faz validação: roteia tudo
				.authorizeExchange(exchanges -> exchanges
						.anyExchange().permitAll());

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
}
