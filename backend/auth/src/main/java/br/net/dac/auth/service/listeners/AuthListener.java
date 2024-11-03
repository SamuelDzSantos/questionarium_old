package br.net.dac.auth.service.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import br.net.dac.auth.service.AuthUserService;
import br.net.dac.entities.auth.CreatedUserAuthDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@RabbitListener(queues = "auth-queue")
public class AuthListener {

    private final AuthUserService authUserService;

    // @RabbitListener(queues = "auth-queue")
    @RabbitHandler
    public boolean receive(@Payload CreatedUserAuthDTO authDTO) {
        try {
            // authUserService.register(authDTO);

            throw new RuntimeException("Erro!");
            // return true;
        } catch (Exception e) {
            return false;
        }
    }

}
