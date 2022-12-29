package org.fugerit.java.doc.base.parser;

import org.fugerit.java.doc.base.model.DocBase;

public class DocParserNameCheck {

	private static final DocParserNameCheck INSTANCE = new DocParserNameCheck();
	
	public static DocParserNameCheck getInstance() {
		return INSTANCE;
	}
	
	public boolean isTypeDoc( String name ) {
		return DocBase.TAG_NAME.equalsIgnoreCase( name );
	}
	
}
