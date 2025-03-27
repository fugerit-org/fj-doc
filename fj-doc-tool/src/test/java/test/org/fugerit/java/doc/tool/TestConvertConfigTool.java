package test.org.fugerit.java.doc.tool;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TestConvertConfigTool extends TestDocTool {

	private static Stream<Arguments> provideConfigs() {
		return Stream.of(
				Arguments.of("src/test/resources/params-test/convert-config-fop.properties", true),
				Arguments.of("src/test/resources/params-test/convert-config-sample.properties", true),
				Arguments.of("src/test/resources/params-test/convert-config-autodoc.properties", true),
				Arguments.of("src/test/resources/params-test/convert-config-help.properties", true),
				Arguments.of("src/test/resources/params-test/convert-config-yaml.properties", true)
		);
	}

	@ParameterizedTest
	@MethodSource("provideConfigs")
	void testCurrent(String path, boolean expectedResult) {
		boolean ok = this.docToolWorker(path);
		Assertions.assertEquals(expectedResult, ok);
	}

}
