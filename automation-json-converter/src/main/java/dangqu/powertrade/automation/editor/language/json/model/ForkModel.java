package dangqu.powertrade.automation.editor.language.json.model;

import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getSettingToTheRules;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Gateway;
import org.flowable.bpmn.model.InclusiveGateway;
import org.flowable.bpmn.model.ParallelGateway;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForkModel extends FlowElementModel {

    private GatewayType gatewayType;

    public ForkModel() {
        super(FlowElementType.Fork);
    }

    public enum GatewayType {
        Parallel("1"), Inclusive("2");

        private static final Map<String, GatewayType> map = new HashMap<>();

        static {
            for (GatewayType e : values()) {
                map.put(e.typeDescriptor, e);
            }
        }

        private String typeDescriptor;

        GatewayType(String typeDescriptor) {
            this.typeDescriptor = typeDescriptor;
        }

        public String getTypeDescriptor() {
            return typeDescriptor;
        }

        public static GatewayType valueOfLabel(String label) {
            return map.get(label);
        }

    }

    @Override
    protected void setSettingProperties(JsonNode settingNode) {
        String typeDescriptor = getSettingToTheRules(settingNode);
        this.gatewayType = GatewayType.valueOfLabel(typeDescriptor);
    }

    @Override
    public FlowElement convertToFlowElement() {
        Gateway gateway;
        if (GatewayType.Inclusive.equals(this.getGatewayType())) {
            gateway = new InclusiveGateway();
        } else {
            gateway = new ParallelGateway();
        }

        gateway.setId(this.getResourceId());
        gateway.setName(this.getName());

        return gateway;
    }

}
