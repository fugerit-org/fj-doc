package org.fugerit.java.doc.json.parse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConstants {

    private JsonConstants() {}

    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    public static ObjectMapper getDefaultMapper() {
        return DEFAULT_MAPPER;
    }

}
