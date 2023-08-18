package test.org.fugerit.java.doc.val;

import org.fugerit.java.doc.val.core.DocValidatorFacade;
import org.fugerit.java.doc.val.poi.DocValidator;
import org.fugerit.java.doc.val.poi.DocxValidator;
import org.fugerit.java.doc.val.poi.XlsValidator;
import org.fugerit.java.doc.val.poi.XlsxValidator;
import org.junit.Assert;
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
		boolean ok = this.worker(FACADE, "doc_as_doc.doc", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testDocxAsDocx() {
		boolean ok = this.worker(FACADE, "docx_as_docx.docx", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testXlsAsXls() {
		boolean ok = this.worker(FACADE, "xls_as_xls.xls", true );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testXlsxAsXlsx() {
		boolean ok = this.worker(FACADE, "xlsx_as_xlsx.xlsx", true );
		Assert.assertTrue( ok );
	}

	@Test
	public void testDocxAsDoc() {
		boolean ok = this.worker(FACADE, "docx_as_doc.doc", false );
		Assert.assertTrue( ok );
	}
	
	@Test
	public void testJpgAsXlsx() {
		boolean ok = this.worker(FACADE, "jpg_as_xlsx.xlsx", false );
		Assert.assertTrue( ok );
	}

	@Test
	public void testXlsxAsXls() {
		boolean ok = this.worker(FACADE, "xlsx_as_xls.xls", false );
		Assert.assertTrue( ok );
	}	
	
}
