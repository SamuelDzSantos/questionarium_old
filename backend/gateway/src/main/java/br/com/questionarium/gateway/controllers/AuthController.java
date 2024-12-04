package dev.questionarium.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.questionarium.entities.RegistrationRequest;
import dev.questionarium.services.AuthUserService;
import dev.questionarium.types.dtos.LoginForm;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthUserService authUserService;
    private final RestTemplate restTemplate;

    @PostMapping("login")
    public Mono<String> login(@RequestBody LoginForm form) {
        return authUserService.login(form.email(), form.password());
    }

    @PostMapping("register")
    public void register(@RequestBody RegistrationRequest request) {
        authUserService.register(request.email(), request.password());
    }

    @GetMapping("test")
    public String test(Authentication authentication) {
        Jwt token = (Jwt) authentication.getPrincipal();
        System.out.println(token.hasClaim("user_id"));
        System.out.println(token.getClaimAsString("user_id"));
        return "Test";
    }

    @GetMapping("current")
    public Void current(Authentication authentication){
        Jwt token = (Jwt) authentication.getPrincipal();
        String userId = token.getClaimAsString("user_id");
        restTemplate.
    } 

}
