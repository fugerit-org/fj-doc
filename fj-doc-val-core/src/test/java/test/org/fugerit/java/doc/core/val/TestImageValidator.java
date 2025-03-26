package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestImageValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR,
			ImageValidator.TIFF_VALIDATOR
	);
	
	@Test
	void testJpgAsJpg() {
		boolean ok = this.worker(FACADE, "jpg_as_jpg.jpg", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testJpgAsJpgExtJpeg() {
		boolean ok = this.worker(FACADE, "jpg_as_jpg.jpeg", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testPdfAsJpg() {
		boolean ok = this.worker(FACADE, "pdf_as_jpg.jpg", false );
		Assertions.assertTrue( ok );
	}

	@Test
	void testPngAsPng() {
		boolean ok = this.worker(FACADE, "png_as_png.png", true );
		Assertions.assertTrue( ok );
	}

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
