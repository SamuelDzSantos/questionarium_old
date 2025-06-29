package com.github.questionarium.service.saga.create_user.steps;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.stereotype.Service;

import com.github.questionarium.model.CreateUserState;
import com.github.questionarium.repositories.CreateUserRepository;
import com.github.questionarium.types.events.AuthEvents;
import com.github.questionarium.types.workflow.BaseWorflowStep;
import com.github.questionarium.types.workflow.WorkflowStepStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CreateAuthUserStep extends BaseWorflowStep {

    private final CreateUserRepository createUserRepository;

    public CreateAuthUserStep(AsyncRabbitTemplate template, CreateUserRepository createUserRepository) {
        super(template);
        this.createUserRepository = createUserRepository;
    }

    @Override
    public Mono<Boolean> process() {
        log.info("Processando etapa 1. Payload: {}", payload.toString());
        CompletableFuture<Long> future = template.convertSendAndReceive(
                AuthEvents.CREATE_AUTH_USER_EVENT.toString(), payload);
        return Mono.fromFuture(future)
                .doOnNext(response -> {
                    this.createUserRepository.save(new CreateUserState(this.sagaId, response, null));
                })
                .map(response -> {
                    return response <= 0 ? false : true;
                })
                .doOnNext(response -> this.status = response ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED)
                .doOnError((error) -> {
                    log.error(error.getMessage());
                });
    }

    @Override
    public Mono<Boolean> revert() {

        Long userId = createUserRepository.findById(sagaId).get().getUserId();
        log.info("Revertendo criação de usuário com id : {} ", userId.toString());
        CompletableFuture<String> future = template.convertSendAndReceive(
                AuthEvents.CREATE_AUTH_USER_EVENT.toString(), userId);
        return Mono.fromFuture(future).map(response -> true);
    }

}
