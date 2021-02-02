package dangqu.powertrade.automation.editor.language.json.model;

import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getSettingFormKey;

import com.fasterxml.jackson.databind.JsonNode;

import org.flowable.bpmn.model.CustomProperty;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;

public class ApplicantModel extends TaskModel {

    public ApplicantModel() {
        super(FlowElementType.Applicant);
    }

    @Override
    protected void setSettingProperties(JsonNode settingNode) {
        this.formKey = getSettingFormKey(settingNode);
        this.setting = settingNode.toString();

    }

    @Override
    public FlowElement convertToFlowElement() {
        UserTask userTask = new UserTask();
        userTask.setId(this.getResourceId());
        userTask.setName(this.getName());

        userTask.setFormKey(this.getFormKey());
        userTask.setAssignee("${initiator}");

        CustomProperty customProperty = new CustomProperty();
        customProperty.setName("setting");
        customProperty.setSimpleValue(this.setting);
        userTask.getCustomProperties().add(customProperty);

        return userTask;
    }
}
