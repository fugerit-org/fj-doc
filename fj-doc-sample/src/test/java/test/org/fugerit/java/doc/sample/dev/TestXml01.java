package test.org.fugerit.java.doc.sample.dev;

import java.io.File;
import java.util.Locale;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestXml01 extends DevHelper {
	
	@Test
	public void test01() throws Exception {
		Locale.setDefault( Locale.UK );
		String testCase = "test-xml-01";
		DocTypeHandler handler = PdfFopTypeHandler.HANDLER;
		boolean res = this.workerXmlToHandler( new File( "src/test/resources/dev/"+testCase+".xml" ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+"."+handler.getType() ), handler );
		Assert.assertTrue( res );
	}
}
