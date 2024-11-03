package dev.questionarium.exception;

import java.util.function.Supplier;

public class InternalErrorException extends RuntimeException {

    private final static String baseMessage = "Um erro interno ocorreu no sistema. Visualize as logs de erro!";

    public InternalErrorException() {
        super(baseMessage);
    }

    public final static Supplier<InternalErrorException> getException() {
        return () -> new InternalErrorException();
    }
}
