package org.fugerit.java.doc.freemarker.html;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocHelperTypeHandler;

public class FreeMarkerHtmlTypeHandler extends FreeMarkerDocHelperTypeHandler {

	public static DocTypeHandler HANDLER = new FreeMarkerHtmlTypeHandler();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerHtmlTypeHandler() {
		super( DocConfig.TYPE_HTML );
	}
	
}