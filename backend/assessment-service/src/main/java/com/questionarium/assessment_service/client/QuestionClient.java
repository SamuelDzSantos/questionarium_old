package com.questionarium.assessment_service.client;

import java.util.List;

import br.com.questionarium.dtos.RpcAlternativeDTO;
import br.com.questionarium.dtos.RpcAnswerKeyDTO;
import br.com.questionarium.dtos.RpcQuestionDTO;

public interface QuestionClient {
    RpcQuestionDTO getQuestion(Long questionId);

    List<RpcAlternativeDTO> getAlternatives(Long questionId);

    List<RpcAnswerKeyDTO> getAnswerKeys(List<Long> questionIds);

}
