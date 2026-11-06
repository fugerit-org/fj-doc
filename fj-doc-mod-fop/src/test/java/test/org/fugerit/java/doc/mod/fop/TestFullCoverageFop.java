package test.org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandler;
import org.fugerit.java.doc.mod.fop.InitFopHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.org.fugerit.java.BasicTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

@Slf4j
class TestFullCoverageFop extends BasicTest {

    private static final String FULL_COVERAGE = "full-coverage";
    private static final File OUTPUT_FILE = new File("target", FULL_COVERAGE + "-fop.pdf");
    private static final File FO_FILE = new File("target", FULL_COVERAGE + "-fop.fo");

    @BeforeAll
    static void init() {
        SafeFunction.apply(InitFopHandler::initDocAsync);
    }

    private static void renderDoc() throws Exception {
        if (!OUTPUT_FILE.exists()) {
            String inputXml = "xml/" + FULL_COVERAGE + ".xml";
            // First dump FO for inspection/comparison
            DocTypeHandler foHandler = FreeMarkerFopTypeHandler.HANDLER;
            try (InputStreamReader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader(inputXml));
                 FileOutputStream fos = new FileOutputStream(FO_FILE)) {
                foHandler.handle(DocInput.newInput(foHandler.getType(), reader), DocOutput.newOutput(fos));
            }

            DocTypeHandler handler = PdfFopTypeHandler.HANDLER;
            try (InputStreamReader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader(inputXml));
                 FileOutputStream fos = new FileOutputStream(OUTPUT_FILE)) {
                handler.handle(DocInput.newInput(handler.getType(), reader), DocOutput.newOutput(fos));
            }
        }
    }

    @Test
    void testFopMetadataAndBookmarks() throws Exception {
        renderDoc();
        Assertions.assertTrue(OUTPUT_FILE.exists());
        try (PDDocument pdfDoc = PDDocument.load(OUTPUT_FILE)) {
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
    void testFopExtractedText() throws Exception {
        renderDoc();
        Assertions.assertTrue(OUTPUT_FILE.exists());
        try (PDDocument pdfDoc = PDDocument.load(OUTPUT_FILE)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String rawText = stripper.getText(pdfDoc);
            log.info("Extracted FOP PDF text length: {}", rawText.length());
            String pdfText = rawText.replaceAll("\\s+", " ");

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
