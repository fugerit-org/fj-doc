package test.org.fugerit.java.doc.base.xml;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.SimpleValue;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.result.Result;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.parser.DocParser;
import org.fugerit.java.doc.base.parser.DocValidationResult;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.fugerit.java.doc.base.xml.DocXmlParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

class TestDocXmlParser  {

	public static final boolean VALID = true;
	public static final boolean NOT_VALID = false;
	
	public static final boolean NO_EXCEPTION = false;
	public static final boolean EXCEPTION = true;
	
	private final static Logger logger = LoggerFactory.getLogger( TestDocXmlParser.class );
	
	@Test
	void testFillDocValidationResultHelper() {
		SAXParseResult result = new SAXParseResult();
		result.putWarning( new SAXException( "warning" ) );
		DocValidationResult docResult = new DocValidationResult( Result.RESULT_CODE_OK );
		DocXmlParser.fillDocValidationResultHelper(result, docResult);
		Assertions.assertFalse( docResult.getInfoList().isEmpty() );
	}
	
	private boolean validateWorker( String path, boolean valid, boolean exception ) {
		SimpleValue<Boolean> res = new SimpleValue<>( true );
		SafeFunction.apply( () -> {
			String fullPath = "sample/"+path+".xml";
			logger.info( "validate -> {}, valid : {}, exception : {}", fullPath, valid, exception );
			DocParser parser = new DocXmlParser();
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath ) ) {
				DocValidationResult result = parser.validateResult( new InputStreamReader( is ) );
				logger.info( "Validation result {}", result.isResultOk() );
				for ( String error : result.getErrorList() ) {
					logger.info( "Validation error {}", error );
				}
				for ( String error : result.getInfoList() ) {
					logger.info( "Validation info {}", error );
				}
				Assertions.assertEquals( valid, result.isResultOk() );
			}
		} );
		return res.getValue();
	}
	
	private boolean parseWorker( String path ) {
		SimpleValue<Boolean> res = new SimpleValue<>( true );
		SafeFunction.apply( () -> {
			DocTypeHandler handler = SimpleMarkdownExtTypeHandler.HANDLER;
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "sample/"+path+".xml" );
					FileOutputStream fos = new FileOutputStream( "target/"+path+"."+handler.getType() ) ) {
				DocXmlParser parser = new DocXmlParser();
				DocBase docBase = parser.parse(is);
				logger.info( "docBase -> {}", docBase );
				DocInput input = DocInput.newInput( handler.getType(), docBase, null );
				DocOutput output = DocOutput.newOutput( fos );
				handler.handle( input, output );
			}
		} );
		return res.getValue();
	}

	@Test
	void testParseCoverage01() {
		Assertions.assertTrue( this.parseWorker( "default_doc" ) );
	}
	
	@Test
	void testParseCoverage02() {
		Assertions.assertTrue( this.parseWorker( "default_doc_alt" ) );
	}

	@Test
	void testParseCoverage03() {
		Assertions.assertTrue( this.parseWorker( "default_doc_fail1" ) );
	}
	
	@Test
	void testParse01() {
		Assertions.assertTrue( this.parseWorker( "doc_test_01" ) );
	}
	
	@Test
	void testValidateOk01() {
		Assertions.assertTrue( this.validateWorker( "doc_test_01", VALID, NO_EXCEPTION) );
	}
	
	@Test
	void testValidateKo02() {
		Assertions.assertTrue(  this.validateWorker( "doc_test_02_ko", NOT_VALID, NO_EXCEPTION) );
	}
	
	@Test
	void parseVersion01() {
		SimpleValue<Boolean> res = new SimpleValue<>( false );
		SafeFunction.apply( () -> {
			String fullPath = "cl://sample/doc_test_01.xml";
			logger.info( "validate -> {}", fullPath );
			DocParser parser = new DocXmlParser();
			try ( Reader reader = StreamHelper.resolveReader( fullPath ) ) {
				int pr = parser.validateVersion( reader );
				res.setValue( Result.RESULT_CODE_OK == pr );
			}
		} );
		Assertions.assertTrue( res.getValue().booleanValue() );
	}
	
	@Test
	void parseFail() {
		boolean failOnElementNotFound = true;
		Assertions.assertThrows( DocException.class , () -> {
			String fullPath = "cl://coverage/xml/default_doc_fail2.xml";
			logger.info( "validate -> {}", fullPath );
			DocParser parser = new DocXmlParser( failOnElementNotFound );
			try ( Reader reader = StreamHelper.resolveReader( fullPath ) ) {
				parser.parse(reader);
			}
		} );
	}
	
}
