package test.org.fugerit.java.doc.sample.dev;

import java.io.File;
import java.util.Locale;

import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.base.typehandler.markdown.SimpleMarkdownExtTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import test.org.fugerit.java.doc.sample.facade.BasicFacadeTest;

public class TestMarkdown01 extends DevHelper {
	
	@Test
	void test01() throws Exception {
		Locale.setDefault( Locale.UK );
		String testCase = "test-xml-01";
		DocTypeHandler handler = SimpleMarkdownExtTypeHandler.HANDLER;
		boolean res = this.workerXmlToHandler( new File( "src/test/resources/dev/"+testCase+".xml" ), 
				new File( BasicFacadeTest.BASIC_OUTPUT_PATH, testCase+"."+handler.getType() ), handler );
		Assertions.assertTrue( res );
	}
	
}
