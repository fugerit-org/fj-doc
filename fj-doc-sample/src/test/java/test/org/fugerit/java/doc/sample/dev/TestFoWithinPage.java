package test.org.fugerit.java.doc.sample.dev;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

import java.io.File;

class TestFoWithinPage extends DevHelper {

	@Test
	void test01KeepTogether1WithinSpace() throws Exception {
		String testCase = "test-fo-01-keep-together-1";
		boolean res = this.workerFoToPdf( new File( "src/test/resources/dev/"+testCase+".fo" ),
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+".pdf" ) );
		Assertions.assertTrue( res );
	}

	@Test
	void test01KeepTogether2WithinSpace() throws Exception {
		String testCase = "test-fo-01-keep-together-2";
		boolean res = this.workerFoToPdf( new File( "src/test/resources/dev/"+testCase+".fo" ),
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+".pdf" ) );
		Assertions.assertTrue( res );
	}

}
