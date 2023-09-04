package test.org.fugerit.java.doc.sample.dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.mod.openpdf.PdfTypeHandler;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestItext2 extends DevHelper {
	
	@Test
	public void test01FoPdf() throws Exception {
		Locale.setDefault( Locale.UK );
		String testCase = "test-xml-01";
		String type = DocConfig.TYPE_PDF;
		DocTypeHandler handler = PdfTypeHandler.HANDLER;
		try ( InputStream input = new FileInputStream( new File( "src/test/resources/dev/"+testCase+".xml" ) );
				OutputStream output = new FileOutputStream( new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+"_itext."+type ) ) ) {
			DocBase doc = DocFacade.parse( input );
			handler.handle( DocInput.newInput( type, doc) , DocOutput.newOutput( output ) );
		}
		Assert.assertTrue( true );
	}
	
	
}
