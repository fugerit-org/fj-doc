package org.fugerit.java.doc.yaml.parse;

import org.fugerit.java.doc.json.parse.DocJsonToXml;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DocYamlToXml extends DocJsonToXml {

	public DocYamlToXml() {
		super( YamlConstants.getDefaultMapper() );
	}

	public DocYamlToXml(ObjectMapper mapper) {
		super(mapper);
	}

}
