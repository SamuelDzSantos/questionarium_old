package com.github.questionarium.interfaces.DTOs;

public record PasswordUpdateForm(String newPassword, String confirmPassword, String currentPassword) {

}
