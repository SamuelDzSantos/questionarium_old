package com.github.questionarium.service.rabbit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.github.questionarium.client.QuestionClient;
import com.github.questionarium.interfaces.DTOs.RpcAlternativeDTO;
import com.github.questionarium.interfaces.DTOs.RpcAnswerKeyDTO;
import com.github.questionarium.interfaces.DTOs.RpcAnswerKeyRequestDTO;
import com.github.questionarium.interfaces.DTOs.RpcQuestionDTO;
import com.github.questionarium.types.events.QuestionEvents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionClientRabbitMQ implements QuestionClient {

    private final RabbitTemplate rabbit;

    @Override
    public RpcQuestionDTO getQuestion(Long questionId) {
        log.info("Obtendo dados para question {}", questionId);

        Map<String, Long> questionMap = new HashMap<>();
        questionMap.put("questionId", questionId);

        var a = rabbit.convertSendAndReceiveAsType(
                // RabbitMQConfig.EXCHANGE,
                QuestionEvents.QUESTION_QUEUE_GET.toString(),
                questionMap,
                new ParameterizedTypeReference<RpcQuestionDTO>() {
                });
        System.out.println(a);
        return a;
    }

    @Override
    public List<RpcAlternativeDTO> getAlternatives(Long questionId) {
        Map<String, Long> questionMap = new HashMap<>();
        questionMap.put("questionId", questionId);
        return rabbit.convertSendAndReceiveAsType(
                // RabbitMQConfig.EXCHANGE,
                QuestionEvents.QUESTION_QUEUE_GET_ALTERNATIVE.toString(),
                questionMap,
                new ParameterizedTypeReference<List<RpcAlternativeDTO>>() {
                });
    }

    @Override
    public List<RpcAnswerKeyDTO> getAnswerKeys(List<Long> questionIds, Long loggedUserId) {
        RpcAnswerKeyRequestDTO payload = new RpcAnswerKeyRequestDTO(questionIds, loggedUserId);
        return rabbit.convertSendAndReceiveAsType(
                // RabbitMQConfig.EXCHANGE,
                QuestionEvents.QUESTION_QUEUE_GET_ASWER_KEY.toString(),
                payload,
                new ParameterizedTypeReference<List<RpcAnswerKeyDTO>>() {
                });
    }

}
