package test.org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandlerUTF8;
import org.fugerit.java.doc.mod.fop.InitFopHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeNoAccessibilityHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import test.org.fugerit.java.BasicTest;

import java.io.FileOutputStream;
import java.io.InputStreamReader;

@Slf4j
class TestNoAccessibility extends BasicTest {

	private boolean testHelper( String testCase, DocTypeHandler handler ) {
		boolean ok = false;
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "sample/doc_alt_01.xml" ) );
				FileOutputStream fos = new FileOutputStream( "target/test_no_accessibilit-"+testCase+"."+handler.getType() ) ) {
			handler.handle( DocInput.newInput( handler.getType(), reader ) , DocOutput.newOutput( fos ) );
			ok = true;
			log.info( "result {}", ok );
		}  catch (Exception e) {
			this.failEx( e );
		}
		return ok;
	}
	
	private static final DocTypeHandler[] HANDLERS = { PdfFopTypeHandler.HANDLER, new PdfFopTypeNoAccessibilityHandler() };
	
	@Test
	void testNoAccessibilityHandler() {
		for ( int k=0; k<HANDLERS.length; k++ ) {
			boolean ok = this.testHelper( HANDLERS[k].getClass().getSimpleName(), HANDLERS[k]);
			Assertions.assertTrue(ok);
		}
	}

	@Test
	void testNoAccessibilityConfig() throws Exception {
		PdfFopTypeHandler handler = new PdfFopTypeHandler();
		this.testHelper( "test-config-pre", handler );
		Element config = DOMIO.loadDOMDoc( "<conf><docHandlerCustomConfig accessibility='false' keep-empty-tags='true'/></conf>" ).getDocumentElement();
		handler.configure( config );
		this.testHelper( "test-config-post", handler );

	}
	
}
