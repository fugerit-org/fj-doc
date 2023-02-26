package test.org.fugerit.java.doc.sample.config;

import static org.junit.Assert.fail;

import org.fugerit.java.doc.base.facade.DocHandlerFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDocHandlerFactory {

	private final static Logger logger = LoggerFactory.getLogger( TestDocHandlerFactory.class );
	
	@Test
	public void initDocFactoryTest() {
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
