package dev.questionarium.entities;

import java.util.List;

import lombok.Builder;

@Builder
public record UserData(Long id,
                String name,
                String email,
                List<String> roles) {
}
