package test.org.fugerit.java.doc.base.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocTypeHandlerDefault {

	
	
	@Test
	public void test1() throws Exception {
		TestHandler handler = new TestHandler();
		log.info( "handler format : {}", handler.getFormat() );
		Assert.assertEquals( TestHandler.FORMAT , handler.getFormat() );
		handler.handle(null, null);
		try ( InputStream is = new ByteArrayInputStream( "test=val".getBytes() ) ) {
			Assert.assertThrows( ConfigException.class , () -> handler.configureProperties( is ) );
		}
		Properties config = new Properties();
		Assert.assertThrows( ConfigException.class , () -> handler.configure( config ) );
		try ( InputStream is = new ByteArrayInputStream( "<handler><config charset='utf-8'/></handler>".getBytes() ) ) {
			handler.configureXML( is );
		}
		try ( InputStream is = new ByteArrayInputStream( "<handler><docHandlerCustomConfig custom-att='att'/></handler>".getBytes() ) ) {
			handler.configureXML( is );
		}
		try ( InputStream is = new ByteArrayInputStream( "<config>".getBytes() ) ) {
			Assert.assertThrows( ConfigException.class , () -> handler.configureXML( is ) );
		}
		String customId = UUID.randomUUID().toString();
		handler.setCustomId( customId );
		Assert.assertEquals( customId, handler.getCustomId() );
	}
	
}

class TestHandler extends DocTypeHandlerDefault {
	
	public static final String FORMAT = "custom";
	
	private static final long serialVersionUID = -266524103360778339L;

	public TestHandler() throws ConfigException {
		super( DocConfig.TYPE_HTML , "test" );
		this.setFormat( FORMAT );
		this.handleConfigTag( null );
	}
	
}

