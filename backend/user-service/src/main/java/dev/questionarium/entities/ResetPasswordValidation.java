package dev.questionarium.entities;

import lombok.Builder;

@Builder
public record ResetPasswordValidation(String code, String email) {

}
