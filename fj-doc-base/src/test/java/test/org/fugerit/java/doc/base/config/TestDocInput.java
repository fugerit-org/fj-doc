package test.org.fugerit.java.doc.base.config;

import java.io.StringReader;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocInput {

	@Test
	public void test1() {
		DocInput docInput = new DocInput( DocConfig.TYPE_PDF , new DocBase(), null );
		log.info( "docInput : {}" , docInput );
		Assert.assertNotNull( docInput.getDoc() );
	}
	
	@Test
	public void test2() {
		try ( StringReader reader = new StringReader( "<doc/>" ) ) {
			DocInput docInput = DocInput.newInput( DocConfig.TYPE_PDF , reader, DocFacadeSource.SOURCE_TYPE_XML );
			log.info( "docInput : {}" , docInput );
			Assert.assertNotNull( docInput.getDoc() );
		}	
	}
	
	@Test
	public void test3() {
		DocInput docInput = new DocInput( DocConfig.TYPE_PDF , null, null );
		log.info( "docInput : {}" , docInput );
		Assert.assertNull( docInput.getDoc() );
	}
	
}
