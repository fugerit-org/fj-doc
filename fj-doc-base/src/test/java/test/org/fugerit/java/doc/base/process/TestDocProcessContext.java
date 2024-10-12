package test.org.fugerit.java.doc.base.process;

import java.util.HashMap;
import java.util.Map;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocProcessContext {

	@Test
	public void testContext1() {
		DocProcessContext context = DocProcessContext.newContext( "c", "d" ).withSourceType(DocFacadeSource.SOURCE_TYPE_XML)
				.withAtt( "a" , "b" ).withDocBase( new DocBase() ).withDocType( DocConfig.TYPE_PDF );
		log.info( "context : {}", context );
		Assert.assertEquals( "b" , context.getAttribute( "a" ) );
		Assert.assertEquals( DocFacadeSource.SOURCE_TYPE_XML , context.getSourceType() );
	}
	
	@Test
	public void testContext2() {
		Map<String, Object> map = new HashMap<>();
		map.put( "a" , "b" );
		DocProcessContext context = DocProcessContext.newContext( map ).withDocInput( DocInput.newInput( DocConfig.TYPE_PDF, new DocBase()) );
		log.info( "context : {}", context );
		Assert.assertEquals( "b" , context.getAttribute( "a" ) );
	}
	
}
