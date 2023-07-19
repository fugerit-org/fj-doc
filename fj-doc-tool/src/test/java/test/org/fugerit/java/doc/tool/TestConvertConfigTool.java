package test.org.fugerit.java.doc.tool;

import org.junit.Test;

public class TestConvertConfigTool extends TestDocTool {
	
	@Test
	public void testConvertConfigFop() {
		this.docToolWorker( "src/test/resources/params-test/convert-config-fop.properties" );
	}
	
	@Test
	public void testConvertConfigSample() {
		this.docToolWorker( "src/test/resources/params-test/convert-config-sample.properties" );
	}

	@Test
	public void testConvertConfigAutodoc() {
		this.docToolWorker( "src/test/resources/params-test/convert-config-autodoc.properties" );
	}
	
	@Test
	public void testConvertConfigHelp() {
		this.docToolWorker( "src/test/resources/params-test/convert-config-help.properties" );
	}
	
}
