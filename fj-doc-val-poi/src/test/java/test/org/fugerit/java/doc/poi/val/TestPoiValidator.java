package test.org.fugerit.java.doc.poi.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.poi.DocValidator;
import org.fugerit.java.doc.val.poi.DocxValidator;
import org.fugerit.java.doc.val.poi.XlsValidator;
import org.fugerit.java.doc.val.poi.XlsxValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestPoiValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict( 
			DocValidator.DEFAULT,
			DocxValidator.DEFAULT,
			XlsValidator.DEFAULT,
			XlsxValidator.DEFAULT
	);
	
	@Test
	void testDocAsDoc() {
		boolean ok = this.worker(FACADE, "doc_as_doc.doc", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testDocxAsDocx() {
		boolean ok = this.worker(FACADE, "docx_as_docx.docx", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testDocxAsDoc() {
		boolean ok = this.worker(FACADE, "docx_as_doc.doc", false );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testDocAsDocx() {
		boolean ok = this.worker(FACADE, "doc_as_docx.docx", false );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testXlsAsXls() {
		boolean ok = this.worker(FACADE, "xls_as_xls.xls", true );
		Assertions.assertTrue( ok );
	}
	
	@Test
	void testXlsxAsXlsx() {
		boolean ok = this.worker(FACADE, "xlsx_as_xlsx.xlsx", true );
		Assertions.assertTrue( ok );
	}

	@Test
	void testJpgAsXlsx() {
		boolean ok = this.worker(FACADE, "jpg_as_xlsx.xlsx", false );
		Assertions.assertTrue( ok );
	}

	@Test
	void testXlsxAsXls() {
		boolean ok = this.worker(FACADE, "xlsx_as_xls.xls", false );
		Assertions.assertTrue( ok );
	}	
	
}
