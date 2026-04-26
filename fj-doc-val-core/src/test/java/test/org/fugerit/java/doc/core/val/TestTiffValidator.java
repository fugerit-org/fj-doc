package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;

class TestTiffValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.TIFF_VALIDATOR
	);

	@Test
	@EnabledForJreRange(minVersion = 11)
	void testTiffAsTiffExtTif() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tif", Boolean.TRUE );
		Assertions.assertTrue( ok );
	}
	
}
