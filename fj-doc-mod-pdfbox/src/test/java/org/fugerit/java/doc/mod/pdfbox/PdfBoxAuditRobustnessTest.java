package org.fugerit.java.doc.mod.pdfbox;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.model.DocBarcode;
import org.fugerit.java.doc.base.model.DocBase;
import org.fugerit.java.doc.base.model.DocImage;
import org.fugerit.java.doc.base.model.DocPageBreak;
import org.fugerit.java.doc.base.model.DocPara;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.org.fugerit.java.BasicTest;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class PdfBoxAuditRobustnessTest extends BasicTest {

    private static DocBase buildProgrammaticMultiPageDoc() {
        DocBase doc = new DocBase();

        DocPara headerPara = new DocPara();
        headerPara.setText("Header Line 1\nHeader Line 2");
        DocImage logo = new DocImage();
        logo.setUrl("cl://test/img_test_blue.png");
        doc.getDocHeader().addElement(headerPara);
        doc.getDocHeader().addElement(logo);

        DocPara p1 = new DocPara();
        p1.setText("Page 1 body");
        p1.setForeColor("not-a-hex-color");
        p1.setBackColor("#eeeeee");
        doc.getDocBody().addElement(p1);

        DocBarcode emptyBarcode = new DocBarcode();
        doc.getDocBody().addElement(emptyBarcode);

        doc.getDocBody().addElement(new DocPageBreak());

        DocPara p2 = new DocPara();
        p2.setText("Page 2 body");
        doc.getDocBody().addElement(p2);

        return doc;
    }

    private static COSBase firstXObject(PDDocument doc, int pageIndex) {
        COSDictionary xObjects = doc.getPage(pageIndex)
                .getResources()
                .getCOSObject()
                .getCOSDictionary(COSName.XOBJECT);
        Assertions.assertNotNull(xObjects, "Expected XObject dictionary on page " + pageIndex);
        Assertions.assertFalse(xObjects.keySet().isEmpty(), "Expected at least one XObject on page " + pageIndex);
        return xObjects.getDictionaryObject(xObjects.keySet().iterator().next());
    }

    @Test
    void testConcurrentDeserializedHandlerAndSharedDocBase() throws Exception {
        Properties props = new Properties();
        props.setProperty(PdfBoxConfig.PDFA_ENABLED, "true");
        PdfBoxTypeHandler original = new PdfBoxTypeHandler();
        original.configure(props);

        PdfBoxTypeHandler deserialized = (PdfBoxTypeHandler) this.fullSerializationTest(original);
        Assertions.assertNotNull(deserialized);

        final DocBase sharedDoc = buildProgrammaticMultiPageDoc();
        int threads = 8;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<byte[]>> futures = new ArrayList<>();
        try {
            for (int i = 0; i < threads; i++) {
                final PdfBoxTypeHandler target = (i % 2 == 0) ? original : deserialized;
                futures.add(pool.submit(() -> {
                    start.await(10, TimeUnit.SECONDS);
                    ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
                    target.handle(DocInput.newInput(DocConfig.TYPE_PDF, sharedDoc), DocOutput.newOutput(pdfOut));
                    return pdfOut.toByteArray();
                }));
            }
            start.countDown();
            for (Future<byte[]> f : futures) {
                byte[] pdf = f.get(30, TimeUnit.SECONDS);
                try (PDDocument loaded = Loader.loadPDF(pdf)) {
                    Assertions.assertEquals(2, loaded.getNumberOfPages());
                    Assertions.assertSame(firstXObject(loaded, 0), firstXObject(loaded, 1),
                            "Header image XObject should be deduplicated across pages");
                }
            }
        } finally {
            pool.shutdownNow();
        }
        Assertions.assertNull(sharedDoc.getStableInfo(), "Handler must not mutate shared DocBase");
    }

    @Test
    void testNullInputOutputAndConfigGuards() {
        PdfBoxTypeHandler handler = new PdfBoxTypeHandler();
        DocOutput validOutput = DocOutput.newOutput(new ByteArrayOutputStream());
        DocInput validInput = DocInput.newInput(DocConfig.TYPE_PDF, new DocBase());

        Assertions.assertThrows(IllegalArgumentException.class, () -> handler.handle(null, validOutput));
        Assertions.assertThrows(IllegalArgumentException.class, () -> handler.handle(validInput, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> handler.handle(validInput, DocOutput.newOutput(null)));

        PdfBoxConfig nullPropsCfg = PdfBoxConfig.fromProperties(null);
        Assertions.assertNotNull(nullPropsCfg);
        Assertions.assertEquals("Untitled", nullPropsCfg.getDocumentTitle());
        Assertions.assertEquals(":regular", PdfBoxConfig.buildCustomFontKey(null, null));
    }

    @Test
    void testPdfAPreservesHardenedProducerAndCreator() throws Exception {
        Properties props = new Properties();
        props.setProperty(PdfBoxConfig.PDFA_ENABLED, "true");
        PdfBoxTypeHandler handler = new PdfBoxTypeHandler();
        handler.configure(props);

        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<doc xmlns=\"http://javacoredoc.fugerit.org\">"
                + "  <metadata>"
                + "    <info name=\"doc-title\">Hardened PDF/A</info>"
                + "    <info name=\"doc-creator\">Custom Creator</info>"
                + "    <info name=\"security-hardening\">1</info>"
                + "  </metadata>"
                + "  <body><para>Hello PDF/A</para></body>"
                + "</doc>";

        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
        try (StringReader reader = new StringReader(xml)) {
            handler.handle(DocInput.newInput(DocConfig.TYPE_PDF, reader), DocOutput.newOutput(pdfOut));
        }

        try (PDDocument doc = Loader.loadPDF(pdfOut.toByteArray())) {
            PDDocumentInformation info = doc.getDocumentInformation();
            Assertions.assertEquals("Custom Creator", info.getCreator());
            Assertions.assertNotNull(info.getProducer());
            Assertions.assertFalse(info.getProducer().contains("3."),
                    "SECURITY_HARDENING_1 must suppress PDFBox version in producer: " + info.getProducer());
            String xmp = new String(doc.getDocumentCatalog().getMetadata().toByteArray(), StandardCharsets.UTF_8);
            Assertions.assertTrue(xmp.contains("Custom Creator"), "XMP CreatorTool should match Info Creator");
        }
    }

    @Test
    void testFontManagerCacheKeyNoCollisionBetweenDefaultAndExplicitHelvetica() throws Exception {
        PdfBoxConfig config = new PdfBoxConfig();
        config.setFontClasspath("font");
        config.setDefaultFontName("TitilliumWeb");
        config.registerCustomFont("helvetica", PdfBoxConfig.FONT_STYLE_REGULAR, "font/TitilliumWeb-Bold.ttf");

        try (PDDocument doc = new PDDocument()) {
            FontManager fm = new FontManager(config, doc);
            PDFont defaultFont = fm.getFont(FontManager.STYLE_NORMAL, null);
            PDFont explicitHelvetica = fm.getFont(FontManager.STYLE_NORMAL, "helvetica");
            PDFont explicitHelveticaUpper = fm.getFont(FontManager.STYLE_NORMAL, "Helvetica");

            Assertions.assertTrue(defaultFont.getName().contains("TitilliumWeb-Regular"),
                    "Expected default TitilliumWeb-Regular, got: " + defaultFont.getName());
            Assertions.assertTrue(explicitHelvetica.getName().contains("TitilliumWeb-Bold"),
                    "Expected explicit helvetica override TitilliumWeb-Bold, got: " + explicitHelvetica.getName());
            Assertions.assertSame(explicitHelvetica, explicitHelveticaUpper);
        }
    }
}
