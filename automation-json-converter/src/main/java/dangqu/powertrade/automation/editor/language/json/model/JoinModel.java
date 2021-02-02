package dangqu.powertrade.automation.editor.language.json.model;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.InclusiveGateway;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;

public class JoinModel extends FlowElementModel {

    public JoinModel() {
        super(FlowElementType.Join);
    }

    @Override
    public FlowElement convertToFlowElement() {
        InclusiveGateway gateway = new InclusiveGateway();

        gateway.setId(this.getResourceId());
        gateway.setName(this.getName());

        return gateway;
    }
}
