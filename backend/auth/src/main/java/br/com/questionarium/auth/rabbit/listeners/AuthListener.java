package br.com.questionarium.auth.rabbit.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import br.com.questionarium.auth.config.RabbitMQConfig;
import br.com.questionarium.auth.interfaces.CreatedUserAuthDTO;
import br.com.questionarium.entities.UserCreatedEvent;
import br.com.questionarium.auth.service.AuthUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@RabbitListener(queues = RabbitMQConfig.AUTH_QUEUE)
public class AuthListener {

    private final AuthUserService authUserService;

    /**
     * Trata mensagens do tipo CreatedUserAuthDTO vindas da fila "auth-queue".
     * Registra o usuário no AuthUserService.
     */
    @RabbitHandler
    public void receiveCreatedUser(@Payload CreatedUserAuthDTO authDTO) {
        try {
            authUserService.register(authDTO);
            log.info("AuthListener: CreatedUserAuthDTO processado para login={}", authDTO.getLogin());
        } catch (Exception e) {
            log.error("AuthListener: falha ao processar CreatedUserAuthDTO para login={}", authDTO.getLogin(), e);
        }
    }

    /**
     * Trata eventos do tipo UserCreatedEvent vindos da mesma fila "auth-queue".
     * Constrói um CreatedUserAuthDTO e o registra no AuthUserService.
     */
    @RabbitHandler
    public void handleUserCreatedEvent(@Payload UserCreatedEvent event) {
        log.info("AuthListener recebeu UserCreatedEvent: userId={} email={}", event.getUserId(), event.getEmail());

        CreatedUserAuthDTO dto = new CreatedUserAuthDTO(
                event.getEmail(), // login
                event.getRawPassword(), // senha “crua”
                event.getRole() // role que veio do shared
        );

        try {
            authUserService.register(dto);
            log.info("AuthListener: UserCreatedEvent convertido e registrado para email={}", event.getEmail());
        } catch (Exception e) {
            log.error("AuthListener: falha ao registrar usuário para email={}", event.getEmail(), e);
        }
    }
}
