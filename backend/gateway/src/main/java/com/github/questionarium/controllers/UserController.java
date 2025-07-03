package com.github.questionarium.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.questionarium.interfaces.DTOs.AuthDataDTO;
import com.github.questionarium.interfaces.DTOs.User;
import com.github.questionarium.interfaces.DTOs.UserData;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final WebClient webClient;

    @GetMapping("/data")
    public Mono<UserData> getUserData(@RequestHeader("X-User-Id") Long userId) {
        System.out.println("OPa");
        ;
        Mono<AuthDataDTO> authUserMono = webClient.get()
                .uri("http://localhost:14001/auth")
                .header("X-User-Id", userId.toString()).retrieve().bodyToMono(AuthDataDTO.class).doOnNext((value) -> {
                    System.out.println(value.email());
                });

        // authUserMono.subscribe();
        Mono<User> userDataMono = webClient.get()
                .uri("http://localhost:14002/users/current")
                .header("X-User-Id", userId.toString())
                .retrieve()
                .bodyToMono(User.class);

        // userDataMono.subscribe();

        return Mono.zip(authUserMono, userDataMono).map(tuple -> {
            AuthDataDTO authUserData = tuple.getT1();
            User userData = tuple.getT2();
            System.out.println(authUserData.toString());
            System.out.println(authUserData);
            System.out.println(userData);
            return new UserData(authUserData.id(), userData.name(),
                    authUserData.email(), userData.image());
        });
    }

}
