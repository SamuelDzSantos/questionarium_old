package com.github.questionarium.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.github.questionarium.service.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoggingFilter implements WebFilter {

        private final JwtUtils jwtUtils;

        @Override
        public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {

                // Obtém informações da requisição atual
                String requestPath = exchange.getRequest().getPath().toString();
                String method = exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name()
                                : "UNKNOWN";
                String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

                log.info("Header {}", authHeader);

                // Marca o início para medir o tempo de resposta
                long startTime = System.currentTimeMillis();

                // Loga a entrada da requisição
                System.out.println("[Gateway] Requisição recebida: " + method + " " + requestPath);
                if (authHeader != null) {
                        System.out.println("[Gateway] Cabeçalho Authorization: "
                                        + authHeader.substring(0, Math.min(authHeader.length(), 20)) + "...");
                        // Continua o processamento da requisição

                        return jwtUtils.getDecodeResult(authHeader.substring(7))
                                        .flatMap(result -> {
                                                log.info("Adicionando X-User-Id: {}", result.userId());
                                                log.info("Adicionando X-User-isAdmin: {}", result.isAdmin());
                                                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                                                .header("X-User-Id", result.userId().toString())
                                                                .header("X-User-isAdmin",
                                                                                result.isAdmin().toString())
                                                                .build();
                                                ServerWebExchange mutatedExchange = exchange.mutate()
                                                                .request(mutatedRequest)
                                                                .build();

                                                return chain.filter(mutatedExchange)
                                                                .then(Mono.fromRunnable(() -> {
                                                                        // Após a resposta ser gerada, calcula o tempo
                                                                        // de resposta
                                                                        long duration = System.currentTimeMillis()
                                                                                        - startTime;

                                                                        // Obtém o status HTTP da resposta
                                                                        HttpStatus status = (HttpStatus) exchange
                                                                                        .getResponse().getStatusCode();
                                                                        int statusCode = (status != null)
                                                                                        ? status.value()
                                                                                        : 0;

                                                                        // Loga a conclusão da requisição
                                                                        System.out.println(
                                                                                        "[Gateway] Resposta concluída: "
                                                                                                        + method + " " +
                                                                                                        requestPath
                                                                                                        + " | Status: "
                                                                                                        + statusCode
                                                                                                        + " | Tempo: "
                                                                                                        + duration
                                                                                                        + " ms");
                                                                }));
                                        });
                } else {
                        System.out.println("[Gateway] Sem cabeçalho Authorization.");
                        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                                // Após a resposta ser gerada, calcula o tempo de resposta
                                long duration = System.currentTimeMillis() - startTime;

                                // Obtém o status HTTP da resposta
                                HttpStatus status = (HttpStatus) exchange.getResponse().getStatusCode();
                                int statusCode = (status != null) ? status.value() : 0;

                                // Loga a conclusão da requisição
                                System.out.println("[Gateway] Resposta concluída: " + method + " " +
                                                requestPath
                                                + " | Status: " + statusCode
                                                + " | Tempo: " + duration + " ms");
                        }));
                }

        }

}
