package org.fugerit.java.doc.freemarker.html;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class FreeMarkerHtmlTypeHandlerUTF8 extends DocTypeHandlerDecorator {

	public static final DocTypeHandler HANDLER = new FreeMarkerHtmlTypeHandlerUTF8();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerHtmlTypeHandlerUTF8() {
		super( FreeMarkerHtmlTypeHandler.HANDLER_UTF8 );
	}
	
}