package test.org.fugerit.java.doc.sample.dev;

import java.io.File;

import org.fugerit.java.doc.base.config.DocConfig;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestHtml01 extends DevHelper {
	
	@Test
	public void test01() throws Exception {
		String testCase = "test-xml-01";
		boolean res = this.workerXmlToHtml( new File( "src/test/resources/dev/"+testCase+"."+DocConfig.TYPE_XML ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+"."+DocConfig.TYPE_HTML) );
		Assert.assertTrue( res );
	}
}
