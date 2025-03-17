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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

class TestJsonParser {

	static final boolean VALID = true;
	static final boolean NOT_VALID = false;
	
	static final boolean NO_EXCEPTION = false;
	static final boolean EXCEPTION = true;
	
	private static final Logger logger = LoggerFactory.getLogger( TestJsonParser.class );
	
	private boolean validateWorker( String path, boolean valid, boolean exception ) {
		return this.validateWorker(path, valid, exception, false);
	}
	
	private boolean validateWorker( String path, boolean valid, boolean exception, boolean parseVersion ) {
		String fullPath = "sample/"+path+".json";
		logger.info( "validate -> {}, valid : {}, exception : {}, parseVersion : {}", fullPath, valid, exception, parseVersion );
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
				Assertions.assertEquals( valid, result.isResultOk() );
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
	void testParse01() {
		Assertions.assertTrue( this.parseWorker( "doc_test_01" ) );
	}

	@Test
	void testParse02() {
		Assertions.assertTrue( this.parseWorker( "doc_test_02" ) );
	}
	
	@Test
	void testValidateOk01() {
		Assertions.assertTrue( this.validateWorker( "doc_test_01", VALID, NO_EXCEPTION ) );
	}
	
	@Test
	void testValidateOk01ParseVersion() {
		Assertions.assertTrue( this.validateWorker( "doc_test_01", VALID, NO_EXCEPTION, true ) );
	}
	
	@Test
	void testValidateOk02ParseVersion() {
		Assertions.assertTrue( this.validateWorker( "doc_test_02", VALID, NO_EXCEPTION, true ) );
	}

	@Test
	void testValidateKo02() {
		Assertions.assertFalse( this.validateWorker( "doc_test_02_ko", NOT_VALID, NO_EXCEPTION ) );
	}

	@Test
	void testFacade() {
		Assertions.assertNotNull(
			SafeFunction.get( () -> {
				try ( InputStreamReader reader = new InputStreamReader(
						ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_01.json" ) ) ) {
					 return DocJsonFacade.parse( reader );
				}
			} )
		);
	}
	
	@Test
	void testHelper() {
		Assertions.assertTrue( DocObjectMapperHelper.isSpecialProperty( DocObjectMapperHelper.PROPERTY_TAG ) );
		Assertions.assertFalse( DocObjectMapperHelper.isSpecialProperty( "aaa" ) );
		Assertions.assertNotNull( new DocObjectMapperHelper( new ObjectMapper() ) );
	}
	
}
