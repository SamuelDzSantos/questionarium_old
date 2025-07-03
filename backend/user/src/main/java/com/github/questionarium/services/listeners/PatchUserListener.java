package com.github.questionarium.services.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.UsernamePatch;
import com.github.questionarium.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatchUserListener {

    private final UserService userService;
    private final String UPDATE_USER_NAME_EVENT = "UPDATE_USER_NAME_EVENT";

    @RabbitListener(queues = UPDATE_USER_NAME_EVENT)
    public boolean handleUserCreate(UsernamePatch patch) {
        log.info("Criando usuário , dados enviados: {}", patch.toString());
        try {
            userService.updateUsername(patch);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
