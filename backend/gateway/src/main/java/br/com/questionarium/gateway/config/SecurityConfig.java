package dev.questionarium.config;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

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
