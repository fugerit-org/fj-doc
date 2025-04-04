package test.org.fugerit.java.doc.base.facade;

import java.io.IOException;
import java.io.InputStream;

import org.fugerit.java.core.cfg.xml.FactoryCatalog;
import org.fugerit.java.core.cfg.xml.GenericListCatalogConfig;
import org.fugerit.java.core.io.helper.StreamHelper;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.facade.DocHandlerFacade;
import org.fugerit.java.doc.base.facade.DocHandlerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDocHandlerFactory {

	private static final String XML_CONFIG_PATH = "coverage/config/doc-handler-sample.xml";
	
	private static final String TEST_CATALOG_ID = "test";
	
	private static final String DEFAULT_CATALOG_ID = "default-complete";
	
	@Test
	void testFacatoryUseCatalog() {
		DocHandlerFacade facade = DocHandlerFactory.register( StreamHelper.PATH_CLASSLOADER+XML_CONFIG_PATH, TEST_CATALOG_ID );
		Assertions.assertNotNull( facade.findHandler( DocConfig.TYPE_MD ) );
	}
	
	@Test
	void testFacatoryDefaultCatalog() {
		DocHandlerFacade facade = DocHandlerFactory.register( StreamHelper.PATH_CLASSLOADER+XML_CONFIG_PATH );
		Assertions.assertNotNull( facade.findHandler( DocConfig.TYPE_MD ) );
	}
	
	@Test
	void testNewInstace() {
		DocHandlerFactory factory = DocHandlerFactory.newInstance( StreamHelper.PATH_CLASSLOADER+XML_CONFIG_PATH );
		Assertions.assertEquals( DEFAULT_CATALOG_ID ,  factory.getUseCatalog() );
		log.info( "hash code : {}", factory.hashCode() );
		boolean eqCheck = factory.equals( factory );
		Assertions.assertTrue( eqCheck );
	}
	
	@Test
	void testFromCatalog() throws IOException {
		FactoryCatalog catalog = new FactoryCatalog();
		try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( XML_CONFIG_PATH ) ) {
			GenericListCatalogConfig.load( is , catalog );
			Assertions.assertNotNull( DocHandlerFactory.register( catalog.getDataList( DEFAULT_CATALOG_ID ) ) );
		}
	}
	
}
