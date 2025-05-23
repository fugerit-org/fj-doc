package test.org.fugerit.java.doc.mod.fop;

import java.io.FileOutputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandlerUTF8;
import org.fugerit.java.doc.mod.fop.InitFopHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.BasicTest;

@Slf4j
class TestAlt extends BasicTest {

	@BeforeAll
	static void init() {
		SafeFunction.apply(  InitFopHandler::initDoc );
	}
	
	private boolean testHelper( DocTypeHandler handler ) {
		boolean ok = false;
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "sample/doc_alt_01.xml" ) );
				FileOutputStream fos = new FileOutputStream( "target/test_alt_01."+handler.getType() ) ) {
			handler.handle( DocInput.newInput( handler.getType(), reader ) , DocOutput.newOutput( fos ) );
			ok = true;
			log.info( "result {}", ok );
		}  catch (Exception e) {
			this.failEx( e );
		}
		return ok;
	}
	
	private static final DocTypeHandler[] HANDLERS = { PdfFopTypeHandler.HANDLER, new FreeMarkerFopTypeHandlerUTF8() };
	
	@Test
	void testAlt001Ok() {
		for ( int k=0; k<HANDLERS.length; k++ ) {
			boolean ok = this.testHelper(HANDLERS[k]);
			Assertions.assertTrue(ok);
		}
	}
	
}
