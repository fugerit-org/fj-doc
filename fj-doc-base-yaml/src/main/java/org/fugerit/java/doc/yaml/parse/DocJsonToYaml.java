package org.fugerit.java.doc.yaml.parse;

import org.fugerit.java.doc.json.parse.JsonConstants;

public class DocJsonToYaml extends DocYamlToJson {

    public DocJsonToYaml() {
        super(JsonConstants.getDefaultMapper(), YamlConstants.getDefaultMapper());
    }

}
