package test.org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.function.UnsafeSupplier;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.XMLException;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import test.org.fugerit.java.BasicTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class TestPdfFopTypePooledHandler extends BasicTest {

	@Test
	public void testPoolUtils() throws ConfigException {
		UnsafeSupplier<FopConfigWrap, ConfigException> supplier = () -> new FopConfigWrap( null, null );
		FopConfigWrap configWrap1 = PoolUtils.handleFopWrap( null, null, 1, 2, supplier );
		Assert.assertNotNull( configWrap1 );
		FopConfigWrap configWrap2 = PoolUtils.handleFopWrap( null, new ArrayList<FopConfigWrap>(), 1, 2, supplier );
		Assert.assertNotNull( configWrap2 );
	}

	@Test
	public void pooledTest1() throws ConfigException, XMLException {
		PdfFopTypeHandler handler = new PdfFopTypeHandler();

		Element config = DOMIO.loadDOMDoc( "<conf><docHandlerCustomConfig fop-pool-min='1' fop-pool-max='1' fop-suppress-events='2'/></conf>" ).getDocumentElement();
		handler.configure( config );
		for ( int k=0; k<30; k++ ) {
			Runnable r = () -> {
				File outputFile = new File( "target/test"+ UUID.randomUUID()+"."+handler.getType() );
				try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_01.xml" ) );
					  FileOutputStream fos = new FileOutputStream( outputFile ) ) {
					handler.handle( DocInput.newInput( handler.getType(), reader ) , DocOutput.newOutput( fos ) );
					Assert.assertTrue( outputFile.exists() );
				}  catch (Exception e) {
					this.failEx( e );
				}
			};
			Thread t = new Thread( r );
			t.start();
			log.info( "thread started : {}", k );
		}
		Assert.assertTrue( Boolean.TRUE );
	}


	@Test
	public void pooledTest2() {
		PdfFopTypeHandler handler = new PdfFopTypeHandler();
		File outputFile = new File( "target/test"+System.currentTimeMillis()+"."+handler.getType() );
		try ( InputStreamReader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "sample/doc_test_01.xml" ) );
			  FileOutputStream fos = new FileOutputStream( outputFile ) ) {
			Element config = DOMIO.loadDOMDoc( "<conf><docHandlerCustomConfig fop-pool-min='10'  fop-suppress-events='1'/></conf>" ).getDocumentElement();
			handler.configure( config );
			handler.handle( DocInput.newInput( handler.getType(), reader ) , DocOutput.newOutput( fos ) );
			Assert.assertTrue( outputFile.exists() );
		}  catch (Exception e) {
			this.failEx( e );
		}
	}

	
}
