package org.ufpr.questionarium.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LoginForm {
    private String email;
    private String password;
}