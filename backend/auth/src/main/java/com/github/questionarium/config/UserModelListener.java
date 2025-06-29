package com.github.questionarium.config;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.github.questionarium.model.User;
import com.github.questionarium.service.SequenceGeneratorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserModelListener extends AbstractMongoEventListener<User> {

    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public void onBeforeConvert(@NonNull BeforeConvertEvent<User> event) {
        if (event.getSource().getId() == null || event.getSource().getId().intValue() < 1) {
            event.getSource().setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
            log.info("Sequence atualizada. Novo valor {}", event.getSource().getId());
        }
    }
}
