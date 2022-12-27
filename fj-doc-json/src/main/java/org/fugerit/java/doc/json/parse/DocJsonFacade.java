package org.fugerit.java.doc.json.parse;

import java.io.Reader;

import org.fugerit.java.doc.base.model.DocBase;

public class DocJsonFacade {

	public static DocBase parse( Reader is ) throws Exception {
		DocJsonParser parser = new DocJsonParser();
		return parser.parse( is );
	}
	
}
