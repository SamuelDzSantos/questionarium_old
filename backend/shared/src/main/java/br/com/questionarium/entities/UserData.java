package br.com.questionarium.entities;

import java.util.List;

public record UserData(Long id,
        String name,
        String email,
        List<String> roles) {
}
