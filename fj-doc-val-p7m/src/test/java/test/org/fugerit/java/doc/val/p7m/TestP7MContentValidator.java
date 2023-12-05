package test.org.fugerit.java.doc.val.p7m;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.core.basic.ImageValidator;
import org.fugerit.java.doc.val.p7m.P7MContentValidator;
import org.fugerit.java.doc.val.pdf.box.PdfboxStrictValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestP7MContentValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE_PDF = DocValidatorFacade.newFacadeStrict( 
			P7MContentValidator.newValidator( DocValidatorFacade.newFacade( PdfboxStrictValidator.DEFAULT ) )
	);
	
	private static final DocValidatorFacade FACADE_JPG = DocValidatorFacade.newFacadeStrict( 
			P7MContentValidator.newValidator( DocValidatorFacade.newFacade( ImageValidator.JPG_VALIDATOR ) )
	);
	
	private static final DocValidatorFacade FACADE_NULL = DocValidatorFacade.newFacadeStrict( 
			P7MContentValidator.newValidator( null )
	);
	
	@Test
	public void testP7MAsP7M() {
		boolean ok = this.worker(FACADE_PDF, "pdf_as_pdf.p7m", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testP7MAsP7MNull() {
		boolean ok = this.worker(FACADE_NULL, "pdf_as_pdf.p7m", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testP7MAsP7MKo() {
		boolean ok = this.worker(FACADE_JPG, "pdf_as_pdf.p7m", false );
		Assert.assertTrue( ok );
	}
	
}
