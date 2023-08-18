package test.org.fugerit.java.doc.tool;

import org.junit.Assert;
import org.junit.Test;

public class TestGenerateHelperTool extends TestDocTool {
	
	@Test
	public void testGenerateHelper() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/test-generate-helper.properties" );
		Assert.assertTrue( ok );
	}
	
}
