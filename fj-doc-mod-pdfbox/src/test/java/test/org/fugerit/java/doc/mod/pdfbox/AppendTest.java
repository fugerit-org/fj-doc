package test.org.fugerit.java.doc.mod.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.junit.Test;

public class AppendTest extends BasicPdfBoxTest {

	private int posX = -10;
	
	private int posY = -10;
	
	private void addText( PDPageContentStream content, PDFont font, int fontSize, PDPage page, String message ) throws Exception {
		 PDRectangle pageSize = page.getMediaBox();
         float stringHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() * fontSize;
         content.setTextMatrix( Matrix.getTranslateInstance( 0, -100 + pageSize.getHeight() - stringHeight / 1000f ) );
	}
	
	private void addPhrase( PDDocument document, PDPage page, String message ) throws Exception {
		try ( PDPageContentStream content = new PDPageContentStream( document , page, AppendMode.APPEND, false ) ) {
			content.beginText();
			PDFont font = PDType1Font.HELVETICA_BOLD;
			int fontSize = 10;
		    addText(content, font, fontSize, page, message);
			content.setFont( font , fontSize );
			content.showText( message );
			content.endText();
		}
	}
	
	@Test
	public void testAppend01() throws Exception {
		try ( PDDocument document = new PDDocument() ) {
			PDPage page = new PDPage( PDRectangle.A4 );
			addPhrase( document , page, "Phrase 01" );
			addPhrase( document , page, "Phrase 02" );
			addPhrase( document , page, "Phrase 03" );
			addPhrase( document , page, "Phrase 04" );
			document.addPage( page );
			document.save( this.getOutFile( "append_01.pdf" ) );
		}
	}

}
