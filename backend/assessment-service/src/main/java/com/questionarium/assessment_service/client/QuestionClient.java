package com.questionarium.assessment_service.client;

import java.util.List;

import com.questionarium.assessment_service.dto.AlternativeDTO;
import com.questionarium.assessment_service.dto.AnswerKeyDTO;
import com.questionarium.assessment_service.dto.QuestionDTO;

public interface QuestionClient {
    QuestionDTO getQuestion(Long questionId);

    List<AlternativeDTO> getAlternatives(Long questionId);

    List<AnswerKeyDTO> getAnswerKeys(List<Long> questionIds);
}
