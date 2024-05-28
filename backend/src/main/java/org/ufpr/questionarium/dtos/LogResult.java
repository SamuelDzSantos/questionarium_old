package org.ufpr.questionarium.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogResult {
    public String username;
    public String email;
    public String token;
}
