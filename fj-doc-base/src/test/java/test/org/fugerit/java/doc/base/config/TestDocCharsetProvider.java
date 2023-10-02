package test.org.fugerit.java.doc.base.config;

import org.fugerit.java.doc.base.config.DocCharsetProvider;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocCharsetProvider {

	@Test
	public void testProvider() {
		DocCharsetProvider.setDefaultProvider( DocCharsetProvider.getDefaultProvider() );
		log.info( "test : {}", DocCharsetProvider.getDefaultProvider() );
	}
	
}
