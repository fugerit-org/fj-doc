package test.org.fugerit.java.doc.sample.handlertest;

import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.junit.Test;

public class TestFullFreemarkerHTML extends TestHandleBase {

	@Test
	public void testHTML() {
		this.testWorker( "handler_full_test" , FreeMarkerHtmlTypeHandler.HANDLER_UTF8 );
	}
	
}
