package org.fugerit.java.doc.ent.servlet;

import java.io.OutputStream;

import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.mod.itext.ITextDocHandler;

public class EntDocFacade {

	public static void createXLS(DocBase docBase, OutputStream outputStream) throws Exception {
		throw new UnsupportedOperationException( "Not supported yet : createXLS()" );
	}

	public static void createPDF( DocBase docBase, OutputStream outputStream ) throws Exception {
		org.fugerit.java.doc.mod.itext.PdfTypeHandler.HANDLER.handle( DocInput.newInput( ITextDocHandler.DOC_OUTPUT_PDF , docBase) , DocOutput.newOutput( outputStream ) );	
	}		
	
	public static void createRTF( DocBase docBase, OutputStream outputStream ) throws Exception {
		org.fugerit.java.doc.mod.itext.RtfTypeHandler.HANDLER.handle( DocInput.newInput( ITextDocHandler.DOC_OUTPUT_RTF , docBase) , DocOutput.newOutput( outputStream ) );
	}	
	
	public static void createHTML( DocBase docBase, OutputStream outputStream ) throws Exception {
		org.fugerit.java.doc.mod.itext.HtmlTypeHandler.HANDLER.handle( DocInput.newInput( ITextDocHandler.DOC_OUTPUT_HTML , docBase) , DocOutput.newOutput( outputStream ) );		
	}	

}
