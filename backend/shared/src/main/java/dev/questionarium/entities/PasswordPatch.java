package dev.questionarium.entities;

import dev.questionarium.interfaces.PasswordEncodable;

public record PasswordPatch(String password, String token, String code) implements PasswordEncodable<PasswordPatch> {

    @Override
    public PasswordPatch getEncodedObject(String password) {
        return new PasswordPatch(password, this.token, this.code);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
