package test.org.fugerit.java.doc.val.p7m;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.p7m.P7MValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestP7MValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			P7MValidator.DEFAULT
	);
	
	@Test
	public void testP7MAsP7M() {
		boolean ok = this.worker(FACADE, "pdf_as_pdf.p7m", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testPNGAsP7M() {
		boolean ok = this.worker(FACADE, "png_as_p7m.p7m", false );
		Assert.assertTrue( ok );
	}

	@Test
	public void testPkcs7Ok1() {
		boolean ok = this.worker(FACADE, "pkcs7_test_ok1.p7m", true );
		Assert.assertTrue( ok );
	}

	@Test
	public void testPkcs7Ok2() {
		boolean ok = this.worker(FACADE, "pkcs7_test_ok2.p7m", true );
		Assert.assertTrue( ok );
	}

	@Test
	public void testPkcs7Ko1() {
		boolean ok = this.worker(FACADE, "pkcs7_test_ko1.p7m", false );
		Assert.assertTrue( ok );
	}

}
