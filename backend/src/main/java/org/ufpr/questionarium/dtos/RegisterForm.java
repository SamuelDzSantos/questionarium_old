package org.ufpr.questionarium.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterForm {
    private final String name;
    private final String email;
    private final String password;
}
