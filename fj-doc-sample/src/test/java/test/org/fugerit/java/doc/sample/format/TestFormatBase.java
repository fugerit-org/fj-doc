package test.org.fugerit.java.doc.sample.format;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFormatBase {

	protected static final Logger logger = LoggerFactory.getLogger( TestFormatBase.class );
	
	protected File getFile( String baseFolder, String name ) throws IOException {
		File folder = new File( baseFolder );
		if ( !folder.exists() ) {
			logger.info( "mkdirs : {} -> {}", folder.getCanonicalPath(), folder.mkdirs() );
		}
		File file = new File( folder, name );
		logger.info( "Generating file : {}", file );
		return file;
	}
	
}
