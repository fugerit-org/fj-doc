package test.org.fugerit.java.doc.base.config;

import java.io.StringReader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownBasicTypeHandlerUTF8;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
 class TestDocTypeHandlerDecorator {

	@Test
	void test1() throws ConfigException, XMLException {
		SimpleMarkdownBasicTypeHandlerUTF8 handler = new SimpleMarkdownBasicTypeHandlerUTF8();
		log.info( "handler : {}", handler );
		try ( StringReader reader = new StringReader( "<config/>" ) ) {
			handler.configure( DOMIO.loadDOMDoc( reader ).getDocumentElement() );
		}
		Assertions.assertNotNull( handler.unwrap() );
	}
	
}
