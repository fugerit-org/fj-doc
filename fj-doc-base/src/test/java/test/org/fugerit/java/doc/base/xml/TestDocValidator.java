package test.org.fugerit.java.doc.base.xml;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.base.xml.DocValidator;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.helpers.io.ReaderFail;

@Slf4j
public class TestDocValidator {

	private static final String VALID_XML_PATH = "sample/doc_test_01.xml";
	
	private void failEx( Exception e ) {
		String message = "Error : "+e.getMessage();
		log.error( message, e );
		fail( message );
	}
	
	@Test
	public void testValidate() {
		String fullPath = VALID_XML_PATH;
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			SAXParseResult result = DocValidator.validate( reader );
			boolean ok = result.isPartialSuccess();
			DocValidator.logResult(result, log);
			log.info( "Validation result {}", ok );
			Assert.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testValidateVersion() {
		String fullPath = VALID_XML_PATH;
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			SAXParseResult result = DocValidator.validateVersion( reader );
			boolean ok = result.isPartialSuccess();
			DocValidator.logResult(result, log);
			log.info( "Validation result {}", ok );
			Assert.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testValidateLoggerKo() {
		String fullPath = "sample/doc_test_02_ko.xml";
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			boolean ok = DocValidator.logValidation(reader, log);
			log.info( "Validation result {}", ok );
			Assert.assertTrue( !ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testValidateLoggerOk() {
		String fullPath = VALID_XML_PATH;
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			boolean ok = DocValidator.logValidation(reader, log);
			log.info( "Validation result {}", ok );
			Assert.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testValidateFail() {
		Assert.assertThrows( XMLException.class, () -> {
			try ( Reader reader = new ReaderFail() ) {
				SAXParseResult result = DocValidator.validate( reader );
				fail( "Should not get here : "+result );
			}
		} );
	}

	@Test
	public void testValidateVersionFail() {
		try ( Reader reader = new ReaderFail() ) {
			SAXParseResult result = DocValidator.validateVersion( reader );
			fail( "Should not get here : "+result );
		} catch (Exception e) {
			Assert.assertTrue( e instanceof XMLException );
		}
	}

	@Test
	public void testValidateLoggerFail() {
		try ( Reader reader = new ReaderFail() ) {
			boolean result = DocValidator.logValidation( reader, log );
			fail( "Should not get here : "+result );
		} catch (Exception e) {
			Assert.assertTrue( e instanceof XMLException );
		}
	}
	
	@Test
	public void testGetXsdVersion() {
		try ( Reader reader = new ReaderFail() ) {
			String result = DocValidator.getXsdVersion(reader);
			fail( "Should not get here : "+result );
		} catch (Exception e) {
			Assert.assertTrue( e instanceof XMLException );
		}
	}
		
}
