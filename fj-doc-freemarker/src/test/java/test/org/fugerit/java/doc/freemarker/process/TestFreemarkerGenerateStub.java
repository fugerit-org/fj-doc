package test.org.fugerit.java.doc.freemarker.process;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Properties;

import org.fugerit.java.doc.freemarker.tool.GenerateStub;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class TestFreemarkerGenerateStub {

	@Test
	void testGenerateStub() {
		try ( Writer writer = new FileWriter( new File( "target/freemarker-doc-process-config-stub.xml" ) ) ) {
			GenerateStub.generate(writer, new Properties() );
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			Assertions.fail(message);
		}
	}
	
}
