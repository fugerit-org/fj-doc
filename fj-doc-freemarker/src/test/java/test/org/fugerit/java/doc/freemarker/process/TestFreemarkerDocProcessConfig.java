package test.org.fugerit.java.doc.freemarker.process;

import static org.junit.Assert.fail;

import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestFreemarkerDocProcessConfig {

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
	
}
