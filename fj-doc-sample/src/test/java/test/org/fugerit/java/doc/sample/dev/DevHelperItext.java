package test.org.fugerit.java.doc.sample.dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.xml.DocValidator;
import org.fugerit.java.doc.mod.itext.PdfTypeHandler;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class DevHelperItext {

	private final static Logger logger = LoggerFactory.getLogger( DevHelperItext.class );
	
	@BeforeClass
	public static void initPath() {
		File baseOut = new File( BasicFacadeTest.BASIC_OUTPUT_PATH );
		if ( !baseOut.exists() ) {
			logger.info( "create base dir -> {} -> {}", baseOut.getAbsolutePath(), baseOut.mkdirs() );
		}
	}
	protected boolean workerItext( File inputPath,  File outputPath ) throws Exception {
		boolean ok = false;
		try ( InputStream input = new FileInputStream( inputPath );
				OutputStream output = new FileOutputStream( outputPath );
				Reader xmlValidationReader = new FileReader( inputPath ) ) {
			boolean valid = DocValidator.logValidation( xmlValidationReader , logger );
			logger.info( "validation result -> {}", valid );
			DocBase doc = DocFacade.parse( input );
			PdfTypeHandler.HANDLER.handle( DocInput.newInput( DocConfig.TYPE_PDF , doc) , DocOutput.newOutput( output ) );
		    ok = valid;
		}
		return ok;
	}
	
}
