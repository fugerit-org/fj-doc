package org.fugerit.java.doc.yaml.parse;

import org.fugerit.java.doc.json.parse.DocJsonToXml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class DocYamlToXml extends DocJsonToXml {

	public DocYamlToXml() {
		super( new ObjectMapper( new YAMLFactory() ) );
	}

	public DocYamlToXml(ObjectMapper mapper) {
		super(mapper);
	}

}
