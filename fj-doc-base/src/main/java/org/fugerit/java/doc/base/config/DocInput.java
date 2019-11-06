package org.fugerit.java.doc.base.config;

import org.fugerit.java.doc.base.model.DocBase;

public class DocInput {

	private String type;
	
	private DocBase doc;

	public String getType() {
		return type;
	}

	public DocBase getDoc() {
		return doc;
	}

	public DocInput(String type, DocBase doc) {
		super();
		this.type = type;
		this.doc = doc;
	}

	public static DocInput newInput( String type, DocBase doc ) {
		return new DocInput(type, doc);
	}
	
}
