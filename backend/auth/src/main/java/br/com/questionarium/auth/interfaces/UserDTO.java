package br.com.questionarium.auth.interfaces;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    // Se seu UserService usa List<String> roles:
    // private List<String> roles;
    // ou se usa um único role (ex: String role):
    // private String role;
}
