package test.org.fugerit.java.doc.mod.fop;

import java.io.FileOutputStream;
import java.io.InputStreamReader;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.FopConfig;
import org.fugerit.java.doc.mod.fop.FopConfigDefault;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import test.org.fugerit.java.BasicTest;

public class TestPdfFopTypeHandler extends BasicTest {

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
	public void test001Ok() {
		FopConfig config = new FopConfigDefault();
		PdfFopTypeHandler handler = new PdfFopTypeHandler();
		handler.setFopConfig( config );
		boolean ok = this.testHelper(handler);
		Assert.assertTrue(ok);
	}
	
	private boolean configureHelper( String path ) {
		boolean ok = false;
		try  ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( path ) ) ) {
			Document doc = DOMIO.loadDOMDoc(reader);
			PdfFopTypeHandler handler = new PdfFopTypeHandler();
			handler.configure( doc.getDocumentElement() );
			ok = true;
		} catch (Exception e) {
			throw new ConfigRuntimeException( e );
		}
		return ok;
	}
	
	@Test
	public void test002Ko() {
		Assert.assertThrows( ConfigRuntimeException.class , () -> {
			this.configureHelper(  "config/test_config_err1.xml" );
		});
	}
	

	@Test
	public void test003Ok() {
		boolean ok = this.configureHelper(  "config/test_config_ok.xml" );
		Assert.assertTrue(ok);
	}
	
	@Test
	public void test004Ko() {
		Assert.assertThrows( ConfigRuntimeException.class , () -> {
			this.configureHelper(  "config/test_config_err2.xml" );
		});
	}
	
	
}
