package dangqu.powertrade.automation.process.rest.service.api.repository;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dangqu.powertrade.automation.modeler.model.ModelRepresentation;
import dangqu.powertrade.automation.modeler.serviceapi.ModelService;
import dangqu.powertrade.automation.process.rest.service.api.BaseAutomationResource;
import dangqu.powertrade.automation.process.rest.service.api.repository.dto.DeployRequestDto;
import dangqu.powertrade.automation.process.rest.service.api.repository.dto.DeploymentResponse;
import dangqu.powertrade.security.conf.tenant.TenantProvider;

@RestController
public class DeploymentResource extends BaseAutomationResource {

    @Autowired
    private TenantProvider tenantProvider;

    @Autowired
    protected ModelService modelService;

    @PostMapping("rest/process/repository/deploy")
    public DeploymentResponse deployProcess(@RequestBody DeployRequestDto requestDto) {

        
        ModelRepresentation model = modelService.getModelRepresentation(requestDto.getModelId());
    
        BpmnModel bpmnModel = editorJsonService.getBpmnModel(
            model.getKey(), 
            model.getName(), 
            model.getDescription(), 
            requestDto.getCategory(), 
            model.getModelEditorJson());

        String tenantId = tenantProvider.getTenantId();
        String resourceName = String.format("%s.bpmn20.xml", model.getName().replaceAll("[^\\d\\w]", "_"));
        if(!StringUtils.isNotEmpty(resourceName))
            resourceName = model.getKey().replaceAll("-", "_");

        Deployment deployment = repositoryService
            .createDeployment()
            .name(model.getName())
            .category(requestDto.getCategory())
            .tenantId(tenantId)
            .addBpmnModel(resourceName, bpmnModel)
            .deploy();

        ProcessDefinition processDefinition =
        repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        
        return new DeploymentResponse(deployment, processDefinition.getId(), processDefinition.getKey());
// return null;
    }

}
