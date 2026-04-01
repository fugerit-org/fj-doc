package test.org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
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
import test.org.fugerit.java.BasicTest;

import java.io.FileOutputStream;
import java.io.InputStreamReader;

@Slf4j
class TestSvg extends BasicTest {

	@BeforeAll
	static void init() {
		SafeFunction.apply(  InitFopHandler::initDoc );
	}
	
	private boolean testHelper( DocTypeHandler handler ) {
		boolean ok = false;
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "sample/doc-svg.xml" ) );
				FileOutputStream fos = new FileOutputStream( "target/test-svg."+handler.getType() ) ) {
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
	void testDocSvgOk() {
		for ( int k=0; k<HANDLERS.length; k++ ) {
			boolean ok = this.testHelper(HANDLERS[k]);
			Assertions.assertTrue(ok);
		}
	}
	
}
