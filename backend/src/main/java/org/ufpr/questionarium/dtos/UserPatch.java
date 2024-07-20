package org.ufpr.questionarium.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPatch {

    @NotBlank
    private final String name;

    @NotBlank
    private final String email;

    @NotBlank
    private final String password;

    @NotBlank
    private final String newPassword;

    @NotBlank
    private final String confirmPassword;
}
