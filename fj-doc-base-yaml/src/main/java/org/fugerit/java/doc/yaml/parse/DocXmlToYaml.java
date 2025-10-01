package org.fugerit.java.doc.yaml.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.fugerit.java.doc.json.parse.DocXmlToJson;

public class DocXmlToYaml extends DocXmlToJson {

    private static final ObjectMapper YAML_MAPPER = new YAMLMapper();

    public DocXmlToYaml() {
        super(YAML_MAPPER);
    }

}
