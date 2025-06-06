package br.com.questionarium.entities;

public record PasswordPatch(String password, String confirmPassword, String token) {
}
