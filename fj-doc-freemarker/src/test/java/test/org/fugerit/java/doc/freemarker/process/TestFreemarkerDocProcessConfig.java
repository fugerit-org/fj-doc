package test.org.fugerit.java.doc.freemarker.process;

import static org.junit.Assert.fail;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Properties;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.xml.dom.DOMIO;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.fugerit.java.doc.base.process.DocProcessData;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConfigStep;
import org.fugerit.java.doc.freemarker.config.FreeMarkerConstants;
import org.fugerit.java.doc.freemarker.config.FreeMarkerSkipProcessStep;
import org.fugerit.java.doc.freemarker.config.FreemarkerApplyHelper;
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

	private static final String MAIN_CONFIG = "fj_doc_test/freemarker-doc-process.xml";

	@Test
	public void testConfigRead001() throws Exception {
		String[] configList = { MAIN_CONFIG, "fj_doc_test/freemarker-doc-process-1.xml", "fj_doc_test/freemarker-doc-process-2.xml", "fj_doc_test/freemarker-doc-process-3.xml" };
		for (  int k=0 ;k<configList.length ;k++ ) {
			String currentConfig = configList[k];
			try ( Reader xmlReader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( currentConfig ) ) ) {
				FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.loadConfig(xmlReader);
				String chainId = "sample_chain";
				String type = DocConfig.TYPE_HTML;
				log.info( "config {}", config.getChain( chainId ) );
				Assert.assertNotNull( config );
				try (FileOutputStream fos = new FileOutputStream( new File( "target", chainId+"_"+k+"."+type ) )  ) {
					config.fullProcess( chainId, DocProcessContext.newContext(), DocConfig.TYPE_HTML, fos );
				}
				String chainIdError = "error_chain";
				log.info( "current config : {} -> {}", k, currentConfig );
				try (ByteArrayOutputStream fos = new ByteArrayOutputStream() ) {
					DocProcessContext context = DocProcessContext.newContext().withSourceType( DocFacadeSource.SOURCE_TYPE_XML );
					if ( k == 0 || k == 2 ) {
						Assert.assertThrows( ConfigRuntimeException.class, () -> config.fullProcess( chainIdError, context, DocConfig.TYPE_HTML, fos ) );
					} else {
						config.fullProcess( chainIdError, context, DocConfig.TYPE_HTML, fos );
					}
				}
			}
		}
	}

	@Test
	public void testSource() throws Exception {
		FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://"+MAIN_CONFIG );
		String[] chainId = { "xml", "json", "yaml" };
		for ( int k=0 ;k<chainId.length ;k++ ) {
			String currentChainId = "asciidoc-"+chainId[k];
			String type = DocConfig.TYPE_HTML;
			File outputFile = new File( "target", "test_source_"+currentChainId+"."+type );
			log.info( "currentChainId : {}, currentSourceType : {}", currentChainId, outputFile );
			try ( FileOutputStream fos = new FileOutputStream( outputFile ) ) {
				config.fullProcess( currentChainId, DocProcessContext.newContext(), type, fos );
			}
		}
		// test template does not exist
		try ( ByteArrayOutputStream os = new ByteArrayOutputStream() ) {
			DocProcessContext context = DocProcessContext.newContext();
			Assert.assertThrows( ConfigRuntimeException.class, () -> config.fullProcess( "not-exists", context, DocConfig.TYPE_HTML, os ) );
		}
	}

	@Test
	public void testSkipFM() throws Exception {
		DocProcessContext context = DocProcessContext.newContext();
		DocProcessData data = new DocProcessData();
		FreeMarkerConfigStep configStep = new FreeMarkerConfigStep();
		configStep.setParam01( "FJ_DOC_TEST_CUSTOM" );
		Properties customConfig = new Properties();
		customConfig.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE, FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_MODE_CLASS );
		customConfig.setProperty( FreeMarkerConfigStep.ATT_FREEMARKER_CONFIG_KEY_PATH, "/fj_doc_test/template/" );
		configStep.setCustomConfig( customConfig );
		configStep.process( context, data );
		// test skip fm
		FreeMarkerSkipProcessStep step = new FreeMarkerSkipProcessStep();
		step.setParam01( "asciidoc-xml.ftl" );
		step.process( context, data );
		Assert.assertNotNull( data.getCurrentXmlData() );
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
		DocProcessContext context = DocProcessContext.newContext( "test", "testString" ).withAtt( "testDate", LocalDateTime.now() );
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
			config.getFacade().registerHandler( FreeMarkerHtmlTypeHandlerUTF8.HANDLER );
			// test full process
			try ( ByteArrayOutputStream baos = new ByteArrayOutputStream() ) {
				DocProcessData data = config.fullProcess( "test_02" ,
						DocProcessContext.newContext(), DocConfig.TYPE_HTML, baos );
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
		Assert.assertTrue( Boolean.TRUE );
	}

	@Test
	public void testLoadConfigKo() {
		String fullPath = "fj_doc_test/freemarker-doc-process_ko.xml";
		Assert.assertThrows( ConfigRuntimeException.class, () -> FreemarkerDocProcessConfigFacade.loadConfigSafe( fullPath ) );
		try {
			FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://not-exists.xml" );
		} catch (ConfigRuntimeException e) {
			log.error( String.format( "Error : %s", e ), e );
			Assert.assertTrue( e.getMessage().contains( FreemarkerDocProcessConfigFacade.ERROR_CONFIG_PATH_NOT_FOUND_BASE_MESSAGE ) );
		}
		Exception testEx = new ConfigRuntimeException( "ex1", new ConfigRuntimeException( "ex0" ) );
		Assert.assertEquals( ConfigRuntimeException.class,  FreemarkerDocProcessConfigFacade.EX_CONSUMER_LOAD_CONFIG.apply( testEx ).getClass() );
	}
	
}
