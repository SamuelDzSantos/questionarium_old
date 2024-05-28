package org.ufpr.questionarium.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatedUserForm {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
}
