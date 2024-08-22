package dev.questionarium.types;

public enum Role {

    USER("USER"),
    ADMIN("ADMIN");

    private String role;

    private Role(String role) {
        this.role = role;
    }

    public String getAsString() {
        return this.role;
    }
}
