package com.github.questionarium.types.events;

public enum AuthEvents {

    CREATE_AUTH_USER_EVENT("CREATE_AUTH_USER_EVENT"),
    CREATE_CONFIRMATION_TOKEN_EVENT("CREATE_CONFIRMATION_TOKEN_EVENT"),
    REVERT_CREATE_AUTH_USER_EVENT("REVERT_CREATE_AUTH_USER_EVENT"),
    UPDATE_AUTH_USER_EMAIL_EVENT("UPDATE_AUTH_USER_EMAIL_EVENT");

    private AuthEvents(String event) {
        this.event = event;
    }

    private String event;

    public String getEvent() {
        return this.event;
    }
}
