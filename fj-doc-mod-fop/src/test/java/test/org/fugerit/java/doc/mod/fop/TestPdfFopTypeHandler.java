package test.org.fugerit.java.doc.mod.fop;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.FopConfig;
import org.fugerit.java.doc.mod.fop.FopConfigDefault;
import org.fugerit.java.doc.mod.fop.InitFopHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.BasicTest;

@Slf4j
class TestPdfFopTypeHandler extends BasicTest {

	@BeforeAll
	static void init() {
		SafeFunction.apply(  InitFopHandler::initDocAsync );
	}
	
	private boolean testHelper( DocTypeHandler handler ) {
		boolean ok = false;
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_01.xml" ) );
				FileOutputStream fos = new FileOutputStream( "target/test"+System.currentTimeMillis()+"."+handler.getType() ) ) {
			handler.handle( DocInput.newInput( handler.getType(), reader ) , DocOutput.newOutput( fos ) );
			ok = true;
		}  catch (Exception e) {
			this.failEx( e );
		}
		return ok;
	}
	
	@Test
	void test001Ok() {
		FopConfig config = new FopConfigDefault();
		PdfFopTypeHandler handler = new PdfFopTypeHandler();
		handler.setSuppressEvents( true );
		log.info( "suppress events : {}", handler.isSuppressEvents() );
		handler.setFopConfig( config );
		boolean ok = this.testHelper(handler);
		Assertions.assertTrue(ok);
	}
	
	private boolean configureHelper( String path ) {
		boolean ok = Boolean.FALSE;
		try  ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( path ) ) ) {
			Document doc = DOMIO.loadDOMDoc(reader);
			PdfFopTypeHandler handler = new PdfFopTypeHandler();
			handler.configure( doc.getDocumentElement() );
			ok = true;
		} catch (Exception e) {
			throw ConfigRuntimeException.convertEx( e );
		}
		return ok;
	}

	@Test
	void testOk() {
        Arrays.asList( "config/test_config_ok.xml",
                "config/test_config_ok-alt1.xml",
                "config/test_config_ok-alt-2.xml").forEach(
                current -> {
                    boolean ok = this.configureHelper( current );
                    Assertions.assertTrue(ok);
                }
        );

	}

    @Test
    void testKo() {
        Arrays.asList( "config/test_config_err1.xml",
                    "config/test_config_err2.xml",
                    "config/test_config_err3.xml" ).forEach(
                current -> Assertions.assertThrows( ConfigRuntimeException.class , () -> this.configureHelper( current ))
        );
    }

}
