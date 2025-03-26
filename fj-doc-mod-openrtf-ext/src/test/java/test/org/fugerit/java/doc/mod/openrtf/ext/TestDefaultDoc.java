package test.org.fugerit.java.doc.mod.openrtf.ext;

import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;

import com.lowagie.text.pdf.BaseFont;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDefaultDoc extends TestDocBase {

	private static final String CUSTOM_FONT = "TitilliumWeb";

	private static final String DEFAULT_DOC = "default_doc";
	
	private static final String DEFAULT_DOC_ALT = "default_doc_alt";
	
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
	void testOpenRTF() {
		boolean ok = this.testDocWorker( DEFAULT_DOC ,  DocConfig.TYPE_RTF );
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
	void testOpenAltRTF() {
		boolean ok = this.testDocWorker( DEFAULT_DOC_ALT ,  DocConfig.TYPE_RTF );
		Assertions.assertTrue(ok);
	}
	
}
