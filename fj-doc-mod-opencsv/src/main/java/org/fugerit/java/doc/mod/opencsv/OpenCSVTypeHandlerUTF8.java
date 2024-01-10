package org.fugerit.java.doc.mod.opencsv;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class OpenCSVTypeHandlerUTF8 extends DocTypeHandlerDecorator {
	
	private static final long serialVersionUID = 6903524359788592137L;
	
	public static final DocTypeHandler HANDLER = new OpenCSVTypeHandlerUTF8();
	
	public OpenCSVTypeHandlerUTF8() {
		super( OpenCSVTypeHandler.HANDLER_UTF8 );
	}

}
