package test.org.fugerit.java.doc.freemarker.tool;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.tool.GenerateStub;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.org.fugerit.java.BasicTest;

class TestGenerateStub extends BasicTest {

	@Test
	void genTest001() {
		int result = SafeFunction.get( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "generate-stub-test/doc-process-autodoc.xml" );
					StringWriter writer = new StringWriter() ) {
				Properties props = new Properties();
				GenerateStub.generate( writer, props, is );
				return writer.toString().length();
			}
		} );
		Assertions.assertNotEquals( 0 , result );
	}
	
	@Test
	void genTest002() {
		int result = SafeFunction.get( () -> {
			try (StringWriter writer = new StringWriter() ) {
				Properties props = new Properties();
				props.setProperty( GenerateStub.ARG_INPUT_FILE , "src/test/resources/generate-stub-test/doc-process-autodoc.xml" );
				GenerateStub.generate( writer, props );
				return writer.toString().length();
			}
		} );
		Assertions.assertNotEquals( 0, result );
	}
	
}
