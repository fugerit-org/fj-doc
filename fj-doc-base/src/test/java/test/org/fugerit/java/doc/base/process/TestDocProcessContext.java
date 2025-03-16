package test.org.fugerit.java.doc.base.process;

import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocProcessContext {

	@Test
	void testContext1() {
		DocProcessContext context = DocProcessContext.newContext( "c", "d" ).withSourceType(DocFacadeSource.SOURCE_TYPE_XML)
				.withAtt( "a" , "b" ).withDocBase( new DocBase() ).withDocType( DocConfig.TYPE_PDF );
		log.info( "context : {}", context );
		Assertions.assertEquals( "b" , context.getAttribute( "a" ) );
		Assertions.assertEquals( DocFacadeSource.SOURCE_TYPE_XML , context.getSourceType() );
	}
	
	@Test
	void testContext2() {
		Map<String, Object> map = new HashMap<>();
		map.put( "a" , "b" );
		DocProcessContext context = DocProcessContext.newContext( map ).withDocInput( DocInput.newInput( DocConfig.TYPE_PDF, new DocBase()) );
		log.info( "context : {}", context );
		Assertions.assertEquals( "b" , context.getAttribute( "a" ) );
	}
	
}
