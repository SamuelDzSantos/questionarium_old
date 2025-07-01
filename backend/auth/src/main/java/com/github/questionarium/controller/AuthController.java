package com.github.questionarium.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.interfaces.DTOs.AuthDataDTO;
import com.github.questionarium.interfaces.DTOs.LoginFormDTO;
import com.github.questionarium.interfaces.DTOs.PasswordUpdateForm;
import com.github.questionarium.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * POST /auth/login
     * Recebe JSON { "login": "...", "password": "..." } e devolve o JWT em JSON:
     * { "token": "..." }.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginFormDTO loginForm) {
        String token = authService.login(loginForm);
        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * GET /auth/authenticated
     * Verifica se o token válido autoriza qualquer usuário (ROLE_USER ou
     * ROLE_ADMIN).
     */
    @GetMapping("/authenticated")
    public ResponseEntity<Boolean> authenticated() {
        return ResponseEntity.ok(true);
    }

    /**
     * GET /auth/admin
     * Somente acessível a usuários com ROLE_ADMIN (configurado no SecurityConfig).
     */
    @GetMapping("/admin")
    public ResponseEntity<Boolean> authenticatedAdmin() {
        return ResponseEntity.ok(true);
    }

    /**
     * Retorna dados do usuário logado
     */
    @GetMapping
    public AuthDataDTO getUser(@RequestHeader("X-User-Id") Long userId) {
        log.info("Buscando dados do user {}", userId);
        return this.authService.getUserData(userId);
    }

    @PatchMapping("/email/{token}")
    public ResponseEntity<Boolean> confirmEmail(@PathVariable String token) {
        log.info("Recebido token {}", token);
        authService.validateEmail(token);
        return ResponseEntity.ok(true);
    }

    @PostMapping("/password/reset/{token}")
    public Boolean resetPasswordToken(@PathVariable String token, @RequestBody PasswordUpdateForm passwordUpdateForm) {
        return authService.resetPasswordToken(token, passwordUpdateForm);
    }

    @PostMapping("/password/reset")
    public String resetPassword(@RequestParam String email) {
        return authService.resetPassword(email);
    }

    /**
     * Update da senha do usuário logado
     */
    @PatchMapping("/password")
    public Boolean updateUserPassword(@RequestHeader("X-User-id") Long userId,
            @RequestBody PasswordUpdateForm passwordUpdateForm) {
        log.info("Atualizando senha do user {}", userId);

        return this.authService.updatePassword(userId, passwordUpdateForm.currentPassword(),
                passwordUpdateForm.confirmPassword(),
                passwordUpdateForm.newPassword());

    }

    /**
     * Reset de senha
     */

}
