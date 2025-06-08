package br.com.questionarium.question_service.dto;

import java.util.List;

public class AnswerKeyRequestDTO {
    private Long userId;
    private List<Long> questionIds;

    public AnswerKeyRequestDTO() {
    }

    public AnswerKeyRequestDTO(Long userId, List<Long> questionIds) {
        this.userId = userId;
        this.questionIds = questionIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }

    @Override
    public String toString() {
        return "AnswerKeyRequestDTO{userId=%d, questionIds=%s}".formatted(userId, questionIds);
    }
}