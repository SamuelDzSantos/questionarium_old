package com.github.questionarium.services.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.github.questionarium.interfaces.DTOs.Email;
import com.github.questionarium.services.EmailService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmailConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);
    public static final String EMAIL_QUEUE = "SEND_EMAIL_EVENT";

    private final EmailService emailService;

    @RabbitListener(queues = EMAIL_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public boolean listen(@Payload Email email) {
        logger.info("Recebido evento de e-mail para {}", email.emailTo());
        try {
            emailService.sendSimpleMailMessage(email);
            logger.info("Processamento concluído para e-mail {}", email.emailTo());
            return true;
        } catch (Exception e) {
            logger.error("Erro ao processar evento de e-mail para {}", email.emailTo(), e);
            return false;// para permitir retry ou DLQ
        }
    }
}
