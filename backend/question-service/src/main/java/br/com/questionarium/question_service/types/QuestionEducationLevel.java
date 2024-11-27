package br.com.questionarium.question_service.types;

public enum QuestionEducationLevel {

    ENSINO_FUNDAMENTAL (0),
    ENSINO_MÉDIO (1),
    ENSINO_SUPERIOR (2);

    private final int value;

    QuestionEducationLevel(int value) {
            this.value = value;
        }

    public int getValue() {
        return value;
    }

    public static QuestionEducationLevel fromInt(int i) {
        for (QuestionEducationLevel level : QuestionEducationLevel.values()) {
            if (level.getValue() == i) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + i);
    }
}