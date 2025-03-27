package test.org.fugerit.java.doc.sample.config;

import static org.junit.jupiter.api.Assertions.fail;

import org.fugerit.java.doc.base.facade.DocHandlerFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDocHandlerFactory {

	private final static Logger logger = LoggerFactory.getLogger( TestDocHandlerFactory.class );
	
	@Test
	void initDocFactoryTest() {
		try {
			String path = "cl://config/doc-handler-sample.xml";
			DocHandlerFactory factory = DocHandlerFactory.newInstance( path );
			logger.info( "factory {}", factory );
		} catch (Throwable t) {
			String message = "Error : "+t;
			logger.error( message, t );
			fail( message );
		}
	}
	
}
