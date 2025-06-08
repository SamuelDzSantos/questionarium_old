package br.com.questionarium.email_service.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.questionarium.email_service.services.EmailService;
import br.com.questionarium.entities.Email;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EmailConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);

    private final EmailService emailService;

    @RabbitListener(queues = "${rabbitmq.email.queue}", containerFactory = "rabbitListenerContainerFactory")
    public void listen(@Payload Email email) {
        logger.info("Recebido evento de e-mail para {}", email.getEmailTo());
        try {
            emailService.sendSimpleMailMessage(email);
            logger.info("Processamento concluído para e-mail {}", email.getEmailTo());
        } catch (Exception e) {
            logger.error("Erro ao processar evento de e-mail para {}", email.getEmailTo(), e);
            throw e; // para permitir retry ou DLQ
        }
    }
}
