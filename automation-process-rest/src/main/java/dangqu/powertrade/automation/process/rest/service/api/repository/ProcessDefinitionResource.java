package dangqu.powertrade.automation.process.rest.service.api.repository;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import dangqu.powertrade.automation.process.rest.service.api.BaseAutomationResource;

@RestController
public class ProcessDefinitionResource extends BaseAutomationResource {

    @GetMapping(value = "rest/process/repository/process-definitions/{processDefinitionId}/resourcedata")
    public byte[] getProcessDefinitionResource(
            @PathVariable String processDefinitionId,
            HttpServletResponse response) {

        response.setContentType("text/xml");

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();

        if (processDefinition == null) {
            throw new FlowableObjectNotFoundException("Could not find a process definition with id '" + processDefinitionId + "'.", ProcessDefinition.class);
        }

        String deploymentId = processDefinition.getDeploymentId();
        String resourceName = processDefinition.getResourceName();

        if (deploymentId == null) {
            throw new FlowableIllegalArgumentException("No deployment id provided");
        }
        if (resourceName == null) {
            throw new FlowableIllegalArgumentException("No resource name provided");
        }

        // Check if deployment exists
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
        if (deployment == null) {
            throw new FlowableObjectNotFoundException("Could not find a deployment with id '" + deploymentId + "'.", Deployment.class);
        }
        

        List<String> resourceList = repositoryService.getDeploymentResourceNames(deploymentId);

        if (resourceList.contains(resourceName)) {
            final InputStream resourceStream = repositoryService.getResourceAsStream(deploymentId, resourceName);

            
            try {
                return IOUtils.toByteArray(resourceStream);
            } catch (Exception e) {
                throw new FlowableException("Error converting resource stream", e);
            }
        } else {
            // Resource not found in deployment
            throw new FlowableObjectNotFoundException("Could not find a resource with name '" + resourceName + "' in deployment '" + deploymentId + "'.", String.class);
        }


    }
}
