package test.org.fugerit.java.doc.sample.freemarker;

import java.io.InputStreamReader;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigValidator;
import org.junit.Test;

public class TestFreemarkerConfigValidation  {

	@Test
	public void testValidation() {
		try ( InputStreamReader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "config/freemarker-doc-process.xml" ) ) ) {
			FreemarkerDocProcessConfigValidator.logValidation( xmlReader );
		} catch (Exception e) {
			throw new RuntimeException( e ); 
		}
	}
	
}
