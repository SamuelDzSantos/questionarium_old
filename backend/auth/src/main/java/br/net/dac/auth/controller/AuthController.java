package br.net.dac.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.net.dac.auth.interfaces.LoginFormDTO;
import br.net.dac.auth.service.AuthUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserService authUserService;

    @PostMapping("/login")
    public String login(@RequestBody LoginFormDTO loginForm) {
        return this.authUserService.login(loginForm);
    }

    @GetMapping("/authenticated")
    public ResponseEntity<Boolean> authenticated() {
        return ResponseEntity.ok(true);
    }

    @GetMapping("/employee")
    public ResponseEntity<Boolean> authenticatedEmployee() {
        return ResponseEntity.ok(true);
    }

}
