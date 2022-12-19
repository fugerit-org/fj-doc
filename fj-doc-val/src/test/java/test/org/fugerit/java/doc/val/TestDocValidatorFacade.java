package test.org.fugerit.java.doc.val;

import static org.junit.Assert.fail;

import java.io.InputStream;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDocValidatorFacade {

	private static final Logger logger = LoggerFactory.getLogger( TestDocValidatorFacade.class );
	
	private static final String BASE_PATH = "sample";
	
	protected void worker( DocValidatorFacade facade, String fileName, boolean result ) {
		String path = BASE_PATH+"/"+fileName;
		logger.info( "test path {}, expected result {}", path, result );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( path ) ) {
			boolean check = facade.check(fileName, is);
			Assert.assertEquals( "File check failed", result, check );
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message );
		}
	}

	
}
