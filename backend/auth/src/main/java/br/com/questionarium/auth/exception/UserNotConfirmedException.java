package br.com.questionarium.auth.exception;

import org.springframework.http.HttpStatus;

public class UserNotConfirmedException extends ApiErrorException {

    public UserNotConfirmedException() {
        super(HttpStatus.UNAUTHORIZED, "Usuário não confirmado.");
    }
}