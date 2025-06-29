package com.github.questionarium.client;

import java.util.List;

import com.github.questionarium.interfaces.DTOs.RpcAlternativeDTO;
import com.github.questionarium.interfaces.DTOs.RpcAnswerKeyDTO;
import com.github.questionarium.interfaces.DTOs.RpcQuestionDTO;

public interface QuestionClient {
    RpcQuestionDTO getQuestion(Long questionId);

    List<RpcAlternativeDTO> getAlternatives(Long questionId);

    List<RpcAnswerKeyDTO> getAnswerKeys(List<Long> questionIds, Long loggedUserId);

}
