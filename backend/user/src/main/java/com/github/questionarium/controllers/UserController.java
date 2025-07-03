package com.github.questionarium.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam(required = true) MultipartFile file,
            @RequestHeader("X-User-Id") Long userId)
            throws IOException {

        userService.saveImage(userId, file.getBytes());
        return ResponseEntity.status(HttpStatus.OK).body("Image uploaded successfully.");
    }
}
