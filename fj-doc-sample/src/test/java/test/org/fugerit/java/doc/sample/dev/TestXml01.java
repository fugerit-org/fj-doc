package test.org.fugerit.java.doc.sample.dev;

import java.io.File;
import java.util.Locale;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.html.FreeMarkerHtmlTypeHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestXml01 extends DevHelper {
	
	//@Test
	public void test01() throws Exception {
		Locale.setDefault( Locale.UK );
		String testCase = "test-xml-01";
		DocTypeHandler handler = PdfFopTypeHandler.HANDLER;
		boolean res = this.workerXmlToHandler( new File( "src/test/resources/dev/"+testCase+".xml" ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+"."+handler.getType() ), handler );
		Assert.assertTrue( res );
	}
	
	@Test
	public void test01FoPdf() throws Exception {
		Locale.setDefault( Locale.UK );
		String testCase = "test-xml-01";
		DocTypeHandler handler = PdfFopTypeHandler.HANDLER;
		boolean res = this.workerXmlToFoToPdf( new File( "src/test/resources/dev/"+testCase+".xml" ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+".fo" ),
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+"."+handler.getType() ) );
		Assert.assertTrue( res );
	}
	
	@Test
	public void test01Html() throws Exception {
		Locale.setDefault( Locale.UK );
		String testCase = "test-xml-01";
		DocTypeHandler handler = FreeMarkerHtmlTypeHandler.HANDLER;
		boolean res = this.workerXmlToHandler( new File( "src/test/resources/dev/"+testCase+".xml" ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+"."+handler.getType() ), handler );
		Assert.assertTrue( res );
	}
	
}
