package dev.questionarium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Value("${key.secret}")
	private String secret;

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				.authorizeExchange(exchanges -> exchanges
						.pathMatchers("/auth/login", "/auth/register").permitAll()
						.anyExchange().authenticated())
				.csrf(csrs -> csrs.disable())
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(withDefaults()));

		return http.build();
	}

	@Bean
	ReactiveJwtDecoder jwtDecoder() {
		SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "AES");
		return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
	}

	@Bean
	JwtEncoder jwtEncoder() {
		SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "AES");
		JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(secretKey);
		return new NimbusJwtEncoder(immutableSecret);
	}

}
