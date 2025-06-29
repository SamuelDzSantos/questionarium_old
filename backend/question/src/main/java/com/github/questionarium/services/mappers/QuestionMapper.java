package com.github.questionarium.services.mappers;

import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.DTOs.QuestionDTO;
import com.github.questionarium.model.Question;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionMapper {

    private final AlternativeMapper alternativeMapper;

    public Question toEntity(QuestionDTO questionDTO) {

        return new Question(null, questionDTO.getMultipleChoice(), questionDTO.getNumberLines(),
                questionDTO.getEducationLevel(), questionDTO.getUserId(), questionDTO.getHeader(),
                questionDTO.getHeaderImage(), null, questionDTO.getEnable(),
                questionDTO.getAccessLevel(),
                null, null, null, null);
    }

    public QuestionDTO toDto(Question question) {
        return new QuestionDTO(question.getId(), question.isMultipleChoice(), question.getNumberLines(),
                question.getEducationLevel(), question.getUserId(), question.getHeader(), question.getHeaderImage(),

                question.getAnswerId(), question.isEnable(), question.getAccessLevel(),

                question.getTags().stream().map((tag) -> {
                    return tag.getId();
                }).toList(),

                question.getAlternatives().stream().map((alternative) -> {
                    return alternativeMapper.toDto(alternative);
                }).toList(), question.getCreationDateTime(), question.getUpdateDateTime());
    }
}
