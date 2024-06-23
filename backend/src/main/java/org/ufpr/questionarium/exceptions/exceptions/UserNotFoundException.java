package org.ufpr.questionarium.exceptions.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {

    private final String message;

    public UserNotFoundException(String user) {
        super(String.format("Usuário de email %s não encontrado!", user));
        String msg = String.format("Usuário de email %s não encontrado!", user);
        this.message = msg;
    }

    public UserNotFoundException(Long id) {
        super(String.format("Usuário de id %s não encontrado!", Long.toString(id)));
        String msg = String.format("Usuário de id %s não encontrado!", Long.toString(id));
        this.message = msg;
    }

}
