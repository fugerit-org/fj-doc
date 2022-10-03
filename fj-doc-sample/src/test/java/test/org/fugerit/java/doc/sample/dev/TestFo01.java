package test.org.fugerit.java.doc.sample.dev;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestFo01 extends DevHelper {
	
	@Test
	public void test01() throws Exception {
		String testCase = "test-fo-01";
		boolean res = this.workerFoToPdf( new File( "src/test/resources/dev/"+testCase+".fo" ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+".pdf" ) );
		Assert.assertTrue( res );
	}
}
