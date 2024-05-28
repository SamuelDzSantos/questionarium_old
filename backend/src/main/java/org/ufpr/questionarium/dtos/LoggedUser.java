package org.ufpr.questionarium.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoggedUser {
    private String username;
    private String email;
}
