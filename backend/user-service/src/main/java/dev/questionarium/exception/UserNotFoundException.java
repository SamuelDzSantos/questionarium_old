package dev.questionarium.exception;

import java.util.function.Supplier;

public class UserNotFoundException extends RuntimeException {

    private final static String baseEmailMessage = "Usuário de email : '%s' não encontrado!";
    private final static String baseIdMessage = "Usuário de id : '%s' não encontrado!";

    public UserNotFoundException(String userEmail) {
        super(String.format(baseEmailMessage, userEmail));
    }

    public UserNotFoundException(Long userId) {
        super(String.format(baseIdMessage, String.valueOf(userId)));
    }

    public final static Supplier<UserNotFoundException> getException(Long userId) {
        return () -> new UserNotFoundException(userId);
    }

    public final static Supplier<UserNotFoundException> getException(String email) {
        return () -> new UserNotFoundException(email);
    }

}
