package org.fugerit.java.doc.mod.fop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;

public class PdfFopTypeHandler extends FreeMarkerFopTypeHandler {

	public static DocTypeHandler HANDLER = new PdfFopTypeHandler();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7394516771708L;

	public PdfFopTypeHandler() {
		super( DocConfig.TYPE_PDF );
	}

	@Override
	public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DocOutput bufferOutput = DocOutput.newOutput( buffer );
		super.handle(docInput, bufferOutput);
		// the XML file which provides the input
		StreamSource xmlSource = new StreamSource( new InputStreamReader( new ByteArrayInputStream( buffer.toByteArray() ) ) );
		// create an instance of fop factory
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, docOutput.getOs());
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		Result res = new SAXResult(fop.getDefaultHandler());
		transformer.transform(xmlSource, res);
	}
	
}