package com.github.questionarium.controllers;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.interfaces.user.RegisterForm;
import com.github.questionarium.interfaces.user.UserPatch;
import com.github.questionarium.service.saga.create_user.UserCreationOrchestratorService;
import com.github.questionarium.service.saga.patch_user.PatchUserOrchestratorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/saga")
public class SagaController {

    private final UserCreationOrchestratorService userCreationOrchestratorService;
    private final PatchUserOrchestratorService patchUserOrchestratorService;

    @PostMapping("/register")
    public void register(@RequestBody RegisterForm registerForm) {
        userCreationOrchestratorService.setRegisterForm(registerForm);
        userCreationOrchestratorService.performAction();
    }

    @PatchMapping("/update")
    public boolean updateUser(@RequestBody UserPatch userPatch, @RequestHeader("X-User-id") Long userId) {
        this.patchUserOrchestratorService.setUserId(userId);
        this.patchUserOrchestratorService.setUserPatch(userPatch);
        this.patchUserOrchestratorService.performAction();
        return true;
    }

}
