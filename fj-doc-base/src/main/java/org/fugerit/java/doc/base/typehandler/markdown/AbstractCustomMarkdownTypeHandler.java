package org.fugerit.java.doc.base.typehandler.markdown;

import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;

public abstract class AbstractCustomMarkdownTypeHandler extends DocTypeHandlerDefault {

	public static final String TYPE = "md";
	
	public static final String MODULE = "markdown";
	
	public static final String MIME = "text/x-markdown";
	
	public AbstractCustomMarkdownTypeHandler() {
		super(TYPE, MODULE, MIME);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -739451608L;

}