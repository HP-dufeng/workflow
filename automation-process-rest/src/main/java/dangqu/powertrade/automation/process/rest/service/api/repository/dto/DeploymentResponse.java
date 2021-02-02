package dangqu.powertrade.automation.process.rest.service.api.repository.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.flowable.engine.repository.Deployment;

import dangqu.powertrade.automation.process.rest.util.DateToStringSerializer;
import lombok.Data;

@Data
public class DeploymentResponse {

    private String id;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String name;
    @JsonSerialize(using = DateToStringSerializer.class, as = Date.class)
    private Date deploymentTime;
    private String category;
    private String tenantId;

    public DeploymentResponse(Deployment deployment, String processDefinitionId, String processDefinitionKey) {
        setId(deployment.getId());
        setName(deployment.getName());
        setDeploymentTime(deployment.getDeploymentTime());
        setCategory(deployment.getCategory());
        setTenantId(deployment.getTenantId());

        setProcessDefinitionId(processDefinitionId);
        setProcessDefinitionKey(processDefinitionKey);
    }

    


}
