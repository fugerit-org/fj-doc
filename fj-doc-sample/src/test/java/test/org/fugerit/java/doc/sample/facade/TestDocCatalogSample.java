package test.org.fugerit.java.doc.sample.facade;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.util.collection.ListMapStringKey;
import org.fugerit.java.doc.sample.facade.DocCatalogEntry;
import org.fugerit.java.doc.sample.facade.DocCatalogSample;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDocCatalogSample {

	private static final Logger logger = LoggerFactory.getLogger( TestDocCatalogSample.class );
	
	@Test
	public void testConfig() {
		try {
			DocCatalogSample catalog = DocCatalogSample.getInstance();
			assertNotNull( "catalog must exists", catalog );
			ListMapStringKey<DocCatalogEntry> list = catalog.getListMap( "playground-core" );
			DocCatalogEntry entry = list.get( "default" );
			assertNotNull( "default entry must exists", entry );
			String content = StreamIO.readString( catalog.entryReader(entry) );
			logger.info( "entry -> {}, content -> {}", entry, content );
			assertNotNull( "entry content must exists", content );
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message ,e );
			fail( message );
		}
	}
	
}
