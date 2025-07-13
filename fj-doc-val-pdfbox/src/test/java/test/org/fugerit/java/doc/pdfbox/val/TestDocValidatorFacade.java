package test.org.fugerit.java.doc.pdfbox.val;

import java.io.InputStream;

import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.val.core.DocTypeValidationResult;
import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hsqldb.types.Type.ReType.check;

class TestDocValidatorFacade {

	private static final Logger logger = LoggerFactory.getLogger( TestDocValidatorFacade.class );
	
	private static final String BASE_PATH = "sample";
	
	protected boolean  worker( DocValidatorFacade facade, String fileName, boolean result ) {
		return SafeFunction.get( () -> {
			String path = BASE_PATH+"/"+fileName;
			logger.info( "test path {}, expected result {}", path, result );
			try ( InputStream is = ClassHelper.loadFromDefaultClassLoader( path ) ) {
				DocTypeValidationResult validationResult = facade.validate(fileName, is);
				logger.info( "validation message : {}", validationResult.getValidationMessage() );
				boolean check = validationResult.isResultOk();
				Assertions.assertEquals( result, check );
				return ( result == check );
			}
		} );
	}
	
}
