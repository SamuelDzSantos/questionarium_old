package br.com.questionarium.question_service.rabbit;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import br.com.questionarium.dtos.RpcAnswerKeyDTO;
import br.com.questionarium.dtos.RpcAnswerKeyRequestDTO;
import br.com.questionarium.dtos.RpcAlternativeDTO;
import br.com.questionarium.dtos.RpcQuestionDTO;
import br.com.questionarium.question_service.dto.AnswerKeyDTO;
import br.com.questionarium.question_service.dto.AlternativeDTO;
import br.com.questionarium.question_service.dto.QuestionDTO;
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

    @RabbitListener(queues = "question-queue")
    public Object handleRequests(
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String key,
            Message message) {

        logger.info("Mensagem recebida na fila 'question-queue' com routing key '{}'", key);
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            logger.debug("Corpo da mensagem (JSON): {}", body);

            if ("question.answer-key".equals(key)) {
                RpcAnswerKeyRequestDTO req = objectMapper.readValue(body, RpcAnswerKeyRequestDTO.class);
                List<AnswerKeyDTO> list = questionService.getAnswerKeys(req.getQuestionIds(), req.getUserId());
                return list.stream()
                        .map(dto -> {
                            RpcAnswerKeyDTO rpc = new RpcAnswerKeyDTO();
                            rpc.setQuestionId(dto.getQuestionId());
                            rpc.setAnswerKey(dto.getAnswerKey().toString());
                            return rpc;
                        })
                        .collect(Collectors.toList());

            } else if ("question.get".equals(key)) {
                Long questionId = Long.valueOf(body);
                QuestionDTO dto = questionService.getQuestionAsDto(questionId);
                RpcQuestionDTO rpc = new RpcQuestionDTO();
                rpc.setQuestion(dto.getId());
                rpc.setMultipleChoice(dto.getMultipleChoice());
                rpc.setNumberLines(dto.getNumberLines());
                rpc.setEducationLevel(dto.getEducationLevel().toString());
                rpc.setHeader(dto.getHeader());
                rpc.setHeaderImage(dto.getHeaderImage());
                rpc.setAnswerId(dto.getAnswerId());
                rpc.setEnable(dto.getEnable());
                rpc.setAccessLevel(dto.getAccessLevel().toString());
                // converter Long IDs em String para o setter correto
                rpc.setTags(dto.getTagIds().stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList()));
                rpc.setAlternatives(dto.getAlternatives()
                        .stream()
                        .map(a -> {
                            RpcAlternativeDTO alt = new RpcAlternativeDTO();
                            alt.setAlternative(a.getId());
                            alt.setDescription(a.getDescription());
                            alt.setImagePath(a.getImagePath());
                            alt.setIsCorrect(a.getIsCorrect());
                            alt.setExplanation(a.getExplanation());
                            alt.setPosition(a.getAlternativeOrder());
                            return alt;
                        })
                        .collect(Collectors.toList()));
                return rpc;

            } else if ("question.alternatives".equals(key)) {
                Long questionId = Long.valueOf(body);
                List<AlternativeDTO> list = questionService.getAlternativesAsDto(questionId);
                return list.stream()
                        .map(a -> {
                            RpcAlternativeDTO rpcAlt = new RpcAlternativeDTO();
                            rpcAlt.setAlternative(a.getId());
                            rpcAlt.setDescription(a.getDescription());
                            rpcAlt.setImagePath(a.getImagePath());
                            rpcAlt.setIsCorrect(a.getIsCorrect());
                            rpcAlt.setExplanation(a.getExplanation());
                            rpcAlt.setPosition(a.getAlternativeOrder());
                            return rpcAlt;
                        })
                        .collect(Collectors.toList());

            } else {
                logger.warn("Routing key inesperada: '{}'. Descarta mensagem.", key);
                return Collections.emptyList();
            }

        } catch (JsonProcessingException e) {
            logger.error("Falha ao desserializar JSON", e);
            throw new AmqpRejectAndDontRequeueException("Erro de parsing JSON", e);
        } catch (Exception e) {
            logger.error("Erro ao processar mensagem com routing key '{}'", key, e);
            throw new AmqpRejectAndDontRequeueException("Erro ao processar mensagem", e);
        }
    }
}