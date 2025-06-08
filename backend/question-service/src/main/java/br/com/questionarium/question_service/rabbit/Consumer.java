package br.com.questionarium.question_service.rabbit;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.dto.AnswerKeyRequestDTO;
import br.com.questionarium.question_service.service.QuestionService;

@Service
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final QuestionService questionService;
    private final ObjectMapper objectMapper;

    public Consumer(QuestionService questionService, ObjectMapper objectMapper) {
        this.questionService = questionService;
        this.objectMapper = objectMapper;
    }

    /**
     * Consome mensagens da fila “question-queue”. O parâmetro @Header captura
     * a routing key usada (ex.: “question.answer-key”).
     *
     * Retorna uma List<AnswerKeyDTO> que será enviada automaticamente como reply
     * ao AsyncRabbitTemplate que publicou a mensagem (convertSendAndReceive).
     */
    @RabbitListener(queues = "question-queue")
    public List<AnswerKeyDTO> handleRequests(
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key,
            Message message) {

        logger.info("Mensagem recebida na fila 'question-queue' com routing key '{}'", key);
        try {
            if ("question.answer-key".equals(key)) {
                // 1) Converte o body (byte[]) para String JSON
                String body = new String(message.getBody(), StandardCharsets.UTF_8);
                logger.debug("Corpo da mensagem (JSON): {}", body);

                // 2) Desserializa como AnswerKeyRequestDTO
                AnswerKeyRequestDTO request = objectMapper.readValue(body, AnswerKeyRequestDTO.class);
                logger.info("Request recebido: {}", request);

                // 3) Pega as chaves de resposta
                List<AnswerKeyDTO> answerKeys = questionService.getAnswerKeys(request.getQuestionIds(),
                        request.getUserId());
                logger.info("Answer keys geradas: {}", answerKeys);
                return answerKeys;
            } else {
                logger.warn("Routing key não esperada: '{}'. Mensagem será descartada", key);
                return Collections.emptyList();
            }
        } catch (JsonProcessingException e) {
            logger.error("Falha ao desserializar corpo da mensagem", e);
            throw new AmqpRejectAndDontRequeueException("Erro de parsing JSON", e);
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem com routing key '{}'", key, e);
            throw new AmqpRejectAndDontRequeueException("Erro ao processar mensagem", e);
        }
    }
}
