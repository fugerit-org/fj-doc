package org.fugerit.java.doc.freemarker.html;

import java.nio.charset.StandardCharsets;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDecorator;

public class FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8 extends DocTypeHandlerDecorator {

	public static final DocTypeHandler HANDLER = new FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8() {
		super( new FreeMarkerHtmlFragmentTypeHandler( StandardCharsets.UTF_8 ).withEscapeTextAsHtml( true ) );
		
	}
	
}