package test.org.fugerit.java.doc.base.config;

import org.fugerit.java.doc.base.config.DocCharsetProvider;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class TestDocCharsetProvider {

	@Test
	void testProvider() {
		DocCharsetProvider.setDefaultProvider( DocCharsetProvider.getDefaultProvider() );
		log.info( "test : {}", DocCharsetProvider.getDefaultProvider() );
		Assertions.assertNotNull( DocCharsetProvider.getDefaultProvider() );
	}
	
}
