package dev.questionarium.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import dev.questionarium.EmailService;
import dev.questionarium.entities.Email;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "emailQueue")
    public void listen(@Payload Email email) {
        emailService.sendSimpleMailMessage(email);
    }

}
