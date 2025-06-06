package test.org.fugerit.java.doc.pdfbox.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.pdf.box.PdfboxStrictValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestPdfboxStrictValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			PdfboxStrictValidator.DEFAULT
	);
	
	@Test
	void testPdfAsPdf() {
		boolean ok = this.worker(FACADE, "pdf_as_pdf.pdf", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testPngAsPdf() {
		boolean ok = this.worker(FACADE, "png_as_pdf.pdf", false );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testPdfAsPdfP7M() {
		boolean ok = this.worker(FACADE, "pdf_as_pdf_p7m.pdf", false );
		Assertions.assertTrue( ok );
	}
	
}
