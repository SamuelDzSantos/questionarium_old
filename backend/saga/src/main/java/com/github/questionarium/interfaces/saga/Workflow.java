package com.github.questionarium.interfaces.saga;

import java.util.List;

public interface Workflow {
    List<WorkflowStep> getSteps();
}