package test.org.fugerit.java.doc.base.facade;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.facade.DocFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class TestDocFacade {

	public static final boolean VALID = true;
	public static final boolean NOT_VALID = false;
	
	public static final boolean NO_EXCEPTION = false;
	public static final boolean EXCEPTION = true;

	
	private boolean testValidateWorker( String path, boolean valid, boolean exception ) {
		return SafeFunction.get(() -> {
			try ( Reader reader = new InputStreamReader( ClassHelper.loadFromDefaultClassLoader( path ) ) ) {
				log.info( "path : {}, valid: {}, exception: {} ", path, valid, exception );
				Properties params = new Properties();
				boolean validResult = DocFacade.validate( reader , params );
				Assertions.assertEquals( valid, validResult );
				return true;
			}
		});
	}
	
	@Test
	void testOk01() {
		Assertions.assertTrue( this.testValidateWorker( "sample/doc_test_01.xml", VALID, NO_EXCEPTION ) );
	}

	@Test
	void testKo02() {
		Assertions.assertTrue( this.testValidateWorker( "sample/doc_test_02_ko.xml", NOT_VALID, NO_EXCEPTION ) );
	}
	
}
