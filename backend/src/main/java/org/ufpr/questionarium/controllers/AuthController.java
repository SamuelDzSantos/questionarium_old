package org.ufpr.questionarium.controllers;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.ufpr.questionarium.dtos.LoginForm;
import org.ufpr.questionarium.dtos.LoginResult;
import org.ufpr.questionarium.dtos.RegisterForm;
import org.ufpr.questionarium.dtos.UserData;
import org.ufpr.questionarium.services.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@Configuration
public class AuthController {

    private final AuthenticationService authenticationService;

    AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(@Valid @RequestBody LoginForm login) {
        return ResponseEntity.ok().body(this.authenticationService.login(login));
    }

    @PostMapping("/register")
    public ResponseEntity<UserData> register(@Valid @RequestBody RegisterForm registerForm) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(this.authenticationService.register(registerForm));
    }

}