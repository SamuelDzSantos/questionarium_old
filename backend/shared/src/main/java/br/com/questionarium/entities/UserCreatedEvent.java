package br.com.questionarium.entities;

import br.com.questionarium.types.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreatedEvent {

    private Long userId;
    private String email;
    private String rawPassword; // NOVO campo
    private Role role; // NOVO campo
}
