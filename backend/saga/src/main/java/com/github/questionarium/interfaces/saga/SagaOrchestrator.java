package com.github.questionarium.interfaces.saga;

import reactor.core.publisher.Mono;

public interface SagaOrchestrator {

    public void performAction();

    public Mono<Object> revertAction();

}
