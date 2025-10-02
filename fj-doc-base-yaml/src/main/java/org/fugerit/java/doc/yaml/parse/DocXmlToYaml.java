package org.fugerit.java.doc.yaml.parse;

import org.fugerit.java.doc.json.parse.DocXmlToJson;

public class DocXmlToYaml extends DocXmlToJson {

    public DocXmlToYaml() {
        super( YamlConstants.getDefaultMapper() );
    }

}
