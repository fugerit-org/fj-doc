package test.org.fugerit.java.doc.freemarker.process;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.sax.SAXParseResult;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigValidator;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestFreemarkerDocProcessConfigValidator {

	private static final String VALID_XML_PATH = "fj_doc_test/freemarker-doc-process.xml";
	
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
			SAXParseResult result = FreemarkerDocProcessConfigValidator.validate( reader );
			boolean ok = result.isPartialSuccess();
			FreemarkerDocProcessConfigValidator.logResult(result);
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
			SAXParseResult result = FreemarkerDocProcessConfigValidator.validateVersion( reader );
			boolean ok = result.isPartialSuccess();
			FreemarkerDocProcessConfigValidator.logResult(result);
			log.info( "Validation result {}", ok );
			Assert.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testValidateLoggerKo() {
		String fullPath = "fj_doc_test/freemarker-doc-process_ko.xml";
		log.info( "validate -> {}", fullPath );
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( fullPath );
				Reader reader = new InputStreamReader(is)) {
			boolean ok = FreemarkerDocProcessConfigValidator.logValidation(reader);
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
			boolean ok = FreemarkerDocProcessConfigValidator.logValidation(reader);
			log.info( "Validation result {}", ok );
			Assert.assertTrue( ok );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testValidateFail() {
		try ( Reader reader = new ReaderFail() ) {
			SAXParseResult result = FreemarkerDocProcessConfigValidator.validate( reader );
			fail( "Should not get here : "+result );
		} catch (Exception e) {
			Assert.assertTrue( e instanceof XMLException );
		}
	}

	@Test
	public void testValidateVersionFail() {
		try ( Reader reader = new ReaderFail() ) {
			SAXParseResult result = FreemarkerDocProcessConfigValidator.validateVersion( reader );
			fail( "Should not get here : "+result );
		} catch (Exception e) {
			Assert.assertTrue( e instanceof XMLException );
		}
	}

	@Test
	public void testValidateLoggerFail() {
		try ( Reader reader = new ReaderFail() ) {
			boolean result = FreemarkerDocProcessConfigValidator.logValidation( reader );
			fail( "Should not get here : "+result );
		} catch (Exception e) {
			Assert.assertTrue( e instanceof XMLException );
		}
	}
	
	@Test
	public void testGetXsdVersion() {
		try ( Reader reader = new ReaderFail() ) {
			String result = FreemarkerDocProcessConfigValidator.getXsdVersion(reader);
			fail( "Should not get here : "+result );
		} catch (Exception e) {
			Assert.assertTrue( e instanceof XMLException );
		}
	}
	
}
