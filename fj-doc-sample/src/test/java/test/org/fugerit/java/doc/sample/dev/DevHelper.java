package test.org.fugerit.java.doc.sample.dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
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
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.mod.fop.FopConfigDefault;
import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;

public class DevHelper {

	protected boolean workerFoToPdf( File inputPath,  File outputPath ) throws Exception {
		boolean ok = false;
		try ( OutputStream output = new FileOutputStream( outputPath ) ) {
			FopFactory fopFactory = FopConfigDefault.DEFAULT.newFactory();
		    FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
			foUserAgent.setAccessibility( PdfFopTypeHandler.DEFAULT_ACCESSIBILITY );
			foUserAgent.setKeepEmptyTags( PdfFopTypeHandler.DEFAULT_KEEP_EMPTY_TAGS );
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, output);
		    // Step 4: Setup JAXP using identity transformer
		    TransformerFactory factory = TransformerFactory.newInstance();
		    Transformer transformer = factory.newTransformer(); // identity transformer
		    // Step 5: Setup input and output for XSLT transformation
		    // Setup input stream
		    Source src = new StreamSource( inputPath );
		    // Resulting SAX events (the generated FO) must be piped through to FOP
		    Result res = new SAXResult(fop.getDefaultHandler());
		    // Step 6: Start XSLT transformation and FOP processing
		    transformer.transform(src, res);
		    ok = true;
		}
		return ok;
	}
	
	protected boolean workerXmlToFo( File inputPath,  File outputPath ) throws Exception {
		boolean ok = false;
		try ( InputStream input = new FileInputStream( inputPath );
				OutputStream output = new FileOutputStream( outputPath ) ) {
			DocBase doc = DocFacade.parse( input );
			FreeMarkerFopTypeHandler.HANDLER.handle( DocInput.newInput( DocConfig.TYPE_FO , doc) , DocOutput.newOutput( output ) );
		    ok = true;
		}
		return ok;
	}
	
	protected boolean workerXmlToFoToPdf( File inputPath,  File outputFo, File outputPdf ) throws Exception {
		boolean ok = this.workerXmlToFo( inputPath , outputFo );
		ok = this.workerFoToPdf( outputFo, outputPdf );
		return ok;
	}
	
}
