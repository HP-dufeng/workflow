package dangqu.powertrade.automation.editor.language.json.model;

import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getCellId;
import static dangqu.powertrade.automation.editor.language.json.converter.util.EditorJsonConverterUtil.getSettingName;

import java.util.function.Supplier;

import com.fasterxml.jackson.databind.JsonNode;

import org.flowable.bpmn.model.FlowElement;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class FlowElementModel {
    @NonNull
    private FlowElementType elementType;

    private String resourceId;
    private String name;

    protected void setCellProperties(JsonNode cellNode) {
    }

    protected void setSettingProperties(JsonNode settingNode) {
    }

    public abstract FlowElement convertToFlowElement();

    public Builder<FlowElementModel> builder() {
        Supplier<FlowElementModel> suppplier = () -> this;
        return new Builder<>(suppplier);
    }

    public static class Builder<T extends FlowElementModel> {
        private JsonNode cellNode;
        private JsonNode settingNode;

        private Supplier<T> supplier;

        private Builder(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        public Builder<T> cellNode(JsonNode cellNode) {
            this.cellNode = cellNode;
            return this;
        }

        public Builder<T> settingNode(JsonNode settingNode) {
            this.settingNode = settingNode;
            return this;
        }

        public T build() {
            T model = supplier.get();

            model.setResourceId(getCellId(cellNode));

            model.setCellProperties(cellNode);

            if (settingNode != null) {
                model.setName(getSettingName(settingNode));
                model.setSettingProperties(settingNode);
            }

            return model;

        }
    }
}
