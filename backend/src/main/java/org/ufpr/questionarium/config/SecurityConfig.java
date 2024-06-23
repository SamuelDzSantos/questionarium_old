package org.ufpr.questionarium.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.ufpr.questionarium.filters.NonApiRequestToRootPathForwarderFilterRegistrationbean;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

// Arquivo de configuração do spring security e definição de Beans associados. Todos os caminhos redirecionados para o angular('/') são permitidos.
// Recursos nos caminhos /resources e urls de login e cadastro são permitidas , todos os outros caminhos requerem autenticação.

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final RsaKeyProperties rsaKeys;
    private final NonApiRequestToRootPathForwarderFilterRegistrationbean nonApiFilter;

    public SecurityConfig(RsaKeyProperties rsaKeys,
            NonApiRequestToRootPathForwarderFilterRegistrationbean nonApiFilter) {
        this.rsaKeys = rsaKeys;
        this.nonApiFilter = nonApiFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/login", "/api/register", "/api/hello", "/api").permitAll()
                        .requestMatchers("/resources/*").permitAll()
                        .anyRequest().authenticated())
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .csrf((csrf) -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(nonApiFilter, AuthorizationFilter.class)
                .build();
    }

    // AuthenticationManager responsavel por gerenciar o login sem o uso de JWT
    // token.

    @Bean
    AuthenticationManager getAuthenticationManager(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    // Encoder utilizado pelo DaoAuthenticationProvider no login sem o JWT token.

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Encoder e Decoder utilizados pelo oauth2 para validar os JWT tokens das
    // requisições e gerar JWT tokens em JWTUtils.

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.getPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.getPublicKey()).privateKey(rsaKeys.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

}