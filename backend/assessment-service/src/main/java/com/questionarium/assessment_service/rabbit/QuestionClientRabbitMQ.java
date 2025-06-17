package com.questionarium.assessment_service.rabbit;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Primary;

import com.questionarium.assessment_service.client.QuestionClient;
import com.questionarium.assessment_service.config.RabbitMQConfig;
import com.questionarium.assessment_service.security.JwtUtils;

import br.com.questionarium.dtos.RpcAlternativeDTO;
import br.com.questionarium.dtos.RpcAnswerKeyDTO;
import br.com.questionarium.dtos.RpcAnswerKeyRequestDTO;
import br.com.questionarium.dtos.RpcQuestionDTO;

@Primary
@Component
public class QuestionClientRabbitMQ implements QuestionClient {

    private final RabbitTemplate rabbit;
    private final JwtUtils jwtUtils;

    public QuestionClientRabbitMQ(RabbitTemplate rabbit, JwtUtils jwtUtils) {
        this.rabbit = rabbit;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public RpcQuestionDTO getQuestion(Long questionId) {
        return rabbit.convertSendAndReceiveAsType(
                RabbitMQConfig.EXCHANGE,
                "question.get",
                questionId,
                new ParameterizedTypeReference<RpcQuestionDTO>() {
                });
    }

    @Override
    public List<RpcAlternativeDTO> getAlternatives(Long questionId) {
        return rabbit.convertSendAndReceiveAsType(
                RabbitMQConfig.EXCHANGE,
                "question.alternatives",
                questionId,
                new ParameterizedTypeReference<List<RpcAlternativeDTO>>() {
                });
    }

    @Override
    public List<RpcAnswerKeyDTO> getAnswerKeys(List<Long> questionIds) {
        RpcAnswerKeyRequestDTO payload = new RpcAnswerKeyRequestDTO(questionIds, getCurrentUserId());
        return rabbit.convertSendAndReceiveAsType(
                RabbitMQConfig.EXCHANGE,
                "question.answer-key",
                payload,
                new ParameterizedTypeReference<List<RpcAnswerKeyDTO>>() {
                });
    }

    private Long getCurrentUserId() {
        return jwtUtils.getCurrentUserId();
    }
}
