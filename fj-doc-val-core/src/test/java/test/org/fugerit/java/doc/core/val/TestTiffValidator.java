package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTiffValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.TIFF_VALIDATOR
	);
	
	// note : only supported for java 9+
	@Test
	void testTiffAsTiff() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tiff", true );
		Assertions.assertTrue( ok );
	}
	@Test
	void testTiffAsTiffExtTif() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tif", true );
		Assertions.assertTrue( ok );
	}
	
}
