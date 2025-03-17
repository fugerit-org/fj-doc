package test.org.fugerit.java.doc.base.facade;


import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDocFacadeSourceYaml {

	private static final Logger logger = LoggerFactory.getLogger( TestDocFacadeSourceYaml.class );
	
	private boolean textSupportedParser( boolean expected, int sourceType ) {
		boolean supported;
		try {
			supported = DocFacadeSource.getInstance().isSourceSupported(sourceType);
			Assertions.assertEquals( expected , supported );
			return supported;
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			return !expected;
		}
	}
		
	@Test
	void testParserXml() {
		Assertions.assertTrue( this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_XML ) );
	}
	
	@Test
	void textParserDefault() {
		Assertions.assertTrue( this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_DEFAULT ) );
	}
	
	@Test
	void testParserJson() {
		Assertions.assertTrue( this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_JSON ) );
	}
	
	@Test
	void testParserYaml() {
		Assertions.assertTrue( this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_YAML ) );
	}
	
}
