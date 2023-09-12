package test.org.fugerit.java.doc.base.facade;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.junit.Assert;
import org.junit.Test;

import test.org.fugerit.java.BasicTest;

public class TestDocFacade extends BasicTest {

	public static final boolean VALID = true;
	public static final boolean NOT_VALID = false;
	
	public static final boolean NO_EXCEPTION = false;
	public static final boolean EXCEPTION = true;

	
	private void testValidateWorker( String path, boolean valid, boolean exception ) {
		runTestEx(() -> {
			try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( path ) ) ) {
				Properties params = new Properties();
				boolean validResult = DocFacade.validate( reader , params );
				Assert.assertEquals( "Validation result" , valid, validResult );
			}
		});
	}
	
	@Test
	public void testOk01() {
		this.testValidateWorker( "sample/doc_test_01.xml", VALID, NO_EXCEPTION );
	}

	@Test
	public void testKo02() {
		this.testValidateWorker( "sample/doc_test_02_ko.xml", NOT_VALID, NO_EXCEPTION );
	}
	
//	@Test
//	public void testCoverageOk01() {
//		this.testValidateWorker( "coverage/default_doc.xml", VALID, NO_EXCEPTION );
//	}
	
}
