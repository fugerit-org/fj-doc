package test.org.fugerit.java.doc.mod.openpdf.ext;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfFontHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.PdfHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestPdfHelper {

	@Test
	void testSetGet() {
		PdfHelper helper = new PdfHelper( null );
		log.info( "getCurrentPageNumber -> {}", helper.getCurrentPageNumber() );
		int current = 10;
		helper.setPageNumberAlignment(current);
		log.info( "getPageNumberAlignment -> {}", helper.getPageNumberAlignment() );
		Assertions.assertEquals( current , helper.getPageNumberAlignment() );
	}
	
	
	@Test
	void testTandleElementSafe() {
		Assertions.assertThrows( NullPointerException.class , () -> OpenPpfDocHandler.handleElementsSafe(null, null, null) );
	}
	
	@Test
	void testCreateFontSafe() {
		Assertions.assertThrows( RuntimeException.class , () -> OpenPdfFontHelper.createBaseFontSafe(null, null, false) );
	}
	
	@Test
	void testCreateFontSafe1() {
		Assertions.assertThrows( ConfigRuntimeException.class , () -> OpenPdfFontHelper.createBaseFontSafe( "no", "no", false) );
	}
}
