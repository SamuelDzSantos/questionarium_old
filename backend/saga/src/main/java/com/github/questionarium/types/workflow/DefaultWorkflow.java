package com.github.questionarium.types.workflow;

import java.util.List;

import com.github.questionarium.interfaces.saga.Workflow;
import com.github.questionarium.interfaces.saga.WorkflowStep;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultWorkflow implements Workflow {

    private final List<WorkflowStep> steps;

    @Override
    public List<WorkflowStep> getSteps() {
        return steps;
    }

}
