package dangqu.powertrade.automation.common.service.exception;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ErrorInfo {

    private String message;
    private String messageKey;
    private Map<String, Object> customData;

    public ErrorInfo(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @JsonInclude(Include.NON_NULL)
    public String getMessageKey() {
        return messageKey;
    }

    @JsonInclude(Include.NON_EMPTY)
    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> params) {
        this.customData = params;
    }

    public void addParameter(String name, Object value) {
        if (customData == null) {
            customData = new HashMap<>();
        }
        customData.put(name, value);
    }
}
