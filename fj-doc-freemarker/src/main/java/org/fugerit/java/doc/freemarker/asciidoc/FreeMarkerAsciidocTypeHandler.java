package org.fugerit.java.doc.freemarker.asciidoc;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.helper.FreeMarkerDocHelperTypeHandler;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FreeMarkerAsciidocTypeHandler extends FreeMarkerDocHelperTypeHandler {

	public static final DocTypeHandler HANDLER = new FreeMarkerAsciidocTypeHandler();

	public static final DocTypeHandler HANDLER_UTF8 = new FreeMarkerAsciidocTypeHandler(StandardCharsets.UTF_8);

	public static final String CHAIN_FREEMARKER = "asciidoc";

	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public FreeMarkerAsciidocTypeHandler() {
		super(DocConfig.TYPE_ADOC, CHAIN_FREEMARKER);
	}

	public FreeMarkerAsciidocTypeHandler(Charset charset) {
		super(DocConfig.TYPE_ADOC, charset, CHAIN_FREEMARKER);
	}

}