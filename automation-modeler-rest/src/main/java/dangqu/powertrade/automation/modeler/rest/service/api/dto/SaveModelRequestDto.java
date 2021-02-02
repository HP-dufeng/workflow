package dangqu.powertrade.automation.modeler.rest.service.api.dto;

import lombok.Data;

@Data
public class SaveModelRequestDto {
    private String id;
    private String name;
    private Integer modelType;
    private String description;
    private String modelEditorJson;

    
}
