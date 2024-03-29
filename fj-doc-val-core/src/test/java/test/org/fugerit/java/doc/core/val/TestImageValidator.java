package test.org.fugerit.java.doc.core.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestImageValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR,
			ImageValidator.TIFF_VALIDATOR
	);
	
	@Test
	public void testJpgAsJpg() {
		boolean ok = this.worker(FACADE, "jpg_as_jpg.jpg", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testJpgAsJpgExtJpeg() {
		boolean ok = this.worker(FACADE, "jpg_as_jpg.jpeg", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testPdfAsJpg() {
		boolean ok = this.worker(FACADE, "pdf_as_jpg.jpg", false );
		Assert.assertTrue( ok );
	}

	@Test
	public void testPngAsPng() {
		boolean ok = this.worker(FACADE, "png_as_png.png", true );
		Assert.assertTrue( ok );
	}

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
