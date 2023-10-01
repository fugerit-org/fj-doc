package org.fugerit.java.doc.yaml.parse;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.json.parse.DocJsonParser;
import org.fugerit.java.xml2json.XmlToJsonHandler;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class DocYamlParser extends DocJsonParser {
	
	public DocYamlParser( XmlToJsonHandler handler ) {
		super( DocFacadeSource.SOURCE_TYPE_YAML, handler );
	}
	
	public DocYamlParser() {
		this( new XmlToJsonHandler( new YAMLMapper() ) );
	}

}
