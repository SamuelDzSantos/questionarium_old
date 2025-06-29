package com.github.questionarium.interfaces.email;

public record Email(String subject,
                String message,
                String emailTo) {
};