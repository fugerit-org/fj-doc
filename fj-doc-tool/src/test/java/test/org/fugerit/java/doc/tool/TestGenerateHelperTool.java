package test.org.fugerit.java.doc.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestGenerateHelperTool extends TestDocTool {
	
	@Test
	void testGenerateHelper() {
		boolean ok = this.docToolWorker( "src/test/resources/params-test/test-generate-helper.properties" );
		Assertions.assertTrue( ok );
	}
	
}
