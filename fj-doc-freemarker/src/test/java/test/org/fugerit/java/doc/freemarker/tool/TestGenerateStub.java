package test.org.fugerit.java.doc.freemarker.tool;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.freemarker.tool.GenerateStub;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.BasicTest;

public class TestGenerateStub extends BasicTest {

	@Test
	public void genTest001() {
		runTestEx( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( "generate-stub-test/doc-process-autodoc.xml" );
					StringWriter writer = new StringWriter() ) {
				Properties props = new Properties();
				GenerateStub.generate( writer, props, is );
				Assert.assertNotEquals( 0 , writer.toString().length() );
			}
		} );
	}
	
	@Test
	public void genTest002() {
		runTestEx( () -> {
			try (StringWriter writer = new StringWriter() ) {
				Properties props = new Properties();
				props.setProperty( GenerateStub.ARG_INPUT_FILE , "src/test/resources/generate-stub-test/doc-process-autodoc.xml" );
				GenerateStub.generate( writer, props );
				Assert.assertNotEquals( 0 , writer.toString().length() );
			}
		} );
	}
	
}
