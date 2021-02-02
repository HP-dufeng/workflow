package dangqu.powertrade.automation.editor.language.json.model;


import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.StartEvent;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;


public class StartModel extends FlowElementModel {

    public StartModel() {
        super(FlowElementType.Start);
    }



    @Override
    public FlowElement convertToFlowElement() {
        StartEvent startEvent = new StartEvent();
        startEvent.setInitiator("initiator");
        startEvent.setId(this.getResourceId());
        startEvent.setName(this.getName());

        return startEvent;
    }

}
