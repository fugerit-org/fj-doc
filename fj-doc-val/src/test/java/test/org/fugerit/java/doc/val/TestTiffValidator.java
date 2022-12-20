package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.Test;

public class TestTiffValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.TIFF_VALIDATOR
	);
	
	// note : only supported for java 9+
	@Test
	public void testTiffAsTiff() {
		this.worker(FACADE, "tiff_as_tiff.tiff", true );
	}
	@Test
	public void testTiffAsTiffExtTif() {
		this.worker(FACADE, "tiff_as_tiff.tif", true );
	}
	
}
