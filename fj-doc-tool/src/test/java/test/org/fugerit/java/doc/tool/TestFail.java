package test.org.fugerit.java.doc.tool;

import java.io.IOException;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cli.ArgUtils;
import org.fugerit.java.doc.tool.DocTool;
import org.fugerit.java.doc.tool.handler.GenerateStubHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestFail extends TestDocTool {
	
	private static final Exception DEF = new IOException( "e" );
	
	@Test
	void testFail01() {
		Assertions.assertThrows( ConfigRuntimeException.class , () ->  this.docToolWorker( "src/test/resources/params-test/test-fail-01.properties" ) );
	}
	
	@Test
	void testFail02() {
		Assertions.assertThrows( ConfigRuntimeException.class , () ->  this.docToolWorker( "src/test/resources/params-test/test-fail-02.properties" ) );
	}

	@Test
	void testFail03() {
		Assertions.assertThrows( ConfigRuntimeException.class , () ->  this.docToolWorker( "src/test/resources/params-test/test-fail-03.properties" ) );
	}
	
	@Test
	void testExHandler() {
		Assertions.assertThrows( ConfigRuntimeException.class , () -> GenerateStubHandler.EX_HANDLER.accept( DEF ) );
	}
	
	@Test
	void testMain01() {
		DocTool.main( null );
		Assertions.assertTrue( true );
	}
	
	@Test
	void testMain02() {
		String[] args = {
				ArgUtils.getArgString(  DocTool.ARG_TOOL ),
				"generate-stub",
				ArgUtils.getArgString( "input" ),
				"src/test/resources/convert-config-test/doc-process-autodoc.xml",
				ArgUtils.getArgString( "output" ),
				"target/autodoc-new-process-config.xml"
		};
		DocTool.main( args );
		Assertions.assertTrue( true );
	}
	
}
