package test.org.fugerit.java.doc.base.config;

import java.io.StringReader;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocInput {

	@Test
	void test1() {
		DocInput docInput = new DocInput( DocConfig.TYPE_PDF , new DocBase(), null );
		log.info( "docInput : {}" , docInput );
		Assertions.assertNotNull( docInput.getDoc() );
	}
	
	@Test
	void test2() {
		try ( StringReader reader = new StringReader( "<doc/>" ) ) {
			DocInput docInput = DocInput.newInput( DocConfig.TYPE_PDF , reader, DocFacadeSource.SOURCE_TYPE_XML );
			log.info( "docInput : {}" , docInput );
			Assertions.assertNotNull( docInput.getDoc() );
		}	
	}
	
	@Test
	void test3() {
		DocInput docInput = new DocInput( DocConfig.TYPE_PDF , null, null );
		log.info( "docInput : {}" , docInput );
		Assertions.assertNull( docInput.getDoc() );
	}
	
}
