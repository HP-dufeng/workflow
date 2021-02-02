package dangqu.powertrade.automation.editor.language.json.converter;

import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.UUID2NCName;
import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getCellId;
import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getCellShape;
import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getCells;
import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getSettings;
import static dangqu.powertrade.automation.editor.language.json.converter.util.JsonArrayNodeStream.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dangqu.powertrade.automation.editor.language.json.model.FlowElementModel;

public class EditorJsonConverter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(EditorJsonConverter.class);

    private static final String TARGET_NAMESPACE = "http://powertrade.lumicable.cn/automation";

    public static Builder createConverter() {
        return new Builder();
    }

    public static class Builder {
        private BpmnModel bpmn;
        private org.flowable.bpmn.model.Process process;

        private ObjectNode modelNode;

        public Builder() {
            this.bpmn = new BpmnModel();
            this.process = new org.flowable.bpmn.model.Process();
            bpmn.addProcess(process);

        }

        public Builder key(String key) {
            String id = UUID2NCName(key);
            process.setId(id);
            return this;
        }

        public Builder name(String name) {
            process.setName(name);
            return this;
        }

        public Builder documentation(String documentation) {
            process.setDocumentation(documentation);
            return this;
        }

        public Builder namespace(String namespace) {
            if (StringUtils.isNotEmpty(namespace))
                bpmn.setTargetNamespace(namespace);
            else
                bpmn.setTargetNamespace(TARGET_NAMESPACE);

            return this;
        }

        public Builder ModelEditorJson(ObjectNode modelNode) {
            this.modelNode = modelNode;

            return this;
        }

        public BpmnModel toBpmnModel() {

            ArrayNode cells = getCells(modelNode);
            ArrayNode settings = getSettings(modelNode);

            // convert json to pojo models
            List<FlowElementModel> flowElementModels = new ArrayList<>();
            for (JsonNode cell : cells) {
                String shape = getCellShape(cell);
                try {
                    FlowElementType elementType = FlowElementType.valueOf(shape);
                    JsonNode setting = stream(settings).filter(s -> Objects.equals(getCellId(s), getCellId(cell)))
                            .findFirst().orElse(null);

                    FlowElementModel model = FlowElementModelFactory.build(elementType, cell, setting);
                    flowElementModels.add(model);
                } catch (IllegalArgumentException e) {
                    throw new FlowableIllegalArgumentException("Unsupported shape for converter: " + shape, e);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    throw new FlowableIllegalArgumentException("Invalid json format for converter: " + shape, e);

                }

            }

            // convert pojo models to bpmn FlowElement
            for (FlowElementModel model : flowElementModels) {

                process.addFlowElement(model.convertToFlowElement());
            }
            
            return bpmn;
        }

    }

}
