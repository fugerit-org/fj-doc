package org.fugerit.java.doc.freemarker.html;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class FreeMarkerHtmlFragmentTypeHandlerUTF8 extends DocTypeHandlerDecorator {

	public static final DocTypeHandler HANDLER = new FreeMarkerHtmlFragmentTypeHandlerUTF8();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerHtmlFragmentTypeHandlerUTF8() {
		super( FreeMarkerHtmlFragmentTypeHandler.HANDLER_UTF8 );
	}
	
}