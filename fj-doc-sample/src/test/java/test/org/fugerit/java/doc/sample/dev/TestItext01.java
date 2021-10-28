package test.org.fugerit.java.doc.sample.dev;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestItext01 extends DevHelperItext {
		
	@Test
	public void test01() throws Exception {
		String testCase = "test-xml-02";
		//ITextDocHandler.registerFont( "default-font", "/dev/arial.ttf");
		boolean res = this.workerItext( new File( "src/test/resources/dev/"+testCase+".xml" ),
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+".pdf" ));
		Assert.assertTrue( res );
	}
	
}
