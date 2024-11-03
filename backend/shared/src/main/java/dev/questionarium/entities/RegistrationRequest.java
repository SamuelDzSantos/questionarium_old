package dev.questionarium.entities;

import dev.questionarium.types.Role;
import lombok.Builder;

@Builder
public record RegistrationRequest(String name, String email, String password, Role role) {
}
