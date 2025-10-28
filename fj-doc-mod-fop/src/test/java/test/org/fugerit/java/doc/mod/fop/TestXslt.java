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
class TestXslt extends BasicTest {

	@BeforeAll
	static void init() {
		SafeFunction.apply(  InitFopHandler::initDoc );
	}
	
	private boolean testHelper( DocTypeHandler handler, String xmlName ) {
		boolean ok = false;
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "xslt-sample/"+xmlName+".xml" ) );
				FileOutputStream fos = new FileOutputStream( "target/"+xmlName+"."+handler.getType() ) ) {
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
	void testXslt01Ok() {
        for (DocTypeHandler handler : HANDLERS) {
            boolean ok = this.testHelper(handler, "xslt-sample-01");
            Assertions.assertTrue(ok);
        }
	}

	@Test
	void testXslt02Ok() {
        for (DocTypeHandler handler : HANDLERS) {
            boolean ok = this.testHelper(handler, "xslt-sample-02");
            Assertions.assertTrue(ok);
        }
	}
	
}
