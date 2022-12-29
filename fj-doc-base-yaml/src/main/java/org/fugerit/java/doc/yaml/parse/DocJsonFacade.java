package org.fugerit.java.doc.yaml.parse;

import java.io.Reader;

import org.fugerit.java.doc.base.model.DocBase;

public class DocJsonFacade {

	public static DocBase parse( Reader is ) throws Exception {
		DocYamlParser parser = new DocYamlParser();
		return parser.parse( is );
	}
	
}
