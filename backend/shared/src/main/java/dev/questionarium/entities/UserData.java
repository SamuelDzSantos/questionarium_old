package dev.questionarium.entities;

import java.util.List;

public record UserData(Long id,
        String name,
        String password,
        String email,
        List<String> roles) {
}
