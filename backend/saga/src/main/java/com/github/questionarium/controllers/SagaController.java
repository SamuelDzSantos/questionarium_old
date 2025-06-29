package com.github.questionarium.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.interfaces.user.RegisterForm;
import com.github.questionarium.service.saga.create_user.UserCreationOrchestratorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/saga")
public class SagaController {

    private final UserCreationOrchestratorService userCreationOrchestratorService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterForm registerForm) {
        userCreationOrchestratorService.setRegisterForm(registerForm);
        userCreationOrchestratorService.performAction();
    }

}
