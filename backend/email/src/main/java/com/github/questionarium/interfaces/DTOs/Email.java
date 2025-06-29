package com.github.questionarium.interfaces.DTOs;

public record Email(String subject,
        String message,
        String emailTo) {
};