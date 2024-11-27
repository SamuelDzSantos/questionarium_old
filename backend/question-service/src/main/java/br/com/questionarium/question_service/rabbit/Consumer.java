package br.com.questionarium.question_service.rabbit;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.service.QuestionService;

@Service
public class Consumer {

    @Autowired
    private QuestionService questionService;

    @RabbitListener(queues = "question-queue")
    public List<AnswerKeyDTO> handleRequests(@Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key, Message message)
            throws AmqpRejectAndDontRequeueException, JsonMappingException, JsonProcessingException {

        switch (key) {
            case "question.answer-key":
                String body = new String(message.getBody(), StandardCharsets.UTF_8);

                ObjectMapper objectMapper = new ObjectMapper();
                List<Long> ids = objectMapper.readValue(body, new TypeReference<List<Long>>() {
                });

                return questionService.getAnswerKeys(ids);

            default:
                return null;
        }

    }

}
