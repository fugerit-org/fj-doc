package test.org.fugerit.java.doc.base.facade;

import static org.junit.Assert.assertEquals;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDocFacadeSourceYaml {

	private static final Logger logger = LoggerFactory.getLogger( TestDocFacadeSourceYaml.class );
	
	private boolean textSupportedParser( boolean expected, int sourceType ) {
		boolean supported;
		try {
			supported = DocFacadeSource.getInstance().isSourceSupported(sourceType);
			assertEquals( "Failed doc parser supported check", expected , supported );
			return supported;
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			return !expected;
		}
	}
		
	@Test
	public void testParserXml() {
		Assert.assertTrue( this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_XML ) );
	}
	
	@Test
	public void textParserDefault() {
		Assert.assertTrue( this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_DEFAULT ) );
	}
	
	@Test
	public void testParserJson() {
		Assert.assertTrue( this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_JSON ) );
	}
	
	@Test
	public void testParserYaml() {
		Assert.assertTrue( this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_YAML ) );
	}
	
}
