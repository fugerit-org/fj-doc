package test.org.fugerit.java.doc.base.config;

import java.io.StringReader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownBasicTypeHandlerUTF8;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocTypeHandlerDecorator {

	@Test
	public void test1() throws ConfigException, XMLException {
		SimpleMarkdownBasicTypeHandlerUTF8 handler = new SimpleMarkdownBasicTypeHandlerUTF8();
		log.info( "handler : {}", handler );
		try ( StringReader reader = new StringReader( "<config/>" ) ) {
			handler.configure( DOMIO.loadDOMDoc( reader ).getDocumentElement() );
		}
		Assert.assertNotNull( handler.unwrap() );
	}
	
}
