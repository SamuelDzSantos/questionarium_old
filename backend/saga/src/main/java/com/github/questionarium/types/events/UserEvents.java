package com.github.questionarium.types.events;

public enum UserEvents {

    CREATE_USER_EVENT("CREATE_USER_EVENT");

    private UserEvents(String event) {
        this.event = event;
    }

    private String event;

    public String getEvent() {
        return this.event;
    }
}
