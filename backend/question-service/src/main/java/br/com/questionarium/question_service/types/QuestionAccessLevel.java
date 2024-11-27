package br.com.questionarium.question_service.types;


public enum QuestionAccessLevel {
    PRIVATE(0),
    PUBLIC(1);

    private final int value;

    QuestionAccessLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static QuestionAccessLevel fromInt(int i) {
        for (QuestionAccessLevel level : QuestionAccessLevel.values()) {
            if (level.getValue() == i) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + i);
    }
}
