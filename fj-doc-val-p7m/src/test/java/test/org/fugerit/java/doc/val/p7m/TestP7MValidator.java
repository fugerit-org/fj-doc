package test.org.fugerit.java.doc.val.p7m;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.p7m.P7MLegacyValidator;
import org.fugerit.java.doc.val.p7m.P7MPemValidator;
import org.fugerit.java.doc.val.p7m.P7MRawValidator;
import org.fugerit.java.doc.val.p7m.P7MValidator;
import org.junit.Assert;
import org.junit.Test;

public class TestP7MValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( P7MValidator.DEFAULT );

	private static final DocValidatorFacade FACADE_LEGACY = DocValidatorFacade.newFacadeStrict( new P7MLegacyValidator() );
	
	@Test
	public void testP7MAsP7M() {
		String path = "pdf_as_pdf.p7m";
		Assert.assertTrue( this.worker(FACADE_LEGACY, path, true ) );
		Assert.assertTrue( this.worker(FACADE, path, true ) );

	}
	
	@Test
	public void testPNGAsP7M() {
		String path = "pdf_as_png.p7m";
		Assert.assertTrue( this.worker(FACADE_LEGACY, path, false ) );
		Assert.assertTrue( this.worker(FACADE, path, false ) );
	}

	@Test
	public void testPkcs7Ok1() {
		String path = "pkcs7_test_ok1.p7m";
		Assert.assertTrue( this.worker(FACADE_LEGACY, path, true ) );
		Assert.assertTrue( this.worker(FACADE, path, true ) );
	}

	@Test
	public void testPkcs7Ok2() {
		String path = "pkcs7_test_ok2.p7m";
		Assert.assertTrue( this.worker(FACADE_LEGACY, path, true ) );
		Assert.assertTrue( this.worker(FACADE, path, true ) );
	}

	@Test
	public void testPkcs7Ko1() {
		String path = "pkcs7_test_ko1.p7m";
		Assert.assertTrue( this.worker(FACADE_LEGACY, path, false ) );
		Assert.assertTrue( this.worker(FACADE, path, false ) );
	}

	@Test
	public void testP7MAsP7MRaValidator() {
		Assert.assertTrue( this.worker(
				DocValidatorFacade.newFacadeStrict( new P7MRawValidator() ), "pdf_as_pdf.p7m", true
		) );
	}

	@Test
	public void testPkcs7Ok1PemValidator() {
		Assert.assertTrue( this.worker(
				DocValidatorFacade.newFacadeStrict( new P7MPemValidator() ), "pkcs7_test_ok1.p7m", true
		) );
	}

}
