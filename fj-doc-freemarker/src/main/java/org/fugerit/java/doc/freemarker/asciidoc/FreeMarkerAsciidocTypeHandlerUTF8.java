package org.fugerit.java.doc.freemarker.asciidoc;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class FreeMarkerAsciidocTypeHandlerUTF8 extends DocTypeHandlerDecorator {

	public static final DocTypeHandler HANDLER = new FreeMarkerAsciidocTypeHandlerUTF8();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerAsciidocTypeHandlerUTF8() {
		super( FreeMarkerAsciidocTypeHandler.HANDLER_UTF8 );
	}
	
}