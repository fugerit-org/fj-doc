package test.org.fugerit.java.doc.base.coverage;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.facade.DocHandlerFactory;
import org.junit.Assert;
import org.junit.Test;

public class TestDocHandlerFactory {

	@Test
	public void test001() {
		SafeFunction.apply( () -> {
			DocHandlerFactory factory = DocHandlerFactory.newInstance( "cl://coverage/config/doc-handler-sample.xml" );	
			DocHandlerFacade facade = factory.get( "test" ); 
			Assert.assertNotNull( facade );
			Assert.assertNotNull( facade.findHandler( "md" ) );
		} );	
	}
	
}
