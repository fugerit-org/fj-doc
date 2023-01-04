package test.org.fugerit.java.doc.base.facade;

import static org.junit.Assert.fail;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDocFacade {

	public static final boolean VALID = true;
	public static final boolean NOT_VALID = false;
	
	public static final boolean NO_EXCEPTION = false;
	public static final boolean EXCEPTION = true;
	
	private static final Logger logger = LoggerFactory.getLogger( TestDocFacade.class );
	
	private void testValidateWorker( String path, boolean valid, boolean exception ) {
		try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( path ) ) ) {
			Properties params = new Properties();
			boolean validResult = DocFacade.validate( reader , params );
			Assert.assertEquals( "Validation result" , valid, validResult );
		} catch (Exception e) {
			String message = "Error : "+e.getMessage();
			logger.error( message, e );
			fail( message );
		}
	}
	
	@Test
	public void testOk01() {
		this.testValidateWorker( "sample/doc_test_01.xml", VALID, NO_EXCEPTION );
	}

	@Test
	public void testKo02() {
		this.testValidateWorker( "sample/doc_test_02_ko.xml", NOT_VALID, NO_EXCEPTION );
	}
	
}
