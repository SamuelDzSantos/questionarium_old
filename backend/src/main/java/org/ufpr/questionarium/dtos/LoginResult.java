package org.ufpr.questionarium.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginResult {

    @NotNull(message = "Usuário é obrigatório!")
    private final UserData user;

    @NotBlank(message = "Token é obrigatório!")
    @NotNull(message = "Token é obrigatório!")
    public final String token;
}