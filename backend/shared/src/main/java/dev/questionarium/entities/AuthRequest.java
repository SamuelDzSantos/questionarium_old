package dev.questionarium.entities;

import dev.questionarium.interfaces.PasswordEncodable;

public record AuthRequest(
                String login,
                String password) implements PasswordEncodable<AuthRequest> {

        @Override
        public AuthRequest getEncodedObject(String password) {
                return new AuthRequest(this.login, password);
        }

        @Override
        public String getPassword() {
                return this.password;
        }

}
