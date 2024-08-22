package dev.questionarium.entities;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserData userData) {

}
