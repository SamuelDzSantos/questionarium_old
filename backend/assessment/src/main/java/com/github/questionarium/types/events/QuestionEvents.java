package com.github.questionarium.types.events;

public enum QuestionEvents {

    QUESTION_QUEUE_GET_ASWER_KEY("QUESTION_QUEUE_GET_ASWER_KEY"),
    QUESTION_QUEUE_GET("QUESTION_QUEUE_GET"),
    QUESTION_QUEUE_GET_ALTERNATIVE("QUESTION_QUEUE_GET_ALTERNATIVE");

    private QuestionEvents(String event) {
        this.event = event;
    }

    private String event;

    public String getEvent() {
        return this.event;
    }
}

//