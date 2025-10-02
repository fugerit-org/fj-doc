package org.fugerit.java.doc.yaml.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class YamlConstants {

    private YamlConstants() {}

    private static final ObjectMapper DEFAULT_MAPPER = new YAMLMapper();

    public static ObjectMapper getDefaultMapper() {
        return DEFAULT_MAPPER;
    }

}


