package test.org.fugerit.java.doc.tool;

import org.junit.Test;

public class TestGenerateStubTool extends TestDocTool {
	
	@Test
	public void testGenerateStub001() {
		this.docToolWorker( "src/test/resources/params-test/generate-stub-001.properties" );
	}
	
	@Test
	public void testGenerateStub002() {
		this.docToolWorker( "src/test/resources/params-test/generate-stub-002.properties" );
	}
	
	@Test
	public void testGenerateStubHelp() {
		this.docToolWorker( "src/test/resources/params-test/generate-stub-help.properties" );
	}
	
}
