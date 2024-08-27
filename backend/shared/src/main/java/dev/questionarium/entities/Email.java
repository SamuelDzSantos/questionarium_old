package dev.questionarium.entities;

import java.io.Serializable;

import lombok.Builder;

@Builder
public record Email(String message, String subject, String emailTo) implements Serializable {
}
