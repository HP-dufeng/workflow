package dangqu.powertrade.automation.editor.language.json.service;

import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.UUID2NCName;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dangqu.powertrade.automation.editor.language.json.converter.EditorJsonConverter;

@Service
public class EditorJsonServiceImpl implements EditorJsonService {

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RepositoryService repositoryService;

    @Override
    public BpmnModel getBpmnModel(String key, String name, String description, String namespace, String modelEditorJson) {
        ObjectNode editorJsonNode;
        try {
            
            ObjectMapper objectMapper = new ObjectMapper();
            editorJsonNode = (ObjectNode) objectMapper.readTree(modelEditorJson);
        } catch (JsonProcessingException e) {
            throw new FlowableIllegalArgumentException("Invalid editor json string.", e);
        }

        BpmnModel bpmnModel = EditorJsonConverter
                .createConverter()
                .key(key)
                .name(name)
                .documentation(description)
                .namespace(namespace)
                .ModelEditorJson(editorJsonNode)
                .toBpmnModel();

        return bpmnModel;
    }

    @Override
    public String getProcessDefinitionKey(String key) {
        return UUID2NCName(key);
    }

    @Override
    public String getTaskSettingJsonData(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        return getTaskSettingJsonData(task);
    }

    @Override
    public String getTaskSettingJsonData(Task task) {

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        List<ExtensionElement> list = flowElement.getExtensionElements().get("setting");
        if(list != null && list.size() > 0) {
            String elementText = list.get(0).getElementText();
            return elementText;
        }

        return null;
    }





}
