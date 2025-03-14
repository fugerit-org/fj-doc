package test.org.fugerit.java.doc.base.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.fugerit.java.doc.base.config.DocOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocOutput {

	@Test
	void test1() throws IOException {
		try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
			DocOutput docOutput = DocOutput.newOutput( baos );	
			log.info( "docOutput : {}" , docOutput );
			Assertions.assertNotNull( docOutput.getResult() );
		}
		
	}
	
}
