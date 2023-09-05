package test.org.fugerit.java.doc.freemarker.process;

import static org.junit.Assert.fail;

import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigValidator;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.BasicTest;

@Slf4j
public class TestFreemarkerDocProcessConfig extends BasicTest {

	@Test
	public void testConfigRead001() {
		try ( Reader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "fj_doc_test/freemarker-doc-process.xml" ) ) ) {
			FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.loadConfig(xmlReader);
			log.info( "config {}", config.getChain( "sample_chain" ) );
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail(message);
		}
	}
	
	@Test
	public void testConfigValidate001() {
		try ( Reader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "fj_doc_test/freemarker-doc-process.xml" ) ) ) {
			FreemarkerDocProcessConfigValidator.logValidation( xmlReader );
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail(message);
		}
	}
	
	@Test
	public void testNewSimpleConfig() {
		try {
			Assert.assertNotNull( FreemarkerDocProcessConfigFacade.newSimpleConfig( "simple-config-001",  "/template").getChainCache( "test" ) );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testNewSimpleConfigVersion() {
		try {
			Assert.assertNotNull( FreemarkerDocProcessConfigFacade.newSimpleConfig( "simple-config-002",  "/template", FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_31 ).getChainCache( "test" ) );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
}
