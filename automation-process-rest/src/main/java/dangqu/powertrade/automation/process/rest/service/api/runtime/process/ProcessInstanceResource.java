package dangqu.powertrade.automation.process.rest.service.api.runtime.process;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dangqu.powertrade.automation.process.rest.service.api.BaseAutomationResource;
import dangqu.powertrade.automation.process.rest.service.api.runtime.process.dto.RestProcessVariable;
import dangqu.powertrade.automation.process.rest.service.api.runtime.process.dto.StartProcessInstanceRequest;
import dangqu.powertrade.security.conf.security.SecurityUtils;

@RestController
public class ProcessInstanceResource extends BaseAutomationResource {

    @PostMapping("/rest/process/runtime/process-instances")
    public void startProcessInstance(@RequestBody StartProcessInstanceRequest request, HttpServletResponse response) {
        if (request.getProcessDefinitionKey() == null || SecurityUtils.getCurrentUserId() == null) {
            throw new FlowableIllegalArgumentException("Either processDefinitionKey, userId is required.");
        }

        Map<String, Object> startVariables = new HashMap<>();
        startVariables.put("initiator", SecurityUtils.getCurrentUserId());
        // startVariables.put("initiator", SecurityContextHolder.getContext().getAuthentication().getName());

        if (request.getVariables() != null && request.getVariables().size() > 0) {
            for (RestProcessVariable variable : request.getVariables()) {
                if (variable.getName() == null) {
                    throw new FlowableIllegalArgumentException("Variable name is required.");
                }
                startVariables.put(variable.getName(), variable.getValue());
            }
        }

        try {

            String processDefinitionKey = editorJsonService.getProcessDefinitionKey(request.getProcessDefinitionKey());
            ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();

            runtimeService.startProcessInstanceById(processDefinition.getId(), startVariables);

            response.setStatus(HttpStatus.CREATED.value());

        } catch (FlowableObjectNotFoundException e) {
            throw new FlowableIllegalArgumentException(e.getMessage(), e);
        }
    }
}
