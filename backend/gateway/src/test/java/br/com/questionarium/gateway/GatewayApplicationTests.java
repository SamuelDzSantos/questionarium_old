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

    @Test
    void testUserRoute() {
        // Testa se a rota /users está corretamente roteada pelo Gateway para o User
        // Service
        // Envia uma requisição POST para criar um usuário

        webTestClient.post()
                .uri("/users") // Endpoint testado
                .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                .bodyValue(
                        "{\"name\":\"Test User\",\"email\":\"testuser@example.com\",\"password\":\"123456\",\"roles\":[\"USER\"]}")
                // Corpo da requisição
                .exchange() // Envia a requisição
                .expectStatus().is2xxSuccessful(); // Verifica se a resposta tem status HTTP 2xx
    }

    // @Test
    // void testCorsForQuestionsRoute() {
    //     // Testa se o CORS está configurado corretamente para a rota /questions

    //     webTestClient.options()
    //             .uri("/questions") // Endpoint testado
    //             .header("Origin", "http://localhost:4200") // Origem do front-end
    //             .header("Access-Control-Request-Method", "GET") // Método que o front-end pretende usar
    //             .exchange()
    //             .expectStatus().isOk() // Espera um 200 OK
    //             .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://localhost:4200") // Deve permitir a origem
    //             .expectHeader().exists("Access-Control-Allow-Methods"); // Deve informar os métodos permitidos
    // }

}
