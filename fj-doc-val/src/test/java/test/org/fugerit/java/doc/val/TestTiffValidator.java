package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestTiffValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.TIFF_VALIDATOR
	);
	
	// note : only supported for java 9+
	@Test
	public void testTiffAsTiff() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tiff", true );
		Assert.assertTrue( ok );
	}
	@Test
	public void testTiffAsTiffExtTif() {
		boolean ok = this.worker(FACADE, "tiff_as_tiff.tif", true );
		Assert.assertTrue( ok );
	}
	
}
