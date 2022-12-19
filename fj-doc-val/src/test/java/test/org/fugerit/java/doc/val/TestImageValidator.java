package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.Test;

public class TestImageValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR,
			ImageValidator.TIFF_VALIDATOR
	);
	
	@Test
	public void testJpgAsJpg() {
		this.worker(FACADE, "jpg_as_jpg.jpg", true );
	}
	
	@Test
	public void testJpgAsJpgExtJpeg() {
		this.worker(FACADE, "jpg_as_jpg.jpeg", true );
	}
	
	@Test
	public void testPdfAsJpg() {
		this.worker(FACADE, "pdf_as_jpg.jpg", false );
	}

	@Test
	public void testPngAsPng() {
		this.worker(FACADE, "png_as_png.png", true );
	}

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
