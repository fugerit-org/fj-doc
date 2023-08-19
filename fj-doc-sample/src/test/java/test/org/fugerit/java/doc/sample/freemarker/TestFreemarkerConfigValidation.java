package test.org.fugerit.java.doc.sample.freemarker;

import java.io.InputStreamReader;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestFreemarkerConfigValidation  {

	@Test
	public void testValidation() {
		try ( InputStreamReader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "config/freemarker-doc-process.xml" ) ) ) {
			boolean valid = FreemarkerDocProcessConfigValidator.logValidation( xmlReader );
			Assert.assertTrue( "Xml non valido", valid );
		} catch (Exception e) {
			throw new ConfigRuntimeException( "Exception on testValidation : "+e, e );
		}
	}
	
}
