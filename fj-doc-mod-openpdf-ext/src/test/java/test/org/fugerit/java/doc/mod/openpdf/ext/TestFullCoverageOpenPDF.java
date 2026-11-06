package test.org.fugerit.java.doc.mod.openpdf.ext;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.io.FileIO;
import org.fugerit.java.doc.base.config.DocConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestFullCoverageOpenPDF extends TestDocBase {

	private static final String FULL_COVERAGE = "full-coverage";

	private void checkMetadataAndBookmarks( PDDocument pdfDoc ) {
		PDDocumentInformation info = pdfDoc.getDocumentInformation();
		Assertions.assertNotNull( info );
		Assertions.assertEquals( "Full Coverage Venus Doc", info.getTitle() );
		Assertions.assertEquals( "Comprehensive test case for Venus Doc Source Document features", info.getSubject() );
		Assertions.assertEquals( "fugerit79", info.getAuthor() );
		Assertions.assertEquals( "Venus Doc Test Suite", info.getCreator() );

		PDDocumentOutline outline = pdfDoc.getDocumentCatalog().getDocumentOutline();
		Assertions.assertNotNull( outline );
		PDOutlineItem firstBookmark = outline.getFirstChild();
		Assertions.assertNotNull( firstBookmark );
		Assertions.assertEquals( "Main Title H1", firstBookmark.getTitle() );
		PDOutlineItem secondBookmark = firstBookmark.getNextSibling();
		Assertions.assertNotNull( secondBookmark );
		Assertions.assertEquals( "Typography Section", secondBookmark.getTitle() );
	}

	private void checkExtractedText( String pdfText ) {
		Assertions.assertTrue( pdfText.contains( "Header Left" ) );
		Assertions.assertTrue( pdfText.contains( "Main Title H1" ) );
		Assertions.assertTrue( pdfText.contains( "Section 1: Typography (H2)" ) );
		Assertions.assertTrue( pdfText.contains( "Heading Level 3" ) );
		Assertions.assertTrue( pdfText.contains( "Heading Level 7" ) );
		Assertions.assertTrue( pdfText.contains( "Paragraph with Times-Roman Bold Left" ) );
		Assertions.assertTrue( pdfText.contains( "external link phrase" ) );
		Assertions.assertTrue( pdfText.contains( "Luthien Tinuviel" ) );
		Assertions.assertTrue( pdfText.contains( "Nested 1" ) );
		Assertions.assertTrue( pdfText.contains( "Inline Cell Left" ) );
		Assertions.assertTrue( pdfText.contains( "Default list item 1 (para)" ) );
		Assertions.assertTrue( pdfText.contains( "Ordered letter list item 1" ) );
		Assertions.assertTrue( pdfText.contains( "Phrase list item:" ) );
		Assertions.assertTrue( pdfText.contains( "Section 2: Lists" ) );
		Assertions.assertTrue( pdfText.contains( "Footer Left" ) );
	}

	@Test
	void testOpenPDFFullCoverage() {
		boolean ok = this.testDocWorker( FULL_COVERAGE, DocConfig.TYPE_PDF );
		Assertions.assertTrue( ok );
		File pdfFile = new File( "target", FULL_COVERAGE + "." + DocConfig.TYPE_PDF );
		Assertions.assertTrue( pdfFile.exists() );
		SafeFunction.apply( () -> {
			try ( PDDocument pdfDoc = PDDocument.load( pdfFile ) ) {
				Assertions.assertTrue( pdfDoc.getNumberOfPages() >= 2 );
				this.checkMetadataAndBookmarks( pdfDoc );
				PDFTextStripper stripper = new PDFTextStripper();
				String pdfText = stripper.getText( pdfDoc );
				log.info( "Extracted PDF text length: {}", pdfText.length() );
				this.checkExtractedText( pdfText );
			}
		} );
	}

	@Test
	void testOpenHTMLFullCoverage() {
		boolean ok = this.testDocWorker( FULL_COVERAGE, DocConfig.TYPE_HTML );
		Assertions.assertTrue( ok );
		File htmlFile = new File( "target", FULL_COVERAGE + "." + DocConfig.TYPE_HTML );
		Assertions.assertTrue( htmlFile.exists() );
		String html = SafeFunction.get( () -> FileIO.readString( htmlFile ) );
		Assertions.assertTrue( html.contains( "Main Title H1" ) );
		Assertions.assertTrue( html.contains( "Inline Cell Left" ) );
	}

}
