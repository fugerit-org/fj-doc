package test.org.fugerit.java.doc.base.coverage;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.facade.DocHandlerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDocHandlerFactory {

	@Test
	void test001() {
		Assertions.assertNotNull( SafeFunction.get( () -> {
			DocHandlerFactory factory = DocHandlerFactory.newInstance( "cl://coverage/config/doc-handler-sample.xml" );	
			DocHandlerFacade facade = factory.get( "test" ); 
			Assertions.assertNotNull( facade );
			return facade.findHandler( "md" );
		} ) );
	}
	
}
