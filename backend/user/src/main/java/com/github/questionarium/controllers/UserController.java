package com.github.questionarium.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.model.User;
import com.github.questionarium.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/current")
    public User getUser(@RequestHeader("X-User-Id") Long userId) {
        User user = userService.getUser(userId);
        log.info("Retornando usuário: {}", user.toString());
        return user;
    }
}
