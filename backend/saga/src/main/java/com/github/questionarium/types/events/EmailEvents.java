package com.github.questionarium.types.events;

public enum EmailEvents {

    SEND_EMAIL_EVENT("SEND_EMAIL_EVENT");

    private EmailEvents(String event) {
        this.event = event;
    }

    private String event;

    public String getEvent() {
        return this.event;
    }
}
