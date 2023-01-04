package test.org.fugerit.java.doc.base.xml;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.xml.DocXmlParser;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestDocXmlParser {

	private final static Logger logger = LoggerFactory.getLogger( TestDocXmlParser.class );
	
	private void test( String path, boolean valid, boolean exception ) {
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( path ) ) {
			DocXmlParser parser = new DocXmlParser();
			DocValidationResult result = parser.validateResult( new InputStreamReader( is ) );
			logger.info( "Validation result {}", result.isResultOk() );
			for ( String error : result.getErrorList() ) {
				logger.info( "Validation error {}", error );
			}
			Assert.assertEquals( "Validation result" , valid, result.isResultOk() );
		} catch (Exception e) {
			String message = "Error : "+e.getMessage();
			logger.error( message, e );
			fail( message );
		}
	}
	
	@Test
	public void testOk01() {
		this.test( "sample/doc_test_01.xml", true, false);
	}
	
	@Test
	public void testKo02() {
		this.test( "sample/doc_test_02_ko.xml", false, false);
	}
	
}
