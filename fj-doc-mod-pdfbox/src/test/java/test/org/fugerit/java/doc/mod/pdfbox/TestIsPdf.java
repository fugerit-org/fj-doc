package test.org.fugerit.java.doc.mod.pdfbox;

import java.io.FileInputStream;
import java.io.InputStream;

import org.fugerit.java.doc.mod.pdfbox.utils.PdfCheckUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestIsPdf {

	private final static Logger logger = LoggerFactory.getLogger( TestIsPdf.class );
	
	public static void main( String[] args ) {
		String path = null;
		if ( path == null ) {
			path = args[0];
		}
		try ( InputStream is = new FileInputStream( path ) ) {
			boolean isPdf = PdfCheckUtils.isPdf( is );
			logger.info( "file {} is pdf -> {}", path, isPdf );
			System.out.println( isPdf );
		} catch (Exception e) {
			logger.error( "failed check on path -> "+path , e ); 
		}
	}
	
}
