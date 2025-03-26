package test.org.fugerit.java.doc.mod.openpdf.ext;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.lowagie.text.pdf.BaseFont;

class TestDefaultDoc extends TestDocBase {

	private static final String CUSTOM_FONT = "TitilliumWeb";

	private static final String DEFAULT_DOC = "default_doc";
	
	private static final String DEFAULT_DOC_ALT = "default_doc_alt";
	
	private static final String DEFAULT_DOC_SIMPLE = "default_doc_simple";
	
	private static final String DEFAULT_DOC_PDFA = "default_doc_pdfa";
	
	@Test
	void testOpenFailPDF() {
		Assertions.assertThrows( AssertionError.class , () -> this.testDocWorker( "default_doc_fail1" ,  DocConfig.TYPE_PDF ) );
	}
	
	@Test
	void testCustomFont() {
		BaseFont font = OpenPpfDocHandler.findFont( CUSTOM_FONT );
		Assertions.assertNotNull(font);
	}
	
	@Test
	void testOpenPDF() {
		boolean ok = this.testDocWorker( DEFAULT_DOC ,  DocConfig.TYPE_PDF );
		Assertions.assertTrue(ok);
	}

	@Test
	void testOpenHTML() {
		boolean ok = this.testDocWorker( DEFAULT_DOC ,  DocConfig.TYPE_HTML );
		Assertions.assertTrue(ok);
	}
	
	@Test
	void testOpenAltPDF() {
		boolean ok = this.testDocWorker( DEFAULT_DOC_ALT ,  DocConfig.TYPE_PDF );
		Assertions.assertTrue(ok);
	}

	@Test
	void testOpenAltHTML() {
		boolean ok = this.testDocWorker( DEFAULT_DOC_ALT ,  DocConfig.TYPE_HTML );
		Assertions.assertTrue(ok);
	}
	
	@Test
	void testOpenSimple() {
		boolean ok = this.testDocWorker( DEFAULT_DOC_SIMPLE ,  DocConfig.TYPE_PDF );
		Assertions.assertTrue(ok);
	}
	
	@Test
	void testOpenPDFA() {
		// still working on font embedding
		Assertions.assertThrows( AssertionError.class , () -> this.testDocWorker( DEFAULT_DOC_PDFA ,  DocConfig.TYPE_PDF ));
	}
	
}
