package org.fugerit.java.doc.freemarker.html;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocHelperTypeHandler;

public class FreeMarkerHtmlFragmentTypeHandler extends FreeMarkerDocHelperTypeHandler {

	public static final String CHAIN_FREEMARKER = "html-fragment-freemarker";
	
	public static DocTypeHandler HANDLER = new FreeMarkerHtmlFragmentTypeHandler();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -739451708L;

	public FreeMarkerHtmlFragmentTypeHandler() {
		super( DocConfig.TYPE_HTML_FRAGMENT, CHAIN_FREEMARKER );
	}

}