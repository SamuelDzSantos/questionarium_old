package com.github.questionarium.interfaces.saga;

import com.github.questionarium.types.workflow.WorkflowStepStatus;

import reactor.core.publisher.Mono;

public interface WorkflowStep {

    WorkflowStepStatus getStatus();

    Mono<Boolean> process();

    Mono<Boolean> revert();

    public <T> void setPayload(T payload);

}