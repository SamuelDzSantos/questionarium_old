package com.github.questionarium.service.saga.create_user.steps;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;

import com.github.questionarium.interfaces.user.UserRegisterData;
import com.github.questionarium.repositories.CreateUserRepository;
import com.github.questionarium.types.events.UserEvents;
import com.github.questionarium.types.workflow.BaseWorflowStep;
import com.github.questionarium.types.workflow.WorkflowStepStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class CreateUserStep extends BaseWorflowStep {

    private final CreateUserRepository createUserRepository;

    public CreateUserStep(AsyncRabbitTemplate template, CreateUserRepository createUserRepository) {

        super(template);
        this.createUserRepository = createUserRepository;
    }

    @Override
    public Mono<Boolean> process() {
        Long userId = createUserRepository.findById(this.sagaId).get().getUserId();

        String name = (String) payload;

        log.info("Processando etapa 2. Dados do usuário: Nome={},Id={}", name, userId.toString());

        CompletableFuture<Boolean> future = template.convertSendAndReceive(
                UserEvents.CREATE_USER_EVENT.toString(), new UserRegisterData(userId, name));
        return Mono.fromFuture(future)
                .doOnNext(response -> this.status = response ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED)
                .doOnError((error) -> {
                    log.error(error.getMessage());
                });

    }

    @Override
    public Mono<Boolean> revert() {
        return Mono.just(null);
    }

}
