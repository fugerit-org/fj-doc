package test.org.fugerit.java.doc.base.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.fugerit.java.doc.base.config.DocOutput;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocOutput {

	@Test
	public void test1() throws IOException {
		try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
			DocOutput docOutput = DocOutput.newOutput( baos );	
			log.info( "docOutput : {}" , docOutput );
			Assert.assertNotNull( docOutput.getResult() );
		}
		
	}
	
}
