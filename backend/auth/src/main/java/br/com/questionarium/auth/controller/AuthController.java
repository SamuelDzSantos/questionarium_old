package br.com.questionarium.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.questionarium.auth.interfaces.LoginFormDTO;
import br.com.questionarium.auth.service.AuthUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserService authUserService;

    /**
     * POST /auth/login
     * Recebe JSON { "login": "...", "password": "..." } e devolve o JWT em JSON:
     * { "token": "..." }.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginFormDTO loginForm) {
        String token = authUserService.login(loginForm);
        return ResponseEntity.ok(Map.of("token", token));
    }

    /**
     * GET /auth/authenticated
     * Apenas verifica se o token válido autoriza qualquer usuário (ROLE_USER ou
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

}
