package com.github.questionarium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}