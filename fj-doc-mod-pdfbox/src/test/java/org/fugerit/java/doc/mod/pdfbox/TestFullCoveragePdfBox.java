package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;

@Slf4j
class TestFullCoveragePdfBox {

    private static final String FULL_COVERAGE = "full-coverage";
    private static final File OUTPUT_FILE = new File("target", FULL_COVERAGE + "-pdfbox.pdf");

    @BeforeAll
    static void initPdf() throws Exception {
        PdfBoxTypeHandler handler = new PdfBoxTypeHandler();
        String inputXml = "xml/" + FULL_COVERAGE + ".xml";
        try (InputStreamReader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader(inputXml));
             OutputStream os = Files.newOutputStream(OUTPUT_FILE.toPath())) {
            handler.handle(DocInput.newInput(handler.getType(), reader), DocOutput.newOutput(os));
        }
    }

    @Test
    void testPdfBoxMetadataAndBookmarks() throws Exception {
        Assertions.assertTrue(OUTPUT_FILE.exists());
        try (PDDocument pdfDoc = Loader.loadPDF(OUTPUT_FILE)) {
            Assertions.assertTrue(pdfDoc.getNumberOfPages() >= 2);
            PDDocumentInformation info = pdfDoc.getDocumentInformation();
            Assertions.assertNotNull(info);
            Assertions.assertEquals("Full Coverage Venus Doc", info.getTitle());
            Assertions.assertEquals("Comprehensive test case for Venus Doc Source Document features", info.getSubject());
            Assertions.assertEquals("fugerit79", info.getAuthor());
            Assertions.assertEquals("Venus Doc Test Suite", info.getCreator());

            PDDocumentOutline outline = pdfDoc.getDocumentCatalog().getDocumentOutline();
            Assertions.assertNotNull(outline);
            PDOutlineItem firstBookmark = outline.getFirstChild();
            Assertions.assertNotNull(firstBookmark);
            Assertions.assertEquals("Main Title H1", firstBookmark.getTitle());
            PDOutlineItem secondBookmark = firstBookmark.getNextSibling();
            Assertions.assertNotNull(secondBookmark);
            Assertions.assertEquals("Typography Section", secondBookmark.getTitle());
        }
    }

    @Test
    void testPdfBoxExtractedText() throws Exception {
        Assertions.assertTrue(OUTPUT_FILE.exists());
        try (PDDocument pdfDoc = Loader.loadPDF(OUTPUT_FILE)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(pdfDoc);
            log.info("Extracted PDF text length: {}", pdfText.length());

            Assertions.assertTrue(pdfText.contains("Header Left"));
            Assertions.assertTrue(pdfText.contains("Main Title H1"));
            Assertions.assertTrue(pdfText.contains("Section 1: Typography (H2)"));
            Assertions.assertTrue(pdfText.contains("Heading Level 3"));
            Assertions.assertTrue(pdfText.contains("Heading Level 7"));
            Assertions.assertTrue(pdfText.contains("Paragraph with Times-Roman Bold Left"));
            Assertions.assertTrue(pdfText.contains("external link phrase"));
            Assertions.assertTrue(pdfText.contains("Luthien Tinuviel"));
            Assertions.assertTrue(pdfText.contains("Nested 1"));
            Assertions.assertTrue(pdfText.contains("Inline Cell Left"));
            Assertions.assertTrue(pdfText.contains("Default list item 1 (para)"));
            Assertions.assertTrue(pdfText.contains("Ordered letter list item 1"));
            Assertions.assertTrue(pdfText.contains("Phrase list item:"));
            Assertions.assertTrue(pdfText.contains("Section 2: Lists"));
            Assertions.assertTrue(pdfText.contains("Footer Left"));
        }
    }
}
