package test.org.fugerit.java.doc.tool;

import org.junit.Assert;
import org.junit.Test;

public class TestGenerateStubTool extends TestDocTool {
	
	@Test
	public void testGenerateStub001() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/generate-stub-001.properties" );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testGenerateStub002() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/generate-stub-002.properties" );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testGenerateStubHelp() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/generate-stub-help.properties" );
		Assert.assertTrue( ok );
	}
	
}
