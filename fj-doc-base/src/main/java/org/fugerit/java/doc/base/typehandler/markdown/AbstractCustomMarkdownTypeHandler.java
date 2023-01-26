package org.fugerit.java.doc.base.typehandler.markdown;

import java.nio.charset.Charset;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;

public abstract class AbstractCustomMarkdownTypeHandler extends DocTypeHandlerDefault {

	public static final String TYPE = DocConfig.TYPE_MD;
	
	public static final String MODULE = "markdown";
	
	public static final String MIME = "text/x-markdown";
	
	public AbstractCustomMarkdownTypeHandler( Charset charset, boolean printComments ) {
		super(TYPE, MODULE, MIME, charset);
		this.printComments = printComments;
	}
	
	public AbstractCustomMarkdownTypeHandler( Charset charset ) {
		this( charset, MarkdownBasicDocFacade.DEFAULT_PRINT_COMMENTS );
	}
	
	public AbstractCustomMarkdownTypeHandler( boolean printComments ) {
		this( null, printComments );
	}
	
	public AbstractCustomMarkdownTypeHandler() {
		this( MarkdownBasicDocFacade.DEFAULT_PRINT_COMMENTS );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -739451608L;

	private boolean printComments;

	public boolean isPrintComments() {
		return printComments;
	}

}