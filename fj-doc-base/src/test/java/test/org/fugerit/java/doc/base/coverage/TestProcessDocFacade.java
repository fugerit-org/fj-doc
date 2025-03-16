package test.org.fugerit.java.doc.base.coverage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.core.cfg.xml.ListMapConfig;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.core.util.filterchain.MiniFilterChain;
import org.fugerit.java.core.util.filterchain.MiniFilterConfigEntry;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.facade.ProcessDocFacade;
import org.fugerit.java.doc.base.process.DocProcessConfig;
import org.fugerit.java.doc.base.process.DocProcessContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestProcessDocFacade {

	public static final String TEST_PROCESS_PATH = "coverage/config/doc-process-autodoc.xml";
	
	public static final String TEST_CHAIN_ID = "test-chain";
	
	@Test
	void test001() {
		Assertions.assertTrue( SafeFunction.get( () -> {
			ProcessDocFacade facade = ProcessDocFacade.newFacade( 
					"cl://"+TEST_PROCESS_PATH,
					"cl://coverage/config/doc-handler-sample.xml", 
					"test" );	
			Assertions.assertNotNull( facade );
			DocProcessContext context = DocProcessContext.newContext();
			// test base
			try ( ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
				facade.process( "test-chain" , DocConfig.TYPE_MD, context, buffer );
				log.info( "result -> \n{}", buffer.toString( StandardCharsets.UTF_8.name() ) );
			}
			// test validate
			try ( ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
				facade.process( "test-chain" , DocConfig.TYPE_MD, context, buffer, true );
				log.info( "result -> \n{}", buffer.toString( StandardCharsets.UTF_8.name() ) );
			}
			// test file
			File testFile = new File( "target/test_coverage_process_doc_facade.md" );
			testFile.delete();
			facade.process( "test-chain" , DocConfig.TYPE_MD, context, testFile  );
			return testFile.exists();
		} ) );
	}
	
	@Test
	void test002() {
		Assertions.assertNotNull( SafeFunction.get( () -> DocProcessConfig.loadConfigSafe( "cl://"+TEST_PROCESS_PATH ) ) );
	}
	
	@Test
	void test003() {
		Assertions.assertTrue( SafeFunction.get( () -> {
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( TEST_PROCESS_PATH ) ) {
				DocProcessConfig facade = new DocProcessConfig();
				DocProcessConfig.loadConfig(is, facade);	
				ListMapConfig<MiniFilterConfigEntry> config = facade.getListMap( TEST_CHAIN_ID);
				MiniFilterChain chain = facade.getChainCache( TEST_CHAIN_ID );
				Assertions.assertNotNull( facade );
				Assertions.assertNotNull( config );
				Assertions.assertNotNull( chain );
				Assertions.assertNotNull( facade.getGeneralProps() );
				Assertions.assertNotNull( facade.getKeys() );
				Assertions.assertNotNull( facade.getIdSet() );
				Assertions.assertNotNull( facade.getDataList( TEST_CHAIN_ID ) );
				facade.setChain( TEST_CHAIN_ID , chain );
				return Boolean.TRUE;
			}
		} ) );
	}
	
}
