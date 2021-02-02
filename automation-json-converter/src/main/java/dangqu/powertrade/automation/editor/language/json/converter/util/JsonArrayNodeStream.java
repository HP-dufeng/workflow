package dangqu.powertrade.automation.editor.language.json.converter.util;

import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.stream.Stream;

public final class JsonArrayNodeStream {
    public static Stream<JsonNode> stream(ArrayNode arrayNode) {
        Stream<JsonNode> stream = Stream.of();

        if (arrayNode != null)
            stream = IntStream.range(0, arrayNode.size()).mapToObj(arrayNode::get);

        return stream;
    }
}
