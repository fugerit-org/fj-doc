package test.org.fugerit.java.doc.base.facade;

import static org.junit.Assert.assertEquals;

import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.junit.Test;

import test.org.fugerit.java.BasicTest;

public class TestDocFacadeSource extends BasicTest {

	private void textSupportedParser( boolean expected, int sourceType ) {
		runTestEx( () -> {
			boolean supported = DocFacadeSource.getInstance().isSourceSupported(sourceType);
			assertEquals( "Failed doc parser supported check", expected , supported );
		} );
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
		this.textSupportedParser( false, DocFacadeSource.SOURCE_TYPE_JSON );
	}
	
	@Test
	public void testParserYaml() {
		this.textSupportedParser( false, DocFacadeSource.SOURCE_TYPE_YAML );
	}
	
}
