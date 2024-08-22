package dev.questionarium.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.questionarium.entities.AuthRequest;
import dev.questionarium.entities.AuthResponse;
import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.entities.UserData;
import dev.questionarium.service.AuthService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody RegistrationRequest request) {
        this.authService.register(request);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(this.authService.login(request));
    }

    @GetMapping("/current")
    public ResponseEntity<UserData> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(this.authService.getCurrent(authentication.getPrincipal()));
    }
}
