package org.fugerit.java.doc.mod.itext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.rtf.RtfWriter2;

public class RtfTypeHandler extends DocTypeHandlerDefault {

	public static final DocTypeHandler HANDLER = new RtfTypeHandler();
	
	public RtfTypeHandler() {
		super( ITextDocHandler.DOC_OUTPUT_RTF );
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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RtfWriter2 rtfWriter2 = RtfWriter2.getInstance( document, baos );
		ITextDocHandler handler = new ITextDocHandler( document, rtfWriter2 );
		handler.handleDoc( docBase );
		baos.writeTo( outputStream );
		baos.close();
		outputStream.close();	
	}

}
