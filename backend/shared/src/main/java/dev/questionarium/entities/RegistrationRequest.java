package dev.questionarium.entities;

import dev.questionarium.interfaces.PasswordEncodable;
import dev.questionarium.types.Role;
import lombok.Builder;

@Builder
public record RegistrationRequest(String name, String email, String password, Role role)
        implements PasswordEncodable<RegistrationRequest> {

    @Override
    public RegistrationRequest getEncodedObject(String password) {
        return new RegistrationRequest(this.name, this.email, password, this.role);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
