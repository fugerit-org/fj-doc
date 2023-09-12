package test.org.fugerit.java.doc.tool;

import java.io.IOException;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.core.cli.ArgUtils;
import org.fugerit.java.doc.tool.DocTool;
import org.fugerit.java.doc.tool.handler.GenerateStubHandler;
import org.junit.Assert;
import org.junit.Test;

public class TestFail extends TestDocTool {
	
	private static final Exception DEF = new IOException( "e" );
	
	@Test
	public void testFail01() {
		Assert.assertThrows( ConfigRuntimeException.class , () ->  this.docToolWorker( "src/test/resources/params-test/test-fail-01.properties" ) );
	}
	
	@Test
	public void testFail02() {
		Assert.assertThrows( ConfigRuntimeException.class , () ->  this.docToolWorker( "src/test/resources/params-test/test-fail-02.properties" ) );
	}

	@Test
	public void testFail03() {
		Assert.assertThrows( ConfigRuntimeException.class , () ->  this.docToolWorker( "src/test/resources/params-test/test-fail-03.properties" ) );
	}
	
	@Test
	public void testExHandler() {
		Assert.assertThrows( ConfigRuntimeException.class , () -> GenerateStubHandler.EX_HANDLER.accept( DEF ) );
	}
	
	@Test
	public void testMain01() {
		DocTool.main( null );
		Assert.assertTrue( true );
	}
	
	@Test
	public void testMain02() {
		String[] args = {
				ArgUtils.getArgString(  DocTool.ARG_TOOL ),
				"generate-stub",
				ArgUtils.getArgString( "input" ),
				"src/test/resources/convert-config-test/doc-process-autodoc.xml",
				ArgUtils.getArgString( "output" ),
				"target/autodoc-new-process-config.xml"
		};
		DocTool.main( args );
		Assert.assertTrue( true );
	}
	
}
