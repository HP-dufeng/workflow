package dangqu.powertrade.automation.process.rest.service.api.runtime.process.dto;

import java.util.List;

import lombok.Data;

@Data
public class StartProcessInstanceRequest {

    private String processDefinitionKey;
    private List<RestProcessVariable> variables;

}
