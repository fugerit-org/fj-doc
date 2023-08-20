package test.org.fugerit.java.doc.base.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDocFacadeSourceJson {

	private static final Logger logger = LoggerFactory.getLogger( TestDocFacadeSourceJson.class );
	
	private void textSupportedParser( boolean expected, int sourceType ) {
		boolean supported;
		try {
			supported = DocFacadeSource.getInstance().isSourceSupported(sourceType);
			assertEquals( "Failed doc parser supported check", expected , supported );
		} catch (Exception e) {
			String message = "Error : "+e;
			logger.error( message, e );
			fail( message );
		}
		
	}
		
	@Test
	public void testParserXml() {
		this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_XML );
	}
	
	@Test
	public void textParserDefault() {
		this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_DEFAULT );
	}
	
	@Test
	public void testParserJson() {
		this.textSupportedParser( true, DocFacadeSource.SOURCE_TYPE_JSON );
	}
	
	@Test
	public void testParserYaml() {
		this.textSupportedParser( false, DocFacadeSource.SOURCE_TYPE_YAML );
	}
	
}
