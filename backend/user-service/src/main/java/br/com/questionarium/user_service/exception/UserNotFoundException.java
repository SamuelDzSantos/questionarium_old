package br.com.questionarium.user_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("Usuário não encontrado com id: " + userId);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
