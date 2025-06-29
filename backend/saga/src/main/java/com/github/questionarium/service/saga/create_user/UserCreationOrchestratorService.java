package com.github.questionarium.service.saga.create_user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.saga.Workflow;
import com.github.questionarium.interfaces.saga.WorkflowStep;
import com.github.questionarium.interfaces.user.RegisterForm;
import com.github.questionarium.model.CreateUserState;
import com.github.questionarium.repositories.CreateUserRepository;
import com.github.questionarium.service.saga.create_user.steps.CreateAuthUserStep;
import com.github.questionarium.service.saga.create_user.steps.CreateUserStep;
import com.github.questionarium.service.saga.create_user.steps.GenerateConfirmationStep;
import com.github.questionarium.service.saga.create_user.steps.SendEmailStep;
import com.github.questionarium.types.workflow.BaseSagaOrchestator;
import com.github.questionarium.types.workflow.BaseWorflowStep;
import com.github.questionarium.types.workflow.DefaultWorkflow;

@Service
public class UserCreationOrchestratorService extends BaseSagaOrchestator {

    private RegisterForm registerForm;
    private final CreateUserRepository createUserRepository;

    public UserCreationOrchestratorService(AsyncRabbitTemplate template, CreateUserRepository createUserRepository) {
        super(template);
        this.createUserRepository = createUserRepository;
    }

    protected Workflow getWorkflow() {

        Long sagaId = createUserRepository.save(new CreateUserState()).getSaga_id();

        BaseWorflowStep step1 = new CreateAuthUserStep(this.template, createUserRepository);
        step1.setPayload(registerForm);

        BaseWorflowStep step2 = new CreateUserStep(template, createUserRepository);
        step2.setPayload(registerForm.name());

        BaseWorflowStep step3 = new GenerateConfirmationStep(template, createUserRepository);
        step3.setPayload(registerForm);
        BaseWorflowStep step4 = new SendEmailStep(template, createUserRepository);
        step4.setPayload(registerForm);

        List<BaseWorflowStep> steps = new ArrayList<>();
        steps.add(step1);
        steps.add(step2);
        steps.add(step3);
        steps.add(step4);

        steps.stream().forEach(step -> step.setSagaId(sagaId));

        DefaultWorkflow workflow = new DefaultWorkflow(steps.stream().map(WorkflowStep.class::cast).toList());
        return workflow;
    }

    public void setRegisterForm(RegisterForm registerForm) {
        this.registerForm = registerForm;
    }

}
