package org.fugerit.java.doc.yaml.parse;

import java.io.Reader;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.model.DocBase;

public class DocJsonFacade {

	private DocJsonFacade() {}	// java:S1118
	
	public static DocBase parse( Reader is ) throws DocException {
		DocYamlParser parser = new DocYamlParser();
		return parser.parse( is );
	}
	
}
