package test.org.fugerit.java.doc.base.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.doc.base.config.DocException;
import org.fugerit.java.doc.base.facade.DocFacadeSource;
import org.fugerit.java.doc.base.facade.DocFacadeSourceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDocFacadeSource {

	private boolean textSupportedParser( boolean expected, int sourceType ) {
		return SafeFunction.get( () -> {
			boolean supported = DocFacadeSource.getInstance().isSourceSupported(sourceType);
			assertEquals( expected , supported );
			return supported;
		} );
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
		Assertions.assertFalse( this.textSupportedParser( false, DocFacadeSource.SOURCE_TYPE_JSON ) );
	}
	
	@Test
	void testParserYaml() {
		Assertions.assertFalse( this.textSupportedParser( false, DocFacadeSource.SOURCE_TYPE_YAML ) );
	}
	
	@Test
	void testParseKO1() throws IOException {
		try ( Reader reader = new StringReader( "<doc>" ) ) {
			DocFacadeSource facade = DocFacadeSource.getInstance();
			assertThrows( ConfigRuntimeException.class , () -> facade.parseRE(reader, DocFacadeSource.SOURCE_TYPE_XML) );
		}
	}
	
	@Test
	void testParseKO2() throws IOException {
		try ( Reader reader = new StringReader( "{}" ) ) {
			DocFacadeSource facade = DocFacadeSource.getInstance();
			assertThrows( DocException.class , () -> facade.parse(reader, DocFacadeSource.SOURCE_TYPE_JSON) );
		}
	}
	
	@Test
	void testParseKO3() throws IOException {
		DocFacadeSource facade = new DocFacadeSource( new DocFacadeSourceConfig( true ) );
		assertThrows( ConfigRuntimeException.class , () -> facade.getParserForSource(DocFacadeSource.SOURCE_TYPE_JSON_NG) );
	}
	
	@Test
	void testParseKO4() throws IOException {
		DocFacadeSource facade = new DocFacadeSource( new DocFacadeSourceConfig().withFailOnSourceModuleNotFound(true) );
		Assertions.assertNull( facade.getParserForSource(-1) );
	}

	@Test
	void textCleanInputStream() throws IOException {
		String input = "test";
		assertEquals( input, StreamIO.readString( DocFacadeSource.cleanSource( new StringReader(input), DocFacadeSource.SOURCE_TYPE_XML ) ) );
		assertEquals( input, StreamIO.readString( DocFacadeSource.cleanSource( new StringReader(input), DocFacadeSource.SOURCE_TYPE_JSON ) ) );
		assertEquals( input, StreamIO.readString( DocFacadeSource.cleanSource( new StringReader(input), DocFacadeSource.SOURCE_TYPE_YAML ) ) );
	}

	@Test
	void textCleanInput() {
		String input = "test";
		assertEquals( input, DocFacadeSource.cleanSource( input, DocFacadeSource.SOURCE_TYPE_XML ) );
		assertEquals( input, DocFacadeSource.cleanSource( input, DocFacadeSource.SOURCE_TYPE_JSON ) );
		assertEquals( input, DocFacadeSource.cleanSource( input, DocFacadeSource.SOURCE_TYPE_YAML ) );
	}

}
