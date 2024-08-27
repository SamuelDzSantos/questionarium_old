package dev.questionarium.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import dev.questionarium.entities.Email;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmailProducer {

    private final AmqpTemplate template;

    public void sendEmail(Email email) throws JsonProcessingException, AmqpException {

        template.convertAndSend("emailQueue", email);
    }

}
