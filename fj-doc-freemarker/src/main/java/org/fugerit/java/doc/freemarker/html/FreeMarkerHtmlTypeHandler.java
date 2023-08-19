package org.fugerit.java.doc.freemarker.html;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocHelperTypeHandler;

public class FreeMarkerHtmlTypeHandler extends FreeMarkerDocHelperTypeHandler {

	public static final DocTypeHandler HANDLER = new FreeMarkerHtmlTypeHandler();

	public static final DocTypeHandler HANDLER_UTF8 = new FreeMarkerHtmlTypeHandler(StandardCharsets.UTF_8);

	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerHtmlTypeHandler() {
		super(DocConfig.TYPE_HTML);
	}

	public FreeMarkerHtmlTypeHandler(Charset charset) {
		super(DocConfig.TYPE_HTML, charset);
	}

}