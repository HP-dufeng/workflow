package dangqu.powertrade.automation.editor.language.json.model;

import dangqu.powertrade.automation.editor.language.json.converter.FlowElementType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class TaskModel extends FlowElementModel {
    protected String formKey;
    protected String setting;

    public TaskModel(FlowElementType elementType) {
        super(elementType);
    }
}
