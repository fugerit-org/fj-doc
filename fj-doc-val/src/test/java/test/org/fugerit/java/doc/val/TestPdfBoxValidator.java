package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.pdf.box.PdfboxValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestPdfBoxValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			PdfboxValidator.DEFAULT
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
	
}
