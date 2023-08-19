package org.fugerit.java.doc.freemarker.html;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocHelperTypeHandler;

public class FreeMarkerHtmlFragmentTypeHandler extends FreeMarkerDocHelperTypeHandler {

	public static final DocTypeHandler HANDLER = new FreeMarkerHtmlFragmentTypeHandler();
	
	public static final DocTypeHandler HANDLER_UTF8 = new FreeMarkerHtmlFragmentTypeHandler( StandardCharsets.UTF_8 );
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -739451708L;

	public FreeMarkerHtmlFragmentTypeHandler() {
		super( DocConfig.TYPE_HTML_FRAGMENT );
	}

	public FreeMarkerHtmlFragmentTypeHandler( Charset charset) {
		super( DocConfig.TYPE_HTML_FRAGMENT, charset );
	}
	
}