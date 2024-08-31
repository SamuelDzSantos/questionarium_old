package dev.questionarium.exception;

import java.util.function.Supplier;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public final static Supplier<BadRequestException> getException(String message) {
        return () -> new BadRequestException(message);
    }
}
