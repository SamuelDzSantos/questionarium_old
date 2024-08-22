package dev.questionarium.entities;

import dev.questionarium.types.Role;

public record RegistrationRequest(String name, String email, String password, Role role) {
}
