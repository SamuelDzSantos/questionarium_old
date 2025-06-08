package br.com.questionarium.email_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.questionarium.entities.Email;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendSimpleMailMessage(Email email) {
        logger.info("Preparando para enviar e-mail para {}", email.getEmailTo());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(email.getSubject());
            message.setFrom(mailFrom);
            message.setTo(email.getEmailTo());
            message.setText(email.getMessage());
            emailSender.send(message);
            logger.info("E-mail enviado com sucesso para {}", email.getEmailTo());
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail para {}", email.getEmailTo(), e);
            throw new RuntimeException("Erro ao enviar e-mail", e); // rethrow
        }
    }
}
