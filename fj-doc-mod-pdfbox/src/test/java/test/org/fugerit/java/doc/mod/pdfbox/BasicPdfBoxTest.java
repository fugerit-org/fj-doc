package test.org.fugerit.java.doc.mod.pdfbox;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicPdfBoxTest {

	protected static Logger logger = LoggerFactory.getLogger( BasicPdfBoxTest.class );
	
	public static final String BASIC_OUTPUT_FOLDER = "target/test_docs";
	
	protected File getBasicFolder() throws Exception {
		File file = new File( BASIC_OUTPUT_FOLDER );
		if ( !file.exists() ) {
			logger.info( "Create : {} -> {}", file.getCanonicalPath(), file.mkdirs() );
		}
		return file;
	}
	
	protected File getOutFile( String fileName ) throws Exception {
		return new File( getBasicFolder(), fileName );
	}
	
}
