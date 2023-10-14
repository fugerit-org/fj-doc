package test.org.fugerit.java.doc.freemarker.process;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerEscapeUTF8;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandlerUTF8;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigValidator;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.BasicTest;

@Slf4j
public class TestFreemarkerDocProcessConfig extends BasicTest {

	@Test
	public void testConfigRead001() {
		try ( Reader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "fj_doc_test/freemarker-doc-process.xml" ) ) ) {
			FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.loadConfig(xmlReader);
			log.info( "config {}", config.getChain( "sample_chain" ) );
			Assert.assertNotNull( config );
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail(message);
		}
	}
	
	@Test
	public void testConfigFail01() {
		Assert.assertThrows( ConfigRuntimeException.class , () -> FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://not-exists.xml" ) );
	}
	
	@Test
	public void testConfigFail02() {
		Reader reader = null;
		Assert.assertThrows( ConfigException.class , () -> FreemarkerDocProcessConfigFacade.loadConfig( reader ) );
	}
	
	@Test
	public void testConfigSec() {
		Assert.assertNotNull( FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj_doc_test/freemarker-doc-process_sec.xml" ) );
	}
	
	
	private void templateTesting( FreemarkerDocProcessConfig config ) {
		DocProcessContext context = DocProcessContext.newContext( "test", "testString" );
		runTestEx( () -> {
			try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				config.process( "test_01", DocConfig.TYPE_MD, context, baos, false );
			}
		} );
		runTestEx( () -> {
			try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				config.process( "test_01_alt", DocConfig.TYPE_MD, context, baos, false );
			}
		} );
		runTestEx( () -> {
			try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				// need to make it work in the future!
				Assert.assertThrows( NullPointerException.class, () -> {
					config.process( "test_01_inline", DocConfig.TYPE_MD, context, baos, false );
				} );
			}
		} );
		Assert.assertThrows( ConfigException.class , () -> {
			try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				config.process( "test_01_fail", DocConfig.TYPE_MD, context, baos, false );
			}
		} );
	}
	
	@Test
	public void testConfigRead002() {
		FreemarkerDocProcessConfig config = 
				FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fj_doc_test/freemarker-doc-process_alt.xml" );
		Assert.assertNotNull( config );
		this.templateTesting(config);
		log.info( "keys : {}", config.getKeys() );
	}
	
	@Test
	public void testConfigValidate001() {
		try ( Reader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( "fj_doc_test/freemarker-doc-process.xml" ) ) ) {
			FreemarkerDocProcessConfigValidator.logValidation( xmlReader );
		} catch (Exception e) {
			String message = "Error : "+e;
			log.error( message, e );
			fail(message);
		}
	}
	
	@Test
	public void testNewSimpleConfig() {
		try {
			Assert.assertNotNull( FreemarkerDocProcessConfigFacade.newSimpleConfig( "simple-config-001",  "/template").getChainCache( "test" ) );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testNewSimpleConfigVersion() {
		try {
			Assert.assertNotNull( FreemarkerDocProcessConfigFacade.newSimpleConfig( "simple-config-002",  "/template", FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_VERSION_2_3_31 ).getChainCache( "test" ) );
		} catch (Exception e) {
			this.failEx(e);
		}
	}
	
	@Test
	public void testProcess() {
		SafeFunction.apply( () -> {
			FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.newSimpleConfig( "simple-config-003",  "/fj_doc_test/template/" );
			// test full process
			try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				DocProcessData data = config.fullProcess( "test_02" ,
						DocProcessContext.newContext(), FreeMarkerHtmlTypeHandlerUTF8.HANDLER, DocOutput.newOutput(baos) );
				Assert.assertNotEquals( 0 , data.getCurrentXmlData().length() );
			}
			// test process 1
			// handler list :
			FreeMarkerHtmlTypeHandler custom1 = new FreeMarkerHtmlTypeHandler();
			try ( StringReader reader = new StringReader( "<config><docHandlerCustomConfig escapeTextAsHtml='true'/></config>" ) ) {
				custom1.configure( DOMIO.loadDOMDoc( reader ).getDocumentElement() );
			}
			DocTypeHandler[] handlers = { FreeMarkerHtmlTypeHandlerUTF8.HANDLER, FreeMarkerHtmlTypeHandlerEscapeUTF8.HANDLER, FreeMarkerHtmlFragmentTypeHandlerEscapeUTF8.HANDLER, custom1 };
			for ( int k=0; k<handlers.length; k++ ) {
				try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
					DocProcessData data = new DocProcessData();
					config.process( "test_02" , DocProcessContext.newContext( "testKey", "<test/>" ), data, FreeMarkerHtmlTypeHandlerUTF8.HANDLER, DocOutput.newOutput(baos) );
					Assert.assertNotEquals( 0 , data.getCurrentXmlData().length() );
					File file = new File( "target/test_02_handler_"+k+".xml" );
					FileIO.writeBytes( data.getCurrentXmlData().getBytes() , file );
				}
			}
		} );
	}
	
	
}
