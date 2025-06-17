package com.questionarium.assessment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import br.com.questionarium.dtos.RpcQuestionDTO;
import br.com.questionarium.dtos.RpcAlternativeDTO;
import br.com.questionarium.dtos.RpcAnswerKeyDTO;

@FeignClient(name = "question-service", url = "${question.service.url}")
public interface QuestionClient {

    @GetMapping("/questions/{id}")
    RpcQuestionDTO getQuestion(@PathVariable("id") Long questionId);

    @GetMapping("/questions/{id}/alternatives")
    List<RpcAlternativeDTO> getAlternatives(@PathVariable("id") Long questionId);

    @PostMapping("/questions/answer-keys")
    List<RpcAnswerKeyDTO> getAnswerKeys(@RequestBody List<Long> questionIds);
}
