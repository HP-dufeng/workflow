package dangqu.powertrade.automation.editor.language.json.model;

import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getSettingNominator;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import org.flowable.bpmn.model.CustomProperty;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTaskModel extends TaskModel {

    private List<String> candidateUsers;

    public UserTaskModel() {
        super(FlowElementType.UserTask);
    }

    

    @Override
    protected void setSettingProperties(JsonNode settingNode){
        this.candidateUsers = getSettingNominator(settingNode);
        this.setting = settingNode.toString();
    }

    @Override
    public FlowElement convertToFlowElement() {
        UserTask userTask = new UserTask();
        userTask.setId(this.getResourceId());
        userTask.setName(this.getName());

        userTask.setCandidateUsers(candidateUsers);

        CustomProperty customProperty = new CustomProperty();
        customProperty.setName("setting");
        customProperty.setSimpleValue(this.setting);
        userTask.getCustomProperties().add(customProperty);

        return userTask;
    }




}
