package br.com.questionarium.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Sobe a aplicação Gateway em uma porta aleatória, isolada do ambiente local
// Evita conflitos de porta e simula um contexto real de execução para o Gateway
@AutoConfigureWebTestClient
// Configura automaticamente o WebTestClient (cliente HTTP reativo) para ser
// usado nos testes
// Permite fazer requisições HTTP simuladas contra o Gateway
class GatewayApplicationTests {

    @Autowired
    private WebTestClient webTestClient;
    // Injeta o WebTestClient, que permite enviar requisições HTTP e verificar as
    // respostas

    @Test
    void testAuthRoute() {
        // Testa se a rota /auth/login está corretamente roteada pelo Gateway para o
        // Auth Service
        // Envia uma requisição POST com JSON simulando um login

        webTestClient.post()
                .uri("/auth/login") // Endpoint testado
                .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                .bodyValue("{\"login\":\"testuser\",\"password\":\"testpass\"}") // Corpo da requisição simulada
                .exchange() // Envia a requisição
                .expectStatus().is2xxSuccessful(); // Verifica se a resposta tem status HTTP 2xx (ou poderia ser
                                                   // .isUnauthorized())
    }
}
