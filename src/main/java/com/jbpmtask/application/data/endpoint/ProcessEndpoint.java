package com.jbpmtask.application.data.endpoint;

import com.jbpmtask.application.data.dto.ProcessDefDto;
import com.jbpmtask.application.data.dto.TaskSummaryDto;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.kie.internal.query.QueryContext;
import org.kie.internal.query.QueryFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.stream.Collectors;

@Endpoint
@AnonymousAllowed
public class ProcessEndpoint {

    private final RuntimeDataService runtimeDataService;
    private final ModelMapper modelMapper;
    private final ProcessService processService;

    public ProcessEndpoint(RuntimeDataService runtimeDataService, ModelMapper modelMapper, ProcessService processService) {
        this.runtimeDataService = runtimeDataService;
        this.modelMapper = modelMapper;
        this.processService = processService;
    }


    public @Nonnull List<@Nonnull ProcessDefDto> listProcess() {
        return runtimeDataService.getProcesses(new QueryContext())
                .stream()
                .map(processDefinition -> modelMapper.map(processDefinition, ProcessDefDto.class))
                .collect(Collectors.toList());
    }

    public long startProcess(String deploymentId, String processId) {
        return processService.startProcess(deploymentId, processId);
    }

    public @Nonnull List<@Nonnull TaskSummaryDto> listUserTask() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return runtimeDataService.getTasksOwned(auth.getName(), new QueryFilter())
                .stream()
                .map(taskSummary -> modelMapper.map(taskSummary, TaskSummaryDto.class))
                .collect(Collectors.toList());
    }

    public @Nonnull Flux<@Nonnull TaskSummaryDto> getFlux(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<TaskSummaryDto> dto = runtimeDataService.getTasksOwned(auth.getName(), new QueryFilter())
                .stream()
                .map(taskSummary -> modelMapper.map(taskSummary, TaskSummaryDto.class))
                .collect(Collectors.toList());

        return Flux.fromIterable(dto);
    }

}
