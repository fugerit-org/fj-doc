package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.poi.DocValidator;
import org.fugerit.java.doc.val.poi.DocxValidator;
import org.fugerit.java.doc.val.poi.XlsValidator;
import org.fugerit.java.doc.val.poi.XlsxValidator;
import org.junit.Test;

public class TestPoiValidator extends TestDocValidatorFacade {

	private static final DocValidatorFacade FACADE = DocValidatorFacade.newFacadeStrict(
			DocxValidator.DEFAULT,
			DocValidator.DEFAULT,
			XlsxValidator.DEFAULT,
			XlsValidator.DEFAULT
	);
	
	@Test
	public void testDocAsDoc() {
		this.worker(FACADE, "doc_as_doc.doc", true );
	}
	
	@Test
	public void testDocxAsDocx() {
		this.worker(FACADE, "docx_as_docx.docx", true );
	}
	
	@Test
	public void testXlsAsXls() {
		this.worker(FACADE, "xls_as_xls.xls", true );
	}
	
	@Test
	public void testXlsxAsXlsx() {
		this.worker(FACADE, "xlsx_as_xlsx.xlsx", true );
	}

	@Test
	public void testDocxAsDoc() {
		this.worker(FACADE, "docx_as_doc.doc", false );
	}
	
	@Test
	public void testJpgAsXlsx() {
		this.worker(FACADE, "jpg_as_xlsx.xlsx", false );
	}

	@Test
	public void testXlsxAsXls() {
		this.worker(FACADE, "xlsx_as_xls.xls", false );
	}	
	
}
