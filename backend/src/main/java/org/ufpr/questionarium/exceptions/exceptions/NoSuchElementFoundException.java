package org.ufpr.questionarium.exceptions.exceptions;

import lombok.Getter;

@Getter
public class NoSuchElementFoundException extends RuntimeException {

    private String message;

    public NoSuchElementFoundException(String msg) {
        super(msg);
        this.message = msg;
    }
}
