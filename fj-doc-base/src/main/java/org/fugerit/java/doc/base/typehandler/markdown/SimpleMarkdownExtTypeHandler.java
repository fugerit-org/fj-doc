package org.fugerit.java.doc.base.typehandler.markdown;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.model.DocBase;

/**
 * DocTypeHandler for markdown extended syntax : 
 * 
 * https://www.markdownguide.org/extended-syntax/
 * 
 * @see MarkdownExtDocFacade
 * 
 * @author Matteo a.k.a. Fugerit
 *
 */
public class SimpleMarkdownExtTypeHandler extends AbstractCustomMarkdownTypeHandler {

	public static final DocTypeHandler HANDLER = new SimpleMarkdownExtTypeHandler();
	
	public static final DocTypeHandler HANDLER_NOCOMMENTS = new SimpleMarkdownExtTypeHandler( false );
	
	public static final DocTypeHandler HANDLER_UTF8 = new SimpleMarkdownExtTypeHandler( StandardCharsets.UTF_8 );
	
	public static final DocTypeHandler HANDLER_NOCOMMENTS_UTF8 = new SimpleMarkdownExtTypeHandler( StandardCharsets.UTF_8, false );
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394541608L;

	public SimpleMarkdownExtTypeHandler() {
		super();
	}

	public SimpleMarkdownExtTypeHandler(boolean printComments) {
		super(printComments);
	}

	public SimpleMarkdownExtTypeHandler(Charset charset, boolean printComments) {
		super(charset, printComments);
	}

	public SimpleMarkdownExtTypeHandler(Charset charset) {
		super(charset);
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		PrintWriter writer = new PrintWriter( new OutputStreamWriter( docOutput.getOs(), this.getCharset() ) );
		DocBase docBase = docInput.getDoc();
		/*
		 * The key for building a DocTypeHandler is to correctly renders 
		 * the DocBase model in the desired output (DocPara, DocTable, DocList etc).
		 * Here we created a facade to do so : 
		 */
		MarkdownExtDocFacade facade = new MarkdownExtDocFacade( writer, this.isPrintComments() );
		facade.handleDoc( docBase );
	}

}