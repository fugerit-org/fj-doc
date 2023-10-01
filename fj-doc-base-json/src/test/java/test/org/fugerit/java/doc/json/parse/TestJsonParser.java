package test.org.fugerit.java.doc.json.parse;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.doc.json.parse.DocJsonFacade;
import org.fugerit.java.doc.json.parse.DocJsonParser;
import org.fugerit.java.doc.json.parse.DocObjectMapperHelper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJsonParser {

	public static final boolean VALID = true;
	public static final boolean NOT_VALID = false;
	
	public static final boolean NO_EXCEPTION = false;
	public static final boolean EXCEPTION = true;
	
	private static final Logger logger = LoggerFactory.getLogger( TestJsonParser.class );
	
	private boolean validateWorker( String path, boolean valid, boolean exception ) {
		return this.validateWorker(path, valid, exception, false);
	}
	
	private boolean validateWorker( String path, boolean valid, boolean exception, boolean parseVersion ) {
		String fullPath = "sample/"+path+".json";
		logger.info( "validate -> {}", fullPath );
		DocParser parser = new DocJsonParser();
		return SafeFunction.get( () -> {
			try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( fullPath ) ) ) {
				DocValidationResult result =  parseVersion ? parser.validateVersionResult( reader ) : parser.validateResult( reader );
				logger.info( "Validation result {}", result.isResultOk() );
				for ( String error : result.getErrorList() ) {
					logger.info( "Validation error {}", error );
				}
				for ( String error : result.getInfoList() ) {
					logger.info( "Validation info {}", error );
				}
				Assert.assertEquals( "Validation result" , valid, result.isResultOk() );
				return result.isResultOk();
			}
		} );
	}
	
	private boolean parseWorker( String path ) {
		return SafeFunction.get( () -> {
			DocTypeHandler handler = SimpleMarkdownExtTypeHandler.HANDLER;
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".json" );
					FileOutputStream fos = new FileOutputStream( "target/"+path+"."+handler.getType() ) ) {
				DocJsonParser parser = new DocJsonParser();
				DocBase docBase = parser.parse(is);
				logger.info( "docBase -> {}", docBase );
				DocInput input = DocInput.newInput( handler.getType(), docBase, null );
				DocOutput output = DocOutput.newOutput( fos );
				handler.handle( input, output );
				return VALID;
			}
		} );
	}
	
	@Test
	public void testParse01() {
		Assert.assertTrue( this.parseWorker( "doc_test_01" ) );
	}

	@Test
	public void testParse02() {
		Assert.assertTrue( this.parseWorker( "doc_test_02" ) );
	}
	
	@Test
	public void testValidateOk01() {
		Assert.assertTrue( this.validateWorker( "doc_test_01", VALID, NO_EXCEPTION ) );
	}
	
	@Test
	public void testValidateOk01ParseVersion() {
		Assert.assertTrue( this.validateWorker( "doc_test_01", VALID, NO_EXCEPTION, true ) );
	}
	
	@Test
	public void testValidateOk02ParseVersion() {
		Assert.assertTrue( this.validateWorker( "doc_test_02", VALID, NO_EXCEPTION, true ) );
	}

	@Test
	public void testValidateKo02() {
		Assert.assertFalse( this.validateWorker( "doc_test_02_ko", NOT_VALID, NO_EXCEPTION ) );
	}

	@Test
	public void testFacade() {
		SafeFunction.apply( () -> {
			try ( InputStreamReader reader = new InputStreamReader( 
					ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_01.json" ) ) ) {
				Assert.assertNotNull( DocJsonFacade.parse( reader ) );
			}
		} );
	}
	
	@Test
	public void testHelper() {
		Assert.assertTrue( DocObjectMapperHelper.isSpecialProperty( DocObjectMapperHelper.PROPERTY_TAG ) );
		Assert.assertFalse( DocObjectMapperHelper.isSpecialProperty( "aaa" ) );
		Assert.assertNotNull( new DocObjectMapperHelper( new ObjectMapper() ) );
	}
	
}
