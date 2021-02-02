package dangqu.powertrade.automation.editor.language.json.service;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.task.api.Task;

public interface EditorJsonService {

    BpmnModel getBpmnModel(String key, String name, String description, String namespace, String modelEditorJson);

    String getProcessDefinitionKey(String key);

    String getTaskSettingJsonData(String taskId);
    String getTaskSettingJsonData(Task task);
}
