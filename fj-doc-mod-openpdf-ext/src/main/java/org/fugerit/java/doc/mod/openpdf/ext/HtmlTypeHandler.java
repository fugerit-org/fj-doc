package org.fugerit.java.doc.mod.openpdf.ext;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPDFConfigHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;

import com.lowagie.text.Document;
import com.lowagie.text.html.HtmlWriter;
import org.w3c.dom.Element;

public class HtmlTypeHandler extends DocTypeHandlerDefault {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3491991891783269389L;
	
	public static final DocTypeHandler HANDLER = new HtmlTypeHandler();
	
	public HtmlTypeHandler() {
		super( OpenPpfDocHandler.DOC_OUTPUT_HTML, OpenPpfDocHandler.MODULE );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		DocBase docBase = docInput.getDoc();
		OutputStream outputStream = docOutput.getOs();
		Document document = new Document( );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		HtmlWriter.getInstance( document, baos );
		OpenPpfDocHandler handler = new OpenPpfDocHandler( document, OpenPpfDocHandler.DOC_OUTPUT_HTML );
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
