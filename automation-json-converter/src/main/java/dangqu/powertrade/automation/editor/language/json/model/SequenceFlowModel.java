package dangqu.powertrade.automation.editor.language.json.model;

import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getCellSource;
import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getCellTarget;
import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getSettingCondition;

import com.fasterxml.jackson.databind.JsonNode;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SequenceFlowModel extends FlowElementModel {

    private String source;
    private String target;

    private String condition;

    public SequenceFlowModel() {
        super(FlowElementType.SequenceFlow);
    }

    

    @Override
    protected void setCellProperties(JsonNode cellNode){
        this.source = getCellSource(cellNode);
        this.target = getCellTarget(cellNode);
    }

    @Override
    protected void setSettingProperties(JsonNode settingNode){
        this.condition = getSettingCondition(settingNode);
    }

    @Override
    public FlowElement convertToFlowElement() {
        SequenceFlow sequence = new SequenceFlow(this.getSource(), this.getTarget());
        sequence.setConditionExpression(this.getCondition());
        sequence.setId(this.getResourceId());
        sequence.setName(this.getName());
        
        return sequence;
    }

}
