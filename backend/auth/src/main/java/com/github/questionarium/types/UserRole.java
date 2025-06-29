package com.github.questionarium.types;

public enum UserRole {

    USER("USER"),
    ADMIN("ADMIN");

    private UserRole(String role) {
        this.role = role;
    }

    private String role;

    public String getRole() {
        return this.role;
    }
}
