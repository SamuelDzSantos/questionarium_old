package org.ufpr.questionarium.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterForm {

    @NotBlank(message = "Nome é obrigatório!")
    @NotNull(message = "Nome é obrigatório!")
    private final String name;

    @NotBlank(message = "Email é obrigatório!")
    @NotNull(message = "Email é obrigatório!")
    private final String email;

    @NotBlank(message = "Senha é obrigatória!")
    @NotNull(message = "Senha é obrigatória!")
    private final String password;
}