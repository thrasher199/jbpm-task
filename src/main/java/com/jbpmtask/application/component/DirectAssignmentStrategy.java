package com.jbpmtask.application.component;

import org.kie.api.task.TaskContext;
import org.kie.api.task.model.Task;
import org.kie.internal.task.api.assignment.Assignment;
import org.kie.internal.task.api.assignment.AssignmentStrategy;

public class DirectAssignmentStrategy implements AssignmentStrategy {
    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public Assignment apply(Task task, TaskContext context, String excludedUser) {
        return null;
    }
}
