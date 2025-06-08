package br.com.questionarium.auth.service;

import br.com.questionarium.auth.interfaces.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {

    private final RestTemplate restTemplate;

    public Long getUserIdByEmail(String email) {
        String url = "http://localhost:14002/users/email?email=" + email;
        log.info("Consultando UserService em: {}", url);

        try {
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Long userId = response.getBody().getId();
                log.info("UserService retornou userId={}", userId);
                return userId;
            } else {
                throw new RuntimeException("Erro ao buscar usuário pelo e-mail: " + email);
            }
        } catch (RestClientException ex) {
            log.error("Erro na comunicação com UserService para email={}", email, ex);
            throw ex; // Será tratado pelo GlobalExceptionHandler
        }
    }
}
