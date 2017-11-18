package com.wf.data.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * Created by chris on 2017/3/8.
 */

/**
 * Helper functions to handle JsonNode values.
 */
public class JsonHelper {
    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();
    private static volatile ObjectMapper objectMapper = null;

    private static ObjectMapper mapper() {
        if (objectMapper == null) {
            return defaultObjectMapper;
        } else {
            return objectMapper;
        }
    }

    /**
     * Convert an object to JsonNode.
     *
     * @param data Value to convert in Json.
     */
    public static JsonNode toJson(final Object data) {
        try {
            return mapper().valueToTree(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a JsonNode to a Java value
     *
     * @param json  Json value to convert.
     * @param clazz Expected Java value type.
     */
    public static <A> A fromJson(JsonNode json, Class<A> clazz) {
        try {
            return mapper().treeToValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String jsonToXml(JsonNode jn) {
        StringBuilder sb = new StringBuilder("<xml>");
        Map<String, String> map = JsonHelper.fromJson(jn, Map.class);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("<").append(entry.getKey()).append("><![CDATA[").append(entry.getValue()).append("]]></").append(entry.getKey()).append(">");
        }
        sb.append("</xml>");
        return sb.toString();
    }
}
