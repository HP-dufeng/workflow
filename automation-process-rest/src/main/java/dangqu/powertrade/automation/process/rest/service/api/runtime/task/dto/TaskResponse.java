package dangqu.powertrade.automation.process.rest.service.api.runtime.task.dto;

import java.util.List;

public class TaskResponse {
    private List<TaskDto> taskList;

    public List<TaskDto> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskDto> taskList) {
        this.taskList = taskList;
    }
}
