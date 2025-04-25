package test.org.fugerit.java.doc.mod.fop;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.config.InitHandler;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.mod.fop.config.ClassLoaderResourceResolverWrapper;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoaderWrapper;
import org.fugerit.java.doc.mod.fop.config.ResourceResolverWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.BasicTest;

@Slf4j
class TestResourceResolver extends BasicTest {

	@Test
	void testResourceResolverWrapper() throws IOException {
		File target = new File( "target" );
		ResourceResolverWrapper wrapper = new ResourceResolverWrapper( new DoNothingResourceResolver() );
		log.info( "wrapper : {} : {}", wrapper, wrapper.unwrap() );
		Assertions.assertNotNull( this.fullSerializationTest( wrapper ) );
		Assertions.assertNull( wrapper.getResource( target.toURI() ) );
		Assertions.assertNull( wrapper.getOutputStream( target.toURI() ) );
	}
	
	@Test
	void testClassLoaderResourceResolverWrapper() throws IOException, URISyntaxException {
		ClassLoaderResourceResolverWrapper rr = new ClassLoaderResourceResolverWrapper();
		File target = new File( "target/test.txt" );
		rr.wrap( new DoNothingResourceResolver() );
		URI u = new URI( ClassLoaderResourceResolverWrapper.CLASSPATH_SCHEMA+"sample/doc_alt_01.xml" );
		log.info( "wrapper : {} : {}", rr, rr.unwrap() );
		Assertions.assertNotNull( rr.getResource( u ) );
		Assertions.assertNull( rr.getResource( target.toURI() ) );
		Assertions.assertNull( rr.getOutputStream( target.toURI() ) );
	}
	
	@Test 
	void testFopConfigClassLoaderWrapper() throws ConfigException {
		FopConfigClassLoaderWrapper config1 = new FopConfigClassLoaderWrapper( "fop-config.xml" );
		Assertions.assertNotNull( config1.newFactory() );
		FopConfigClassLoaderWrapper config2 = new FopConfigClassLoaderWrapper( "fop-config-pdfa.xml" );
		Assertions.assertNotNull( config2.newFactory() );
		FopConfigClassLoaderWrapper config3 = new FopConfigClassLoaderWrapper( "fop-config-pdfua.xml" );
		Assertions.assertNotNull( config3.newFactory() );
		Assertions.assertNotNull( new PdfFopTypeHandler( StandardCharsets.UTF_8 , config3) );
		Assertions.assertNotNull( new PdfFopTypeHandler( StandardCharsets.UTF_8 , false, false ) );
		Assertions.assertNotNull( new PdfFopTypeHandler( StandardCharsets.UTF_8 ) );
		Assertions.assertNotNull( config1.getCustomResourceResolver() );
	}
	
	@Test 
	void testFmConfig() throws Exception {
		FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fm-config.xml" );
		for ( DocTypeHandler handler : config.getFacade().handlers() ) {
			log.info( "init handler : {}", handler );
			Assertions.assertTrue( InitHandler.initDoc( handler ) );
		}
	}
	
}
