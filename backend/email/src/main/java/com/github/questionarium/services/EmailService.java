package com.github.questionarium.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.Email;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendSimpleMailMessage(Email email) {
        logger.info("Preparando para enviar e-mail para {}", email.emailTo());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(email.subject());
            message.setFrom(mailFrom);
            message.setTo(email.emailTo());
            message.setText(email.message());
            emailSender.send(message);
            logger.info("E-mail enviado com sucesso para {}", email.emailTo());
        } catch (Exception e) {
            logger.error("Falha ao enviar e-mail para {}", email.emailTo(), e);
            throw new RuntimeException("Erro ao enviar e-mail", e); // rethrow
        }
    }
}
