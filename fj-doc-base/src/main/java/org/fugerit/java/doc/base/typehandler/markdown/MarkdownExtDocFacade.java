package org.fugerit.java.doc.base.typehandler.markdown;

import java.io.PrintWriter;
import java.util.List;

import org.fugerit.java.doc.base.helper.DocTypeFacadeHelper;
import org.fugerit.java.doc.base.model.DocCell;
import org.fugerit.java.doc.base.model.DocContainer;
import org.fugerit.java.doc.base.model.DocElement;
import org.fugerit.java.doc.base.model.DocRow;
import org.fugerit.java.doc.base.model.DocTable;
import org.fugerit.java.doc.base.model.util.DocTableUtil;

/**
 * DocTypeFacade for extended markdown syntax
 * 
 * https://www.markdownguide.org/extended-syntax/
 * 
 * NOTE: all elements, including tables, are rendered in markdown format
 * 
 * @see MarkdownBasicDocFacade
 * 
 * @author Matteo a.k.a. Fugerit
 */
public class MarkdownExtDocFacade extends MarkdownBasicDocFacade {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 134234342334L;

	public MarkdownExtDocFacade(PrintWriter writer) {
		super(writer);
	}

	@Override
	public void handleTable(DocTable docTable, DocContainer parent, DocTypeFacadeHelper helper) throws Exception {
		this.getWriter().println();
		this.handleDocUtilTable( docTable, parent, helper );
	}
		
	protected void handleRowList( DocTable table, DocTableUtil tableUtil, List<DocElement> rowList, boolean header, DocTypeFacadeHelper helper  ) throws Exception {
		for ( DocElement element: rowList ) {
			DocRow row = (DocRow) element;
			for ( DocElement cellEl : row.getElementList() ) {
				DocCell cell = (DocCell) cellEl;
				this.getWriter().print( "| " );
				this.handleElements( cell, helper );
			}
			this.getWriter().println( " |" );
		}
		if ( header ) {
			for ( int k=0; k<table.getColumns(); k++ ) {
				this.getWriter().print( "| ---------------" );
			}
			this.getWriter().println( " |" );
		}
	}

}
