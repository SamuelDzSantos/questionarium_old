package com.github.questionarium.types.workflow;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;

import com.github.questionarium.interfaces.saga.SagaOrchestrator;
import com.github.questionarium.interfaces.saga.Workflow;
import com.github.questionarium.interfaces.saga.WorkflowStep;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class BaseSagaOrchestator implements SagaOrchestrator {

    protected final AsyncRabbitTemplate template;

    @Override
    public void performAction() {
        Workflow workflow = getWorkflow();
        Flux.fromStream(workflow.getSteps().stream()).concatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    System.out.println("Resultado:" + aBoolean);
                    if (aBoolean)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new RuntimeException("Error during step execution!"));
                }))
                .onErrorResume(ex -> revertAction())
                .subscribe();
    }

    @Override
    public Mono<Object> revertAction() {
        Workflow workflow = getWorkflow();
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkflowStep::revert)
                .retry(3)
                .then(Mono.empty());
    }

    abstract protected Workflow getWorkflow();
}
