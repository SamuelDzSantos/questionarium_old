package com.github.questionarium.service.saga.patch_user.steps;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;

import com.github.questionarium.repositories.PatchUserRepository;
import com.github.questionarium.types.events.UserEvents;
import com.github.questionarium.types.workflow.BaseWorflowStep;
import com.github.questionarium.types.workflow.WorkflowStepStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class UpdateUserNameStep extends BaseWorflowStep {

    private final PatchUserRepository patchUserRepository;

    public UpdateUserNameStep(AsyncRabbitTemplate template, PatchUserRepository patchUserRepository) {
        super(template);
        this.patchUserRepository = patchUserRepository;
    }

    @Override
    public Mono<Boolean> process() {

        log.info("Processando etapa 1. Payload: {}", payload.toString());
        CompletableFuture<Boolean> future = template.convertSendAndReceive(
                UserEvents.UPDATE_USER_NAME_EVENT.toString(), payload);
        return Mono.fromFuture(future)
                .doOnNext(response -> {
                })
                .map(response -> {
                    return response;
                })
                .doOnNext(response -> this.status = response ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED)
                .doOnError((error) -> {
                    log.error(error.getMessage());
                });
    }

    @Override
    public Mono<Boolean> revert() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'revert'");
    }

}
