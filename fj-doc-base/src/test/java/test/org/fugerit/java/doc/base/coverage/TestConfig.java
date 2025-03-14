package test.org.fugerit.java.doc.base.coverage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocConstants;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.config.InitHandler;
import org.fugerit.java.doc.base.facade.DocFacadeSourceConfig;
import org.fugerit.java.doc.base.helper.Base64Helper;
import org.fugerit.java.doc.base.helper.DocHelperEuro;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestConfig {

	@Test
	void testDocConfig() {
		DocConfig config = new DocConfig();
		log.info( "version config -> {}", config.getVersionConfig() );
		Assertions.assertNotNull( config );
	}
	
	@Test
	void testBase64() {
		String text = "Venus!";
		String base64 = Base64Helper.encodeBase64String( text.getBytes() );
		Assertions.assertEquals( text , new String( Base64Helper.decodeBase64String( base64 ) ) );
	}
	
	@Test
	void testDocConstatns() {
		DocConstants config = new DocConstants();
		log.info( "const-> {}", config.getEuro() );
		Assertions.assertNotNull( DocConstants.getInstance() );
	}
	
	@Test
	void testDocFacadeSourceConfig() {
		boolean value = false;
		DocFacadeSourceConfig config = new DocFacadeSourceConfig(value);
		log.info( "config-> {}", config.isFailOnSourceModuleNotFound() );
		Assertions.assertEquals( value , config.isFailOnSourceModuleNotFound() );
	}
	
	@Test
	void testDocHelperEuro() {
		DocHelperEuro config = new DocHelperEuro();
		log.info( "config-> {}", config.filterText( "test" ) );
		log.info( "config-> {}", config.filterText( "test €" ) );
		Assertions.assertNotNull( config );
	}
	
		
	@Test
	void testInitAsyncAll() {
		DocTypeHandler fail = new DocTypeHandlerDefault( DocConfig.TYPE_CSV , "test-fail" ) {
			@Override
			public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
				throw new ConfigException( "Test fail init" );
			}
			private static final long serialVersionUID = -5969726916244716079L;
		};
		List<DocTypeHandler> handlers = Arrays.asList( SimpleMarkdownExtTypeHandler.HANDLER, fail );
		InitHandler.initDocAllAsync( handlers );
		Map<DocTypeHandler, Exception> failMap = InitHandler.initDocAll( handlers );
		Assertions.assertTrue( failMap.containsKey( fail ) );
		Assertions.assertFalse( failMap.containsKey( SimpleMarkdownExtTypeHandler.HANDLER ) );
		Assertions.assertThrows( ConfigException.class , () -> InitHandler.initDoc( 
				fail
		) );
	}
	
	
}
