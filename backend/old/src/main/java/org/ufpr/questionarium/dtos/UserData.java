package org.ufpr.questionarium.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserData {

    @NotBlank(message = "Id é obrigatório!")
    @NotNull(message = "Id é obrigatório!")
    private final Long id;

    @NotBlank(message = "Nome de usuário é obrigatório!")
    @NotNull(message = "Nome de usuário é obrigatório!")
    private final String name;

    @NotBlank(message = "Email é obrigatório!")
    @NotNull(message = "Email é obrigatório!")
    private final String email;

}