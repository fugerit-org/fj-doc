package test.org.fugerit.java.doc.base.xml;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.base.xml.DocValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.fugerit.java.test.helper.core.io.ReaderFail;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocValidator {

	private static final String VALID_XML_PATH = "sample/doc_test_01.xml";
	
	private void failEx( Exception e ) {
		String message = "Error : "+e.getMessage();
		log.error( message, e );
		fail( message );
	}
	
	@Test
	void testValidate() {
		String fullPath = VALID_XML_PATH;
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			SAXParseResult result = DocValidator.validate( reader );
			boolean ok = result.isPartialSuccess();
			DocValidator.logResult(result, log);
			log.info( "Validation result {}", ok );
			Assertions.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	void testValidateVersion() {
		String fullPath = VALID_XML_PATH;
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			SAXParseResult result = DocValidator.validateVersion( reader );
			boolean ok = result.isPartialSuccess();
			DocValidator.logResult(result, log);
			log.info( "Validation result {}", ok );
			Assertions.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	void testValidateLoggerKo() {
		String fullPath = "sample/doc_test_02_ko.xml";
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			boolean ok = DocValidator.logValidation(reader, log);
			log.info( "Validation result {}", ok );
			Assertions.assertTrue( !ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	void testValidateLoggerOk() {
		String fullPath = VALID_XML_PATH;
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			boolean ok = DocValidator.logValidation(reader, log);
			log.info( "Validation result {}", ok );
			Assertions.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	void testValidateFail() {
		Assertions.assertThrows( XMLException.class, () -> {
			try ( Reader reader = new ReaderFail() ) {
				SAXParseResult result = DocValidator.validate( reader );
				fail( "Should not get here : "+result );
			}
		} );
	}

	@Test
	void testValidateVersionFail() {
		Assertions.assertThrows( XMLException.class, () -> {
			try ( Reader reader = new ReaderFail() ) {
				SAXParseResult result = DocValidator.validateVersion( reader );
				fail( "Should not get here : "+result );
			}
		} );
	}

	@Test
	void testValidateLoggerFail() {
		Assertions.assertThrows( XMLException.class, () -> {
			try ( Reader reader = new ReaderFail() ) {
				boolean result = DocValidator.logValidation( reader, log );
				fail( "Should not get here : "+result );
			}
		} );
	}
	
	@Test
	void testGetXsdVersion() {
		Assertions.assertThrows( XMLException.class, () -> {
			try ( Reader reader = new ReaderFail() ) {
				String result = DocValidator.getXsdVersion(reader);
				fail( "Should not get here : "+result );
			}
		} );
	}
		
}
