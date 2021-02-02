package dangqu.powertrade.automation.process.rest.service.api.runtime.task.dto;

import lombok.Data;

@Data
public class FormPropertyDto {
    private String id;
    private String name;
    private String type;
    private String value;
    private Boolean required;
    private Boolean readable;
    private Boolean writable;
}
