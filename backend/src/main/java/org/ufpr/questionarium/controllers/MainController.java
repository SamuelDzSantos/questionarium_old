package org.ufpr.questionarium.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ufpr.questionarium.dtos.Login;
import org.ufpr.questionarium.dtos.RegisterForm;
import org.ufpr.questionarium.services.JwtUtils;
import org.ufpr.questionarium.services.UserService;

@RestController
@RequestMapping("/")
public class MainController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    MainController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping("/")
    public String main() {
        return "hello";
    }

    @GetMapping("/secured")
    public String secured() {
        return "Secured";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login login) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login.getEmail(),
                login.getPassword());
        this.authenticationManager.authenticate(token);
        return ResponseEntity.ok(jwtUtils.generateToken(login.getEmail()));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterForm registerForm) {
        this.userService.register(registerForm);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/hello")
    public ResponseEntity<String> apiHello() {
        return ResponseEntity.ok("Hello from Api");
    }

}
