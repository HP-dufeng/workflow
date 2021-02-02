package dangqu.powertrade.automation.process.rest.service.api.runtime.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.task.api.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dangqu.powertrade.automation.common.service.exception.UnauthorizedException;
import dangqu.powertrade.automation.process.rest.service.api.BaseAutomationResource;
import dangqu.powertrade.automation.process.rest.service.api.runtime.process.dto.RestProcessVariable;
import dangqu.powertrade.automation.process.rest.service.api.runtime.task.dto.CompleteTaskRequest;
import dangqu.powertrade.automation.process.rest.service.api.runtime.task.dto.TaskDto;
import dangqu.powertrade.automation.process.rest.service.api.runtime.task.dto.TaskResponse;
import dangqu.powertrade.security.conf.security.SecurityUtils;



@RestController
public class TaskResource extends BaseAutomationResource {


    @GetMapping("/rest/process/runtime/task")
    public TaskResponse getUserTasks(){

        String currentUserId = SecurityUtils.getCurrentUserId();
        // String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!StringUtils.isNotEmpty(currentUserId))
            throw new UnauthorizedException("");

        List<Task> taskList = taskService.createTaskQuery().taskAssignee(currentUserId).list();
        List<TaskDto> dtos = new ArrayList<>();
        for (Task task : taskList) {
            TaskDto taskDto = mapper.map(task, TaskDto.class);
            taskDto.setData(editorJsonService.getTaskSettingJsonData(task));
            
            dtos.add(taskDto);
        }

        TaskResponse response = new TaskResponse();
        response.setTaskList(dtos);


        return response;
    }

    @PostMapping("/rest/process/runtime/task/complete")
    public void completeTask(@RequestBody CompleteTaskRequest request) {

        Map<String, Object> variables = new HashMap<>();

        if (request.getVariables() != null && request.getVariables().size() > 0) {
            for (RestProcessVariable variable : request.getVariables()) {
                if (variable.getName() == null) {
                    throw new FlowableIllegalArgumentException("Variable name is required.");
                }
                variables.put(variable.getName(), variable.getValue());
            }
        }


        taskService.complete(request.getTaskId(), variables);
    
    }
}
