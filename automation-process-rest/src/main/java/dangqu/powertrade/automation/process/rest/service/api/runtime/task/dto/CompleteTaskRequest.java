package dangqu.powertrade.automation.process.rest.service.api.runtime.task.dto;

import java.util.List;

import dangqu.powertrade.automation.process.rest.service.api.runtime.process.dto.RestProcessVariable;
import lombok.Data;

@Data
public class CompleteTaskRequest {
    private String taskId;

    private List<RestProcessVariable> variables;

}
