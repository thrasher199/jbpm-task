package com.jbpmtask.application.data.endpoint;

import com.jbpmtask.application.data.dto.ProcessDefDto;
import com.jbpmtask.application.data.dto.TaskSummaryDto;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;
import dev.hilla.Nonnull;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.UserTaskService;
import org.jbpm.services.api.admin.UserTaskAdminService;
import org.kie.internal.query.QueryContext;
import org.kie.internal.query.QueryFilter;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Endpoint
@AnonymousAllowed
public class ProcessEndpoint {

    private final RuntimeDataService runtimeDataService;
    private final ModelMapper modelMapper;
    private final ProcessService processService;
    private final UserTaskService userTaskService;

    public ProcessEndpoint(RuntimeDataService runtimeDataService, ModelMapper modelMapper, ProcessService processService, UserTaskService userTaskService) {
        this.runtimeDataService = runtimeDataService;
        this.modelMapper = modelMapper;
        this.processService = processService;
        this.userTaskService = userTaskService;
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

    public @Nonnull List<@Nonnull TaskSummaryDto> completeTask(long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userTaskService.completeAutoProgress(id, auth.getName(), new HashMap<>());
        return listUserTask();
    }

    public @Nonnull List<@Nonnull TaskSummaryDto> release(long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userTaskService.release(id, auth.getName());
        return listUserTask();
    }

    public @Nonnull List<@Nonnull TaskSummaryDto> poolList(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return runtimeDataService.getTasksAssignedAsPotentialOwner(auth.getName(), new QueryFilter()).stream()
                .map(taskSummary -> modelMapper.map(taskSummary, TaskSummaryDto.class))
                .collect(Collectors.toList());
    }

    public @Nonnull List<@Nonnull TaskSummaryDto> claimTask(long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userTaskService.claim(id, auth.getName());
        return poolList();
    }

    public void testMethod(String id){
        ////
    }
}
