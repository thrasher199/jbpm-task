package com.jbpmtask.application.job;

import org.jbpm.executor.commands.SLATrackingCommand;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutionResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomSLATrackingCommand extends SLATrackingCommand {
    private static final Logger logger = LoggerFactory.getLogger(CustomSLATrackingCommand.class);

    @Override
    public ExecutionResults execute(CommandContext ctx) throws Exception {
        ExecutionResults executionResults = new ExecutionResults();
        logger.info("Executing CustomSLATrackingCommand");

        return executionResults;
    }
}
