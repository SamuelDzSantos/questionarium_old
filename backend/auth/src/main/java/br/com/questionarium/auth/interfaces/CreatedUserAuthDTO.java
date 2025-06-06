package br.com.questionarium.auth.interfaces;

import br.com.questionarium.types.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedUserAuthDTO {

    @NotBlank
    @Email
    private String login;

    @NotBlank
    private String password;

    @NotNull
    private Role role;
}
