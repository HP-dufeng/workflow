package dangqu.powertrade.automation.process.rest.service.api.runtime.process.dto;

public class RestProcessVariable {
    private String name;
    private Object value;

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }
}
