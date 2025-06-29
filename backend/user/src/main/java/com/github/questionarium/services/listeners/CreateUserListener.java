package com.github.questionarium.services.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.UserRegisterData;
import com.github.questionarium.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUserListener {

    private final UserService userService;
    private final String CREATE_USER_EVENT = "CREATE_USER_EVENT";

    @RabbitListener(queues = CREATE_USER_EVENT)
    public boolean handleUserCreate(UserRegisterData userRegisterData) {
        log.info("Criando usuário , dados enviados: {}", userRegisterData.toString());
        try {
            userService.register(userRegisterData.userId(), userRegisterData.name());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
