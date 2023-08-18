package test.org.fugerit.java.doc.tool;

import org.junit.Assert;
import org.junit.Test;

public class TestConvertConfigTool extends TestDocTool {
	
	@Test
	public void testConvertConfigFop() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/convert-config-fop.properties" );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testConvertConfigSample() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/convert-config-sample.properties" );
		Assert.assertTrue( ok );
	}

	@Test
	public void testConvertConfigAutodoc() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/convert-config-autodoc.properties" );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testConvertConfigHelp() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/convert-config-help.properties" );
		Assert.assertTrue( ok );
	}
	
}
