package org.fugerit.java.doc.mod.openpdf.ext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPDFConfigHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.w3c.dom.Element;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

public class PdfTypeHandler extends DocTypeHandlerDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5459938865782356227L;
	
	public static final DocTypeHandler HANDLER = new PdfTypeHandler();
	
	public PdfTypeHandler() {
		super( DocConfig.TYPE_PDF, OpenPpfDocHandler.MODULE );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		DocBase docBase = docInput.getDoc();
		OutputStream outputStream = docOutput.getOs();
		String[] margins = docBase.getInfo().getProperty( "margins", "20;20;20;20" ).split( ";" );
		Document document = new Document( PageSize.A4, Integer.parseInt( margins[0] ),
				Integer.parseInt( margins[1] ),
				Integer.parseInt( margins[2] ), 
				Integer.parseInt( margins[3] ) );
		// allocate buffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// create pdf writer
		PdfWriter pdfWriter = PdfWriter.getInstance( document, baos );
		// create doc handler
		OpenPpfDocHandler handler = new OpenPpfDocHandler( document, pdfWriter );
		if ( "true".equalsIgnoreCase( docBase.getInfo().getProperty( "set-total-page" ) ) ) {
			handler.handleDoc( docBase );
			int totalPageCount = pdfWriter.getCurrentPageNumber()-1;
			document = new Document( PageSize.A4, Integer.parseInt( margins[0] ),
					Integer.parseInt( margins[1] ),
					Integer.parseInt( margins[2] ), 
					Integer.parseInt( margins[3] ) );
			baos = new ByteArrayOutputStream();
			pdfWriter = PdfWriter.getInstance( document, baos );
			handler = new OpenPpfDocHandler(document, pdfWriter, totalPageCount );
		}
		handler.handleDoc( docBase );
		baos.writeTo( outputStream );
		baos.close();
		outputStream.close();	
	}

	@Override
	protected void handleConfigTag(Element config) throws ConfigException {
		super.handleConfigTag(config);
		OpenPDFConfigHelper.handleConfig( config, this.getType() );
	}
}
