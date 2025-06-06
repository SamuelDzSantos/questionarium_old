package br.com.questionarium.gateway.filter;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Obtém informações da requisição atual
        String requestPath = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name()
                : "UNKNOWN";
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // Loga a entrada da requisição
        System.out.println("[Gateway] Requisição recebida: " + method + " " + requestPath);
        if (authHeader != null) {
            System.out.println("[Gateway] Cabeçalho Authorization: "
                    + authHeader.substring(0, Math.min(authHeader.length(), 20)) + "...");
        } else {
            System.out.println("[Gateway] Sem cabeçalho Authorization.");
        }

        // Marca o início para medir o tempo de resposta
        long startTime = System.currentTimeMillis();

        // Continua o processamento da requisição
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // Após a resposta ser gerada, calcula o tempo de resposta
            long duration = System.currentTimeMillis() - startTime;

            // Obtém o status HTTP da resposta
            HttpStatus status = (HttpStatus) exchange.getResponse().getStatusCode();
            int statusCode = (status != null) ? status.value() : 0;

            // Loga a conclusão da requisição
            System.out.println("[Gateway] Resposta concluída: " + method + " " + requestPath
                    + " | Status: " + statusCode
                    + " | Tempo: " + duration + " ms");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Define a ordem de execução do filtro (negativo = executa antes dos outros)
    }
}
