package com.questionarium.assessment_service.client;

import java.util.List;

import com.questionarium.assessment_service.dto.AlternativeDto;
import com.questionarium.assessment_service.dto.AnswerKeyDTO;
import com.questionarium.assessment_service.dto.QuestionDto;

public interface QuestionClient {
    QuestionDto getQuestion(Long questionId);

    List<AlternativeDto> getAlternatives(Long questionId);

    List<AnswerKeyDTO> getAnswerKeys(List<Long> questionIds);
}
