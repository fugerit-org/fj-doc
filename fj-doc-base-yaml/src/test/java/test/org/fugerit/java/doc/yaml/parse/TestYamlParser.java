package test.org.fugerit.java.doc.yaml.parse;

import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.doc.yaml.parse.DocYamlParser;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestYamlParser {

	public static final boolean VALID = true;
	public static final boolean NOT_VALID = false;
	
	public static final boolean NO_EXCEPTION = false;
	public static final boolean EXCEPTION = true;
	
	private static final Logger logger = LoggerFactory.getLogger( TestYamlParser.class );
	
	private void validateWorker( String path, boolean valid, boolean exception ) {
		String fullPath = "sample/"+path+".yaml";
		logger.info( "validate -> {}", fullPath );
		DocParser parser = new DocYamlParser();
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath ) ) {
			DocValidationResult result = parser.validateResult( new InputStreamReader( is ) );
			logger.info( "Validation result {}", result.isResultOk() );
			for ( String error : result.getErrorList() ) {
				logger.info( "Validation error {}", error );
			}
			for ( String error : result.getInfoList() ) {
				logger.info( "Validation info {}", error );
			}
			Assert.assertEquals( "Validation result" , valid, result.isResultOk() );
		} catch (Exception e) {
			String message = "Error : "+e.getMessage();
			logger.error( message, e );
			fail( message );
		}
	}
	
	private void parserWorker( String path ) {
		DocTypeHandler handler = SimpleMarkdownExtTypeHandler.HANDLER;
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".yaml" );
				FileOutputStream fos = new FileOutputStream( "target/"+path+"."+handler.getType() )) {
			DocYamlParser parser = new DocYamlParser();
			DocBase docBase = parser.parse(is);
			logger.info( "docBase -> {}", docBase );
			DocInput input = DocInput.newInput( handler.getType(), docBase, null );
			DocOutput output = DocOutput.newOutput( fos );
			handler.handle( input, output );
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message ) ;
		}
	}
	
	@Test
	public void test01() {
		this.parserWorker( "doc_test_01" );
	}
	
	@Test
	public void testValidateOk01() {
		this.validateWorker( "doc_test_01", VALID, NO_EXCEPTION );
	}
	
	@Test
	public void testValidateKo02() {
		this.validateWorker( "doc_test_02_ko", NOT_VALID, NO_EXCEPTION );
	}
	
}
