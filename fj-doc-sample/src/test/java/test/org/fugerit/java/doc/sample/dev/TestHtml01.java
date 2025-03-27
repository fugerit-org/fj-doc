package test.org.fugerit.java.doc.sample.dev;

import java.io.File;

import org.fugerit.java.doc.base.config.DocConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

class TestHtml01 extends DevHelper {
	
	@Test
	void test01() throws Exception {
		String testCase = "test-xml-01";
		boolean res = this.workerXmlToHtml( new File( "src/test/resources/dev/"+testCase+"."+DocConfig.TYPE_XML ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+"."+DocConfig.TYPE_HTML) );
		Assertions.assertTrue( res );
	}
}
