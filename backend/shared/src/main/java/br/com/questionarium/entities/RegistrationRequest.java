package br.com.questionarium.entities;

import br.com.questionarium.types.Role;

public record RegistrationRequest(String name, String email, String password, Role role) {
}
