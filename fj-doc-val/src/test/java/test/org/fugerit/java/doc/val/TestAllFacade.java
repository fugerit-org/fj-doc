package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxValidator;
import org.junit.Test;

public class TestAllFacade extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			PdfboxValidator.DEFAULT,
			ImageValidator.JPG_VALIDATOR,
			ImageValidator.PNG_VALIDATOR,
			ImageValidator.TIFF_VALIDATOR
	);
	
	@Test
	public void testPdfAsPdf() {
		this.worker(FACADE, "pdf_as_pdf.pdf", true );
	}
	
	@Test
	public void testPngAsPdf() {
		this.worker(FACADE, "png_as_pdf.pdf", false );
	}
	
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
