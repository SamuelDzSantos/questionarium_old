package br.com.questionarium.auth.interfaces;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
}
