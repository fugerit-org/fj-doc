package test.org.fugerit.java.doc.tool;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TestGenerateStubTool extends TestDocTool {

	private static Stream<Arguments> provideConfigs() {
		return Stream.of(
				Arguments.of("src/test/resources/params-test/generate-stub-001.properties", true),
				Arguments.of("src/test/resources/params-test/generate-stub-002.properties", true),
				Arguments.of("src/test/resources/params-test/generate-stub-help.properties", true)
		);
	}

	@ParameterizedTest
	@MethodSource("provideConfigs")
	void testCurrent(String path, boolean expectedResult) {
		boolean ok = this.docToolWorker(path);
		Assertions.assertEquals(expectedResult, ok);
	}

}
