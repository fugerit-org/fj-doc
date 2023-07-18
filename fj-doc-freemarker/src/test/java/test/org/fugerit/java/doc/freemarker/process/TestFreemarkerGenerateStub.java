package test.org.fugerit.java.doc.freemarker.process;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Properties;

import org.fugerit.java.doc.freemarker.tool.GenerateStub;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestFreemarkerGenerateStub {

	@Test
	public void testGenerateStub() {
		try ( Writer writer = new FileWriter( new File( "target/freemarker-doc-process-config-stub.xml" ) ) ) {
			GenerateStub.generate(writer, new Properties() );
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail(message);
		}
	}
	
}
