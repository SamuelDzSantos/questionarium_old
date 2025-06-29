package com.github.questionarium.service.saga.create_user.steps;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;

import com.github.questionarium.model.CreateUserState;
import com.github.questionarium.repositories.CreateUserRepository;
import com.github.questionarium.types.events.AuthEvents;
import com.github.questionarium.types.workflow.BaseWorflowStep;
import com.github.questionarium.types.workflow.WorkflowStepStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class GenerateConfirmationStep extends BaseWorflowStep {

    private final CreateUserRepository createUserRepository;

    public GenerateConfirmationStep(AsyncRabbitTemplate template, CreateUserRepository createUserRepository) {
        super(template);
        this.createUserRepository = createUserRepository;
    }

    @Override
    public Mono<Boolean> process() {

        Long userId = createUserRepository.findById(this.sagaId).get().getUserId();

        log.info("Processando etapa 3. Dados do usuário: Id={}", userId.toString());

        CompletableFuture<String> future = template.convertSendAndReceive(
                AuthEvents.CREATE_CONFIRMATION_TOKEN_EVENT.toString(), Collections.singletonMap("userId", userId));
        return Mono.fromFuture(future)
                .doOnNext(
                        response -> {
                            if (response != null) {
                                saveUrl(response);
                            }
                            this.status = response != null ? WorkflowStepStatus.COMPLETE
                                    : WorkflowStepStatus.FAILED;
                        })
                .map(response -> response != null)
                .doOnError((error) -> {
                    log.error(error.getMessage());
                });

    }

    @Override
    public Mono<Boolean> revert() {
        return Mono.empty();
    }

    private void saveUrl(String url) {
        CreateUserState createUserState = this.createUserRepository.findById(sagaId).get();
        createUserState.setConfirmationUrl(url);
        this.createUserRepository.save(createUserState);
    }

}
