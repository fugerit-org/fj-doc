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
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestConfig {

	@Test
	public void testDocConfig() {
		DocConfig config = new DocConfig();
		log.info( "version config -> {}", config.getVersionConfig() );
		Assert.assertNotNull( config );
	}
	
	@Test
	public void testBase64() {
		String text = "Venus!";
		String base64 = Base64Helper.encodeBase64String( text.getBytes() );
		Assert.assertEquals( text , new String( Base64Helper.decodeBase64String( base64 ) ) );
	}
	
	@Test
	public void testDocConstatns() {
		DocConstants config = new DocConstants();
		log.info( "const-> {}", config.getEuro() );
		Assert.assertNotNull( DocConstants.getInstance() );
	}
	
	@Test
	public void testDocFacadeSourceConfig() {
		boolean value = false;
		DocFacadeSourceConfig config = new DocFacadeSourceConfig(value);
		log.info( "config-> {}", config.isFailOnSourceModuleNotFound() );
		Assert.assertEquals( value , config.isFailOnSourceModuleNotFound() );
	}
	
	@Test
	public void testDocHelperEuro() {
		DocHelperEuro config = new DocHelperEuro();
		log.info( "config-> {}", config.filterText( "test" ) );
		log.info( "config-> {}", config.filterText( "test €" ) );
		Assert.assertNotNull( config );
	}
	
		
	@Test
	public void testInitAsyncAll() {
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
		Assert.assertTrue( failMap.containsKey( fail ) );
		Assert.assertFalse( failMap.containsKey( SimpleMarkdownExtTypeHandler.HANDLER ) );
		Assert.assertThrows( ConfigException.class , () -> InitHandler.initDoc( 
				fail
		) );
	}
	
	
}
