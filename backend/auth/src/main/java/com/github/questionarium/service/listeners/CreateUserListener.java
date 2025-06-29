package com.github.questionarium.service.listeners;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.AuthUserRegisterDTO;
import com.github.questionarium.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUserListener {

    private final AuthService userService;
    private final String CREATE_AUTH_USER_EVENT = "CREATE_AUTH_USER_EVENT";
    private final String REVERT_CREATE_AUTH_USER_EVENT = "REVERT_CREATE_AUTH_USER_EVENT";
    private final String CREATE_CONFIRMATION_TOKEN_EVENT = "CREATE_CONFIRMATION_TOKEN_EVENT";

    @RabbitListener(queues = CREATE_AUTH_USER_EVENT)
    public Long handleCreateUser(AuthUserRegisterDTO registerForm) {
        log.info("Requisição de criação recebida formulário: {}", registerForm.toString());
        try {
            return userService.register(registerForm).getId();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @RabbitListener(queues = REVERT_CREATE_AUTH_USER_EVENT)
    public boolean handleRevertUserCreate(Long userId) {
        log.info("Revertendo criação do usuário de id : {}", userId.toString());
        if (userService.userExists(userId)) {
            log.info("Usuário de id {} não existe no banco. Retornando sucesso!", userId);
            return true;
        }
        try {
            userService.remove(userId);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    @RabbitListener(queues = CREATE_CONFIRMATION_TOKEN_EVENT)
    public String handleCreateUser(Map<String, Long> user) {
        log.info("Gerando token para ativação do usuário", user.get("userId"));
        try {
            return userService.generateConfirmationToken(user.get("userId"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
