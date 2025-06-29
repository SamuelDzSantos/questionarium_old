package com.github.questionarium.services.rabbit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.AlternativeDTO;
import com.github.questionarium.interfaces.DTOs.AnswerKeyDTO;
import com.github.questionarium.interfaces.DTOs.QuestionDTO;
import com.github.questionarium.interfaces.DTOs.RpcAlternativeDTO;
import com.github.questionarium.interfaces.DTOs.RpcAnswerKeyDTO;
import com.github.questionarium.interfaces.DTOs.RpcAnswerKeyRequestDTO;
import com.github.questionarium.interfaces.DTOs.RpcQuestionDTO;
import com.github.questionarium.services.QuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Consumer {

    private final QuestionService questionService;

    private final String QUESTION_QUEUE_GET_ASWER_KEY = "QUESTION_QUEUE_GET_ASWER_KEY";
    private final String QUESTION_QUEUE_GET = "QUESTION_QUEUE_GET";
    private final String QUESTION_QUEUE_GET_ALTERNATIVE = "QUESTION_QUEUE_GET_ALTERNATIVE";

    @RabbitListener(queues = QUESTION_QUEUE_GET_ASWER_KEY)
    public List<RpcAnswerKeyDTO> getAswerKey(RpcAnswerKeyRequestDTO req) {

        log.info("Obtendo respostas para questões {}", req.getQuestionIds());

        try {

            List<AnswerKeyDTO> list = questionService.getAnswerKeys(req.getQuestionIds(), req.getUserId());
            return list.stream()
                    .map(dto -> {
                        RpcAnswerKeyDTO rpc = new RpcAnswerKeyDTO();
                        rpc.setQuestionId(dto.getQuestionId());
                        rpc.setAnswerKey(dto.getAnswerKey().toString());
                        return rpc;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }

    }

    @RabbitListener(queues = QUESTION_QUEUE_GET)
    public RpcQuestionDTO getQuestion(Map<String, Long> questionId) {

        try {

            log.info("Obtendo dados da questão {}", questionId);
            QuestionDTO dto = questionService.getQuestionAsDto(questionId.get("questionId"));
            RpcQuestionDTO rpc = new RpcQuestionDTO();
            rpc.setUserId(dto.getUserId());
            rpc.setQuestion(dto.getId());
            rpc.setMultipleChoice(dto.getMultipleChoice());
            rpc.setNumberLines(dto.getNumberLines());
            rpc.setEducationLevel(dto.getEducationLevel().toString());
            rpc.setHeader(dto.getHeader());
            rpc.setHeaderImage(dto.getHeaderImage());
            rpc.setAnswerId(dto.getAnswerId());
            rpc.setEnable(dto.getEnable());
            rpc.setAccessLevel(dto.getAccessLevel().toString());
            rpc.setTags(dto.getTagIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList()));
            rpc.setAlternatives(dto.getAlternatives().stream()
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
        } catch (Exception e) {
            return null;
        }
    }

    @RabbitListener(queues = QUESTION_QUEUE_GET_ALTERNATIVE)
    public List<RpcAlternativeDTO> getAlternatives(Map<String, Long> questionId) {

        try {

            log.info("Obtendo dados da questão {}", questionId);
            List<AlternativeDTO> list = questionService.getAlternativesAsDto(questionId.get("questionId"));
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
        } catch (Exception e) {
            return null;
        }
    }
}