package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.pdf.box.PdfboxValidator;
import org.junit.Test;

public class TestPdfBoxValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			PdfboxValidator.DEFAULT
	);
	
	@Test
	public void testPdfAsPdf() {
		this.worker(FACADE, "pdf_as_pdf.pdf", true );
	}
	
	@Test
	public void testPngAsPdf() {
		this.worker(FACADE, "png_as_pdf.pdf", false );
	}
	
}
