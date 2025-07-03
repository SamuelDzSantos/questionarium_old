package com.github.questionarium.service.saga.patch_user.steps;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.stereotype.Service;

import com.github.questionarium.model.PatchUserState;
import com.github.questionarium.repositories.PatchUserRepository;
import com.github.questionarium.types.events.AuthEvents;
import com.github.questionarium.types.workflow.BaseWorflowStep;
import com.github.questionarium.types.workflow.WorkflowStepStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UpdateAuthEmailStep extends BaseWorflowStep {

    private final PatchUserRepository patchUserRepository;

    public UpdateAuthEmailStep(AsyncRabbitTemplate template, PatchUserRepository patchUserRepository) {
        super(template);
        this.patchUserRepository = patchUserRepository;
    }

    @Override
    public Mono<Boolean> process() {
        log.info("Processando etapa 1. Payload: {}", payload.toString());
        CompletableFuture<Long> future = template.convertSendAndReceive(
                AuthEvents.UPDATE_AUTH_USER_EMAIL_EVENT.toString(), payload);
        return Mono.fromFuture(future)
                .doOnNext(response -> {
                    this.patchUserRepository.save(new PatchUserState(this.sagaId, response));
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
        return null;
    }

}
