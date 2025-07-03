package com.github.questionarium.service.saga.patch_user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.stereotype.Service;

import com.github.questionarium.interfaces.saga.Workflow;
import com.github.questionarium.interfaces.saga.WorkflowStep;
import com.github.questionarium.interfaces.user.EmailPatch;
import com.github.questionarium.interfaces.user.UserPatch;
import com.github.questionarium.interfaces.user.UsernamePatch;
import com.github.questionarium.model.PatchUserState;
import com.github.questionarium.repositories.PatchUserRepository;
import com.github.questionarium.service.saga.patch_user.steps.UpdateAuthEmailStep;
import com.github.questionarium.service.saga.patch_user.steps.UpdateUserNameStep;
import com.github.questionarium.types.workflow.BaseSagaOrchestator;
import com.github.questionarium.types.workflow.BaseWorflowStep;
import com.github.questionarium.types.workflow.DefaultWorkflow;

@Service
public class PatchUserOrchestratorService extends BaseSagaOrchestator {

    private UserPatch userPatch;
    private Long userId;
    private final PatchUserRepository patchUserRepository;

    public PatchUserOrchestratorService(AsyncRabbitTemplate template, PatchUserRepository patchUserRepository) {
        super(template);
        this.patchUserRepository = patchUserRepository;
    }

    protected Workflow getWorkflow() {

        Long sagaId = patchUserRepository
                .save(new PatchUserState()).getSaga_id();

        BaseWorflowStep step1 = new UpdateAuthEmailStep(this.template, patchUserRepository);

        EmailPatch emailPatch = new EmailPatch(this.userId, this.userPatch.email());

        step1.setPayload(emailPatch);

        UsernamePatch usernamePatch = new UsernamePatch(this.userId, this.userPatch.name());

        BaseWorflowStep step2 = new UpdateUserNameStep(this.template, patchUserRepository);
        step2.setPayload(usernamePatch);

        List<BaseWorflowStep> steps = new ArrayList<>();
        steps.add(step1);
        steps.add(step2);

        steps.stream().forEach(step -> step.setSagaId(sagaId));

        DefaultWorkflow workflow = new DefaultWorkflow(steps.stream().map(WorkflowStep.class::cast).toList());
        return workflow;

    }

    public void setUserPatch(UserPatch userPatch) {
        this.userPatch = userPatch;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
