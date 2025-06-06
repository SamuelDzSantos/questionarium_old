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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Value("${key.secret}")
	private String secret;

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				// 1) Desabilita CSRF (API stateless)
				.csrf(csrf -> csrf.disable())

				// 2) Habilita CORS, usando nosso bean reactiveCorsConfigurationSource()
				.cors(cors -> cors.configurationSource(reactiveCorsConfigurationSource()))

				// 3) Regras de autorização:
				// - POST /auth/login e POST /users são públicos (sem JWT)
				// - Qualquer outra rota exige JWT válido
				.authorizeExchange(exchanges -> exchanges
						.pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.pathMatchers(HttpMethod.POST, "/users").permitAll()
						.anyExchange().authenticated())

				// 4) Resource Server: o gateway valida o JWT, não gera tokens
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(jwtSpec -> {
							// Usará o ReactiveJwtDecoder definido abaixo
						}));

		return http.build();
	}

	// Bean responsável por configurar CORS para chamadas vindas do front-end (ex:
	// http://localhost:4200)
	@Bean
	public CorsConfigurationSource reactiveCorsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// Permitir apenas o domínio do front-end durante desenvolvimento
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

		// Métodos HTTP permitidos
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));

		// Permitir todos os headers
		config.setAllowedHeaders(Arrays.asList("*"));

		// Permitir credenciais (cookies, headers de autorização, etc.)
		config.setAllowCredentials(true);

		// Expor o header Authorization para que o browser possa lê-lo
		config.setExposedHeaders(Arrays.asList("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		// Aplica esta configuração a todas as rotas do gateway
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	// JwtDecoder para validar os tokens assinados pelo AuthService.
	// Alterado o algoritmo de "AES" para "HmacSHA256", que é compatível com HS256
	// (HMAC) usado na assinatura JWT.
	@Bean
	public ReactiveJwtDecoder jwtDecoder() {
		// Converte a string secreta em bytes UTF-8 e cria uma chave HMAC-SHA256
		SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
	}
}
