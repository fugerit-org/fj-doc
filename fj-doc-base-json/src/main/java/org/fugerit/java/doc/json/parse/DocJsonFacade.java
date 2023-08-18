package org.fugerit.java.doc.json.parse;

import java.io.Reader;

import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.model.DocBase;

public class DocJsonFacade {

	private DocJsonFacade() {}
	
	public static DocBase parse( Reader is ) throws DocException {
		DocJsonParser parser = new DocJsonParser();
		return parser.parse( is );
	}
	
}
