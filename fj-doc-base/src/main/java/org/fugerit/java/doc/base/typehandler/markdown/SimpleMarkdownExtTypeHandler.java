package org.fugerit.java.doc.base.typehandler.markdown;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

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
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394541608L;

	public SimpleMarkdownExtTypeHandler() {
		super();
	}

	public SimpleMarkdownExtTypeHandler(boolean printComments) {
		super(printComments);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		PrintWriter writer = new PrintWriter( new OutputStreamWriter( docOutput.getOs() ) );
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