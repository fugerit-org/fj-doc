package test.org.fugerit.java.doc.val.p7m;

import java.io.InputStream;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDocValidatorFacade {

	private static final Logger logger = LoggerFactory.getLogger( TestDocValidatorFacade.class );
	
	private static final String BASE_PATH = "sample";
	
	protected boolean  worker( DocValidatorFacade facade, String fileName, boolean result ) {
		return SafeFunction.get( () -> {
			String path = BASE_PATH+"/"+fileName;
			logger.info( "test path {}, expected result {}", path, result );
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( path ) ) {
				boolean check = facade.check(fileName, is);
				Assert.assertEquals( "File check failed", result, check );
				return ( result == check );
			}
		} );
	}
	
}
