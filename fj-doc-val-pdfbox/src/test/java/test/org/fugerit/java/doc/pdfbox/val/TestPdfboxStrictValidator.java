package test.org.fugerit.java.doc.pdfbox.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.pdf.box.PdfboxStrictValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestPdfboxStrictValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			PdfboxStrictValidator.DEFAULT
	);
	
	@Test
	public void testPdfAsPdf() {
		boolean ok = this.worker(FACADE, "pdf_as_pdf.pdf", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testPngAsPdf() {
		boolean ok = this.worker(FACADE, "png_as_pdf.pdf", false );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testPdfAsPdfP7M() {
		boolean ok = this.worker(FACADE, "pdf_as_pdf_p7m.pdf", false );
		Assert.assertTrue( ok );
	}
	
}
