package test.org.fugerit.java.doc.freemarker.process;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigValidator;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.org.fugerit.java.helpers.io.ReaderFail;

@Slf4j
class TestFreemarkerDocProcessConfigValidator {

	private static final String VALID_XML_PATH = "fj_doc_test/freemarker-doc-process.xml";
	
	private void failEx( Exception e ) {
		String message = "Error : "+e.getMessage();
		log.error( message, e );
		Assertions.fail( message );
	}
	
	@Test
	void testValidate() {
		String fullPath = VALID_XML_PATH;
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			SAXParseResult result = FreemarkerDocProcessConfigValidator.validate( reader );
			boolean ok = result.isPartialSuccess();
			FreemarkerDocProcessConfigValidator.logResult(result);
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
			SAXParseResult result = FreemarkerDocProcessConfigValidator.validateVersion( reader );
			boolean ok = result.isPartialSuccess();
			FreemarkerDocProcessConfigValidator.logResult(result);
			log.info( "Validation result {}", ok );
			Assertions.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}

	@Test
	void testValidateLoggerKo() {
		String fullPath = "fj_doc_test/freemarker-doc-process_ko.xml";
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			boolean ok = FreemarkerDocProcessConfigValidator.logValidation(reader);
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
			boolean ok = FreemarkerDocProcessConfigValidator.logValidation(reader);
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
				SAXParseResult result = FreemarkerDocProcessConfigValidator.validate( reader );
				Assertions.fail( "Should not get here : "+result );
			}
		} );
	}

	@Test
	void testValidateVersionFail() {
		Assertions.assertThrows( XMLException.class, () -> {
			try ( Reader reader = new ReaderFail() ) {
				SAXParseResult result = FreemarkerDocProcessConfigValidator.validateVersion( reader );
				Assertions.fail( "Should not get here : "+result );
			}
		} );
	}

	@Test
	void testValidateLoggerFail() {
		Assertions.assertThrows( XMLException.class, () -> {
			try ( Reader reader = new ReaderFail() ) {
				boolean result = FreemarkerDocProcessConfigValidator.logValidation( reader );
				Assertions.fail( "Should not get here : "+result );
			}
		} );
	}
	
	@Test
	void testGetXsdVersion() {
		Assertions.assertThrows( XMLException.class, () -> {
			try ( Reader reader = new ReaderFail() ) {
				String result = FreemarkerDocProcessConfigValidator.getXsdVersion(reader);
				Assertions.fail( "Should not get here : "+result );
			}
		} );
	}
	
}
