package test.org.fugerit.java.doc.base.facade;

import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownBasicTypeHandler;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocHandlerFacade {

	private static final String XML_TEST_PATH = "coverage/xml/default_doc.xml";
	
	@Test
	public void testDocFacade() throws Exception {
		// test register
		DocHandlerFacade facade = new DocHandlerFacade();
		String testId = "test-id";
		facade.registerHandlerAndId( "test-id" , SimpleMarkdownBasicTypeHandler.HANDLER );
		Assert.assertNotNull( facade.findHandler( testId ) );
		facade.registerHandlerAndId( testId, SimpleMarkdownBasicTypeHandler.HANDLER, true ); // only log duplicate
		Assert.assertThrows( ConfigRuntimeException.class , () -> facade.registerHandlerAndId( testId, SimpleMarkdownBasicTypeHandler.HANDLER, false ) );
		facade.registerHandler( SimpleMarkdownBasicTypeHandler.HANDLER, true, false ); // only log duplicate
		Assert.assertThrows( ConfigException.class , () -> facade.registerHandler( SimpleMarkdownBasicTypeHandler.HANDLER, true, true ) );
		// test direct 1 ok
		try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( XML_TEST_PATH ) );
				ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
			facade.direct( reader , SimpleMarkdownBasicTypeHandler.HANDLER.getType() , baos );
			Assert.assertNotEquals( 0 , baos.toByteArray().length );
		}
		// test direct 2 ok
		try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( XML_TEST_PATH ) );
				ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
			facade.direct( reader , SimpleMarkdownBasicTypeHandler.HANDLER.getType() , DocOutput.newOutput(baos) );
			Assert.assertNotEquals( 0 , baos.toByteArray().length );
		}
		// test direct 3 ko
		try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( XML_TEST_PATH ) );
				ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
			Assert.assertThrows( ConfigException.class , () -> facade.direct( reader , "not-exists" , baos ) );
		}
		// print handlers
		for ( DocTypeHandler handler : facade.handlers() ) {
			log.info( "current handler {}", handler );
		}
		Assert.assertFalse( facade.listHandlersForType( SimpleMarkdownBasicTypeHandler.HANDLER.getType() ).isEmpty() );
	}
	
}
