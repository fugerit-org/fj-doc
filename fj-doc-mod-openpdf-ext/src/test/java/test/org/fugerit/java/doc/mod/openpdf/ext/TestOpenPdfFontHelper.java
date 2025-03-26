package test.org.fugerit.java.doc.mod.openpdf.ext;

import java.io.IOException;

import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfFontHelper;
import org.fugerit.java.doc.mod.openpdf.ext.helpers.OpenPdfHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;

class TestOpenPdfFontHelper {

	@Test
	void testCreateFont() throws DocumentException, IOException {
		Font font = OpenPdfFontHelper.createFont( "TestTitilliumWeb" , "src/test/resources/font/TitilliumWeb-Regular.ttf", 10, Font.NORMAL, new OpenPdfHelper(), null );
		Assertions.assertNotNull( font );
	}
	
}
