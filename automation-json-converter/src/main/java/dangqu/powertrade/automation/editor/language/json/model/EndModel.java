package dangqu.powertrade.automation.editor.language.json.model;

import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowElement;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;


public class EndModel extends FlowElementModel {


    public EndModel() {
        super(FlowElementType.End);
    }

    @Override
    public FlowElement convertToFlowElement() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(this.getResourceId());
        endEvent.setName(this.getName());

        return endEvent;
    }


}
