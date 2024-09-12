package test.org.fugerit.java.doc.mod.openpdf.ext;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.junit.Assert;
import org.junit.Test;

import com.lowagie.text.pdf.BaseFont;

public class TestDefaultDoc extends TestDocBase {

	private static final String CUSTOM_FONT = "TitilliumWeb";

	private static final String DEFAULT_DOC = "default_doc";
	
	private static final String DEFAULT_DOC_ALT = "default_doc_alt";
	
	private static final String DEFAULT_DOC_SIMPLE = "default_doc_simple";
	
	private static final String DEFAULT_DOC_PDFA = "default_doc_pdfa";
	
	@Test
	public void testOpenFailPDF() {
		Assert.assertThrows( AssertionError.class , () -> this.testDocWorker( "default_doc_fail1" ,  DocConfig.TYPE_PDF ) );
	}
	
	@Test
	public void testCustomFont() {
		BaseFont font = OpenPpfDocHandler.findFont( CUSTOM_FONT );
		Assert.assertNotNull(font);
	}

	@Test
	public void testAccessibility() {
		boolean ok = this.testDocWorker( "accessibility_test" ,  DocConfig.TYPE_PDF );
		Assert.assertTrue(ok);
	}

	@Test
	public void testOpenPDF() {
		boolean ok = this.testDocWorker( DEFAULT_DOC ,  DocConfig.TYPE_PDF );
		Assert.assertTrue(ok);
	}

	@Test
	public void testOpenHTML() {
		boolean ok = this.testDocWorker( DEFAULT_DOC ,  DocConfig.TYPE_HTML );
		Assert.assertTrue(ok);
	}
	
	@Test
	public void testOpenAltPDF() {
		boolean ok = this.testDocWorker( DEFAULT_DOC_ALT ,  DocConfig.TYPE_PDF );
		Assert.assertTrue(ok);
	}

	@Test
	public void testOpenAltHTML() {
		boolean ok = this.testDocWorker( DEFAULT_DOC_ALT ,  DocConfig.TYPE_HTML );
		Assert.assertTrue(ok);
	}
	
	@Test
	public void testOpenSimple() {
		boolean ok = this.testDocWorker( DEFAULT_DOC_SIMPLE ,  DocConfig.TYPE_PDF );
		Assert.assertTrue(ok);
	}
	
	@Test
	public void testOpenPDFA() {
		// still working on font embedding
		Assert.assertThrows( AssertionError.class , () -> this.testDocWorker( DEFAULT_DOC_PDFA ,  DocConfig.TYPE_PDF ));
	}
	
}
