package test.org.fugerit.java.doc.val.p7m;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.p7m.P7MLegacyValidator;
import org.fugerit.java.doc.val.p7m.P7MPemValidator;
import org.fugerit.java.doc.val.p7m.P7MRawValidator;
import org.fugerit.java.doc.val.p7m.P7MValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class  TestP7MValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( P7MValidator.DEFAULT );

	private static final DocValidatorFacade FACADE_LEGACY = DocValidatorFacade.newFacadeStrict( new P7MLegacyValidator() );
	
	@Test
	void testP7MAsP7M() {
		String path = "pdf_as_pdf.p7m";
		Assertions.assertTrue( this.worker(FACADE_LEGACY, path, true ) );
		Assertions.assertTrue( this.worker(FACADE, path, true ) );

	}
	
	@Test
	void testPNGAsP7M() {
		String path = "pdf_as_png.p7m";
		Assertions.assertTrue( this.worker(FACADE_LEGACY, path, false ) );
		Assertions.assertTrue( this.worker(FACADE, path, false ) );
	}

	@Test
	void testPkcs7Ok1() {
		String path = "pkcs7_test_ok1.p7m";
		Assertions.assertTrue( this.worker(FACADE_LEGACY, path, true ) );
		Assertions.assertTrue( this.worker(FACADE, path, true ) );
	}

	@Test
	void testPkcs7Ok2() {
		String path = "pkcs7_test_ok2.p7m";
		Assertions.assertTrue( this.worker(FACADE_LEGACY, path, true ) );
		Assertions.assertTrue( this.worker(FACADE, path, true ) );
	}

	@Test
	void testPkcs7Ko1() {
		String path = "pkcs7_test_ko1.p7m";
		Assertions.assertTrue( this.worker(FACADE_LEGACY, path, false ) );
		Assertions.assertTrue( this.worker(FACADE, path, false ) );
	}

	@Test
	void testP7MAsP7MRaValidator() {
		Assertions.assertTrue( this.worker(
				DocValidatorFacade.newFacadeStrict( new P7MRawValidator() ), "pdf_as_pdf.p7m", true
		) );
	}

	@Test
	void testPkcs7Ok1PemValidator() {
		Assertions.assertTrue( this.worker(
				DocValidatorFacade.newFacadeStrict( new P7MPemValidator() ), "pkcs7_test_ok1.p7m", true
		) );
	}

}
