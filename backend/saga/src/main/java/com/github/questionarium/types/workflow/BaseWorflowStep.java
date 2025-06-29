package com.github.questionarium.types.workflow;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;

import com.github.questionarium.interfaces.saga.WorkflowStep;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseWorflowStep implements WorkflowStep {

    protected final AsyncRabbitTemplate template;
    protected WorkflowStepStatus status = WorkflowStepStatus.PENDING;
    protected Object payload;
    protected Long sagaId;

    @Override
    public WorkflowStepStatus getStatus() {
        return this.status;
    }

    @Override
    public <T> void setPayload(T payload) {
        this.payload = payload;
    }

    public void setSagaId(Long id) {
        this.sagaId = id;
    }

}
