package com.github.questionarium.service.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.EmailPatch;
import com.github.questionarium.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatchUserListener {

    private final AuthService userService;
    private final String UPDATE_AUTH_USER_EMAIL_EVENT = "UPDATE_AUTH_USER_EMAIL_EVENT";

    @RabbitListener(queues = UPDATE_AUTH_USER_EMAIL_EVENT)
    public Long handleCreateUser(EmailPatch data) {
        log.info("Atualizando email de {}", data.userId());
        try {
            return userService.updateEmail(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1L;
        }
    }
}
