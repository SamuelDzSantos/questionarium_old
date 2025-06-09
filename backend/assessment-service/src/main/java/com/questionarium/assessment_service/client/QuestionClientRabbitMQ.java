package com.questionarium.assessment_service.client;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.questionarium.assessment_service.dto.AlternativeDto;
import com.questionarium.assessment_service.dto.AnswerKeyDTO;
import com.questionarium.assessment_service.dto.QuestionDto;
import com.questionarium.assessment_service.rabbit.RabbitMQConfig;

@Component
public class QuestionClientRabbitMQ implements QuestionClient {

    private final RabbitTemplate rabbit;

    // @Autowired
    public QuestionClientRabbitMQ(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @Override
    public QuestionDto getQuestion(Long questionId) {
        return rabbit.convertSendAndReceiveAsType(
                RabbitMQConfig.EXCHANGE,
                "question.get", // certifique-se que o question-service escuta essa routing-key
                questionId,
                new ParameterizedTypeReference<QuestionDto>() {
                });
    }

    @Override
    public List<AlternativeDto> getAlternatives(Long questionId) {
        return rabbit.convertSendAndReceiveAsType(
                RabbitMQConfig.EXCHANGE,
                "question.get-alternatives", // certifique-se que o question-service escuta essa routing-key
                questionId,
                new ParameterizedTypeReference<List<AlternativeDto>>() {
                });
    }

    @Override
    public List<AnswerKeyDTO> getAnswerKeys(List<Long> questionIds) {
        return rabbit.convertSendAndReceiveAsType(
                RabbitMQConfig.EXCHANGE,
                "question.answer-key",
                questionIds,
                new ParameterizedTypeReference<List<AnswerKeyDTO>>() {
                });
    }
}
