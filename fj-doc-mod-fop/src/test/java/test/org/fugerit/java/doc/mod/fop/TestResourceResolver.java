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
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import test.org.fugerit.java.BasicTest;

@Slf4j
public class TestResourceResolver extends BasicTest {

	@Test 
	public void testResourceResolverWrapper() throws IOException {
		File target = new File( "target" );
		ResourceResolverWrapper wrapper = new ResourceResolverWrapper( new DoNothingResourceResolver() );
		log.info( "wrapper : {} : {}", wrapper, wrapper.unwrap() );
		Assert.assertNotNull( this.fullSerializationTest( wrapper ) );
		Assert.assertNull( wrapper.getResource( target.toURI() ) );
		Assert.assertNull( wrapper.getOutputStream( target.toURI() ) );
	}
	
	@Test
	public void testClassLoaderResourceResolverWrapper() throws IOException, URISyntaxException {
		ClassLoaderResourceResolverWrapper rr = new ClassLoaderResourceResolverWrapper();
		File target = new File( "target/test.txt" );
		rr.wrap( new DoNothingResourceResolver() );
		URI u = new URI( ClassLoaderResourceResolverWrapper.CLASSPATH_SCHEMA+"sample/doc_alt_01.xml" );
		log.info( "wrapper : {} : {}", rr, rr.unwrap() );
		Assert.assertNotNull( rr.getResource( u ) );
		Assert.assertNull( rr.getResource( target.toURI() ) );
		Assert.assertNull( rr.getOutputStream( target.toURI() ) );
	}
	
	@Test 
	public void testFopConfigClassLoaderWrapper() throws IOException, ConfigException {
		FopConfigClassLoaderWrapper config1 = new FopConfigClassLoaderWrapper( "fop-config.xml" );
		Assert.assertNotNull( config1.newFactory() );
		FopConfigClassLoaderWrapper config2 = new FopConfigClassLoaderWrapper( "fop-config-pdfa.xml" );
		Assert.assertNotNull( config2.newFactory() );
		FopConfigClassLoaderWrapper config3 = new FopConfigClassLoaderWrapper( "fop-config-pdfua.xml" );
		Assert.assertNotNull( config3.newFactory() );
		Assert.assertNotNull( new PdfFopTypeHandler( StandardCharsets.UTF_8 , config3) );
		Assert.assertNotNull( new PdfFopTypeHandler( StandardCharsets.UTF_8 , false, false ) );
		Assert.assertNotNull( new PdfFopTypeHandler( StandardCharsets.UTF_8 ) );
		Assert.assertNotNull( config1.getCustomResourceResolver() );
	}
	
	@Test 
	public void testFmConfig() throws Exception {
		FreemarkerDocProcessConfig config = FreemarkerDocProcessConfigFacade.loadConfigSafe( "cl://fm-config.xml" );
		for ( DocTypeHandler handler : config.getFacade().handlers() ) {
			log.info( "init handler : {}", handler );
			Assert.assertTrue( InitHandler.initDoc( handler ) );
		}
	}
	
}
