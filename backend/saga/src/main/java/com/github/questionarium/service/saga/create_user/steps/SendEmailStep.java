package com.github.questionarium.service.saga.create_user.steps;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;

import com.github.questionarium.interfaces.email.Email;
import com.github.questionarium.interfaces.user.RegisterForm;
import com.github.questionarium.model.CreateUserState;
import com.github.questionarium.repositories.CreateUserRepository;
import com.github.questionarium.types.events.EmailEvents;
import com.github.questionarium.types.workflow.BaseWorflowStep;
import com.github.questionarium.types.workflow.WorkflowStepStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public class SendEmailStep extends BaseWorflowStep {

    private final CreateUserRepository createUserRepository;

    public SendEmailStep(AsyncRabbitTemplate template, CreateUserRepository createUserRepository) {

        super(template);
        this.createUserRepository = createUserRepository;
    }

    @Override
    public Mono<Boolean> process() {

        CreateUserState createUserState = this.createUserRepository.findById(sagaId).get();

        RegisterForm registerForm = (RegisterForm) this.payload;

        log.info("Processando etapa 4. Dados do usuário: Nome={}", registerForm.name());

        String message = "Olá " + registerForm.name() + ", clique: " + createUserState.getConfirmationUrl();
        Email email = new Email("Confirme seu cadastro", message, registerForm.email());

        CompletableFuture<Boolean> future = template.convertSendAndReceive(
                EmailEvents.SEND_EMAIL_EVENT.toString(), email);
        return Mono.fromFuture(future)
                .doOnNext(
                        response -> this.status = response ? WorkflowStepStatus.COMPLETE
                                : WorkflowStepStatus.FAILED)
                .doOnError((error) -> {
                    log.error(error.getMessage());
                });

    }

    @Override
    public Mono<Boolean> revert() {
        return Mono.empty();
    }

}
