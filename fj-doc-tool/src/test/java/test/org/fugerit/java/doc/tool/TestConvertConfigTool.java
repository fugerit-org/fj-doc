package test.org.fugerit.java.doc.tool;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TestConvertConfigTool extends TestDocTool {

	private static final Object[][] PARAMS = {
			{ "src/test/resources/params-test/convert-config-fop.properties", true },
			{ "src/test/resources/params-test/convert-config-sample.properties", true },
			{ "src/test/resources/params-test/convert-config-autodoc.properties", true },
			{ "src/test/resources/params-test/convert-config-help.properties", true },
			{ "src/test/resources/params-test/convert-config-yaml.properties", true },
	};
	
	@SuppressWarnings("rawtypes")
	@Parameterized.Parameters
	public static Collection primeNumbers() {
		return Arrays.asList( PARAMS );
	}

	private String path;
	
	private boolean expectedResult;

	public TestConvertConfigTool(String path, boolean exptectedResult ) {
		this.path = path;
		this.expectedResult = exptectedResult;
	}

	@Test
	public void testCurrent() {
		boolean ok = this.docToolWorker(this.path);
		Assert.assertEquals( this.expectedResult , ok );
	}

}
