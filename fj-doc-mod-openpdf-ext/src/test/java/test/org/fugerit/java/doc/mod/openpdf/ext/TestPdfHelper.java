package test.org.fugerit.java.doc.mod.openpdf.ext;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfFontHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPpfDocHandler;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.PdfHelper;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestPdfHelper {

	@Test
	public void testSetGet() {
		PdfHelper helper = new PdfHelper( null );
		log.info( "getCurrentPageNumber -> {}", helper.getCurrentPageNumber() );
		int current = 10;
		helper.setPageNumberAlignment(current);
		log.info( "getPageNumberAlignment -> {}", helper.getPageNumberAlignment() );
		Assert.assertEquals( current , helper.getPageNumberAlignment() );
	}
	
	
	@Test
	public void testTandleElementSafe() {
		Assert.assertThrows( NullPointerException.class , () -> OpenPpfDocHandler.handleElementsSafe(null, null, null) );
	}
	
	@Test
	public void testCreateFontSafe() {
		Assert.assertThrows( RuntimeException.class , () -> OpenPdfFontHelper.createBaseFontSafe(null, null, false) );
	}
	
	@Test
	public void testCreateFontSafe1() {
		Assert.assertThrows( ConfigRuntimeException.class , () -> OpenPdfFontHelper.createBaseFontSafe( "no", "no", false) );
	}
}
