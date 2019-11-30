package org.fugerit.java.doc.mod.pdfbox;

import java.io.OutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;

public class PdfBoxTypeHandler extends DocTypeHandlerDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7188424476250673707L;
	
	public static final String MODULE = "box";
	
	public static DocTypeHandler HANDLER = new PdfBoxTypeHandler();
	
	public PdfBoxTypeHandler() {
		super( PdfBoxDocHandler.DOC_OUTPUT_PDF, MODULE );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		DocBase docBase = docInput.getDoc();
		OutputStream outputStream = docOutput.getOs();
        try (PDDocument doc = new PDDocument(); ) {
            try ( PdfBoxDocHandler handler = new PdfBoxDocHandler( doc ) ) {
            	handler.handleDoc( docBase );
            }
            doc.save( outputStream );
        }
	}

}
