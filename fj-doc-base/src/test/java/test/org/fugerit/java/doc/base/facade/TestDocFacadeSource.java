package test.org.fugerit.java.doc.base.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.facade.DocFacadeSourceConfig;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.BasicTest;

public class TestDocFacadeSource extends BasicTest {

	private boolean textSupportedParser( boolean expected, int sourceType ) {
		return SafeFunction.get( () -> {
			boolean supported = DocFacadeSource.getInstance().isSourceSupported(sourceType);
			assertEquals( "Failed doc parser supported check", expected , supported );
			return supported;
		} );
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
		Assert.assertFalse( this.textSupportedParser( false, DocFacadeSource.SOURCE_TYPE_JSON ) );
	}
	
	@Test
	public void testParserYaml() {
		Assert.assertFalse( this.textSupportedParser( false, DocFacadeSource.SOURCE_TYPE_YAML ) );
	}
	
	@Test
	public void testParseKO1() throws IOException {
		try ( Reader reader = new StringReader( "<doc>" ) ) {
			DocFacadeSource facade = DocFacadeSource.getInstance();
			assertThrows( ConfigRuntimeException.class , () -> facade.parseRE(reader, DocFacadeSource.SOURCE_TYPE_XML) );
		}
	}
	
	@Test
	public void testParseKO2() throws IOException {
		try ( Reader reader = new StringReader( "{}" ) ) {
			DocFacadeSource facade = DocFacadeSource.getInstance();
			assertThrows( DocException.class , () -> facade.parse(reader, DocFacadeSource.SOURCE_TYPE_JSON) );
		}
	}
	
	@Test
	public void testParseKO3() throws IOException {
		DocFacadeSource facade = new DocFacadeSource( new DocFacadeSourceConfig( true ) );
		assertThrows( ConfigRuntimeException.class , () -> facade.getParserForSource(DocFacadeSource.SOURCE_TYPE_JSON_NG) );
	}
	
	@Test
	public void testParseKO4() throws IOException {
		DocFacadeSource facade = new DocFacadeSource( new DocFacadeSourceConfig().withFailOnSourceModuleNotFound(true) );
		Assert.assertNull( facade.getParserForSource(-1) );
	}

	@Test
	public void textCleanInput() {
		String input = "test";
		Assert.assertEquals( input, DocFacadeSource.cleanSource( input, DocFacadeSource.SOURCE_TYPE_XML ) );
		Assert.assertEquals( input, DocFacadeSource.cleanSource( input, DocFacadeSource.SOURCE_TYPE_JSON ) );
		Assert.assertEquals( input, DocFacadeSource.cleanSource( input, DocFacadeSource.SOURCE_TYPE_YAML ) );
	}

}
