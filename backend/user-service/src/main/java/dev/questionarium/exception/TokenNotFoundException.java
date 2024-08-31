package dev.questionarium.exception;

import java.util.function.Supplier;

public class TokenNotFoundException extends RuntimeException {

    private final static String baseMessage = "Token %s não encontrado!";

    public TokenNotFoundException(String token) {
        super(String.format(baseMessage, token));
    }

    public final static Supplier<TokenNotFoundException> getException(String token) {
        return () -> new TokenNotFoundException(token);
    }
}
