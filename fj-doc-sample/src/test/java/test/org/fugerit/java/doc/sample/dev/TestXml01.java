package test.org.fugerit.java.doc.sample.dev;

import java.io.File;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestXml01 extends DevHelper {
	
	@Test
	public void test01() throws Exception {
		Locale.setDefault( Locale.UK );
		String testCase = "test-xml-01";
		boolean res = this.workerXmlToFoToPdf( new File( "src/test/resources/dev/"+testCase+".xml" ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+".fo" ),
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+".pdf" ));
		Assert.assertTrue( res );
	}
}
