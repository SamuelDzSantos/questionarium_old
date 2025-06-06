package br.com.questionarium.entities;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserData userData) {

}
