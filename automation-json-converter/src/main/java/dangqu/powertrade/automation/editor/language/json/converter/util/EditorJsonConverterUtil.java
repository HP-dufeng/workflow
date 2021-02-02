package dangqu.powertrade.automation.editor.language.json.converter.util;

import static dangqu.powertrade.automation.editor.language.json.converter.util.JsonArrayNodeStream.stream;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class EditorJsonConverterUtil{

    public static final String KEY_PRIFIX = "pt_";
    
    public static ArrayNode getCells(JsonNode modelNode) {
        return (ArrayNode) modelNode.get("cells");
    }

    public static ArrayNode getSettings(JsonNode modelNode) {
        return (ArrayNode) modelNode.get("data");
    }


    public static String getCellId(JsonNode cell) {
        String id = getValueAsString("id", cell);

        return UUID2NCName(id);
    }

    public static String getCellShape(JsonNode cell) {
        return getValueAsString("shape", cell);
    }

    

    public static String getCellSource(JsonNode cell) {
        String id = cell.get("source").get("cell").asText();
        return UUID2NCName(id);
    }

    public static String getCellTarget(JsonNode cell) {
        String id = cell.get("target").get("cell").asText();
        return UUID2NCName(id);
    }


    public static String getSettingName(JsonNode settingNode) {
        return getValueAsString("name", settingNode);
    }

    public static String getSettingFormKey(JsonNode settingNode) {
        return getValueAsString("formKey", settingNode);
    }

    public static String getSettingCondition(JsonNode settingNode) {
        return getValueAsString("ruleProperties", settingNode);
    }

    public static String getSettingToTheRules(JsonNode settingNode) {
        return getValueAsString("toTheRules", settingNode);
    }

    public static List<String> getSettingNominator(JsonNode settingNode) {
        ArrayNode nodes = (ArrayNode) settingNode.get("nominator");
        List<String> collect = stream(nodes)
            .flatMap(m -> stream((ArrayNode)m.get("ruleIds")))
            .map(m -> m.asText())
            .collect(Collectors.toList());

        return collect;
    }

    // public static List<String> getSettingFunctionDesign(JsonNode settingNode) {
    //     List<String> commands = new ArrayList<>();
        
    //     JsonNode node = settingNode.get("functionDesign");
    //     if(node != null && !node.isNull())
    //         node.fieldNames().forEachRemaining(commands::add);

    //     return commands;
    // }


    public static String getValueAsString(String name, JsonNode settingNode) {
        String propertyValue = null;
        JsonNode propertyNode = settingNode.get(name);
        if (propertyNode != null && !propertyNode.isNull()) {
            propertyValue = propertyNode.asText();
        }
        return propertyValue;
    }

    

    /**
     * reslove the exception of org.xml.sax.SAXParseException: is not a valid value for 'NCName'
     */
    public static String UUID2NCName(String id) {

        if(isUUID(id)){
            return KEY_PRIFIX + id;
        }

        return id;
    }

    public static Boolean isUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
