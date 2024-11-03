package dev.questionarium;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import dev.questionarium.entities.Email;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendSimpleMailMessage(Email email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(email.subject());
            message.setFrom("samueldzsantos@gmail.com");
            message.setTo(email.emailTo());
            message.setText(email.message());
            emailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
