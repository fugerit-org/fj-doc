package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDMarkInfo;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.fugerit.java.core.io.StreamIO;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfig;
import org.fugerit.java.doc.freemarker.process.FreemarkerDocProcessConfigFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
class PdfBoxEnhancedFeaturesTest {

    private static final String CONFIG_PATH = "cl://fm-test-mod-pdfbox-config.xml";

    private static byte[] renderXml(DocTypeHandler handler, String docXml) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (StringReader reader = new StringReader(docXml)) {
            handler.handle(DocInput.newInput(DocConfig.TYPE_PDF, reader), DocOutput.newOutput(baos));
        }
        return baos.toByteArray();
    }

    private static String buildSampleDocXml(String title, String author, String subject, String language, boolean withBookmarks) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<doc xmlns=\"http://javacoredoc.fugerit.org\">\n");
        sb.append("  <metadata>\n");
        if (title != null) {
            sb.append("    <info name=\"doc-title\">").append(title).append("</info>\n");
        }
        if (author != null) {
            sb.append("    <info name=\"doc-author\">").append(author).append("</info>\n");
        }
        if (subject != null) {
            sb.append("    <info name=\"doc-subject\">").append(subject).append("</info>\n");
        }
        if (language != null) {
            sb.append("    <info name=\"doc-language\">").append(language).append("</info>\n");
        }
        if (withBookmarks) {
            sb.append("    <bookmark-tree>\n");
            sb.append("      <bookmark ref=\"sec-intro\">Introduction</bookmark>\n");
            sb.append("      <bookmark ref=\"cell-anchor\">Table Cell Anchor</bookmark>\n");
            sb.append("      <bookmark ref=\"missing-target-id\">Unresolved Bookmark</bookmark>\n");
            sb.append("    </bookmark-tree>\n");
        }
        sb.append("  </metadata>\n");
        sb.append("  <body>\n");
        sb.append("    <h id=\"sec-intro\" head-level=\"1\" style=\"bold\">Main Heading</h>\n");
        sb.append("    <para style=\"normal\">Regular text paragraph.</para>\n");
        sb.append("    <para style=\"bold\">Bold text paragraph.</para>\n");
        sb.append("    <para style=\"italic\">Italic text paragraph.</para>\n");
        sb.append("    <para style=\"bolditalic\">Bold italic text paragraph.</para>\n");
        sb.append("    <para font-name=\"TitilliumWeb\" style=\"bold\">Named TitilliumWeb bold text.</para>\n");
        if (withBookmarks) {
            sb.append("    <table columns=\"2\" colwidths=\"50;50\" width=\"100\">\n");
            sb.append("      <row>\n");
            sb.append("        <cell><para id=\"cell-anchor\" style=\"bold\">Anchored Cell</para></cell>\n");
            sb.append("        <cell><para style=\"italic\">Value</para></cell>\n");
            sb.append("      </row>\n");
            sb.append("    </table>\n");
            sb.append("    <page-break/>\n");
            sb.append("    <para id=\"sec-page2\" style=\"normal\">Second page content for multi-page PDF/UA check.</para>\n");
        }
        sb.append("  </body>\n");
        sb.append("</doc>\n");
        return sb.toString();
    }

    private static Set<String> collectEmbeddedFontNames(PDDocument pdfDoc) throws IOException {
        Set<String> names = new HashSet<>();
        for (PDPage page : pdfDoc.getPages()) {
            PDResources resources = page.getResources();
            if (resources != null) {
                for (COSName fontKey : resources.getFontNames()) {
                    PDFont font = resources.getFont(fontKey);
                    Assertions.assertNotNull(font, "Font should not be null for key " + fontKey);
                    Assertions.assertTrue(font.isEmbedded(), "Font must be embedded: " + font.getName());
                    names.add(font.getName());
                }
            }
        }
        return names;
    }

    private static void assertAllFourTitilliumStylesPresent(Set<String> fontNames) {
        log.info("Embedded fonts in PDF: {}", fontNames);
        Assertions.assertTrue(fontNames.stream().anyMatch(n -> n.contains("TitilliumWeb-Regular")),
                "Expected TitilliumWeb-Regular in " + fontNames);
        Assertions.assertTrue(fontNames.stream().anyMatch(n -> n.contains("TitilliumWeb-Bold") && !n.contains("Italic")),
                "Expected TitilliumWeb-Bold in " + fontNames);
        Assertions.assertTrue(fontNames.stream().anyMatch(n -> n.contains("TitilliumWeb-Italic")),
                "Expected TitilliumWeb-Italic in " + fontNames);
        Assertions.assertTrue(fontNames.stream().anyMatch(n -> n.contains("TitilliumWeb-BoldItalic")),
                "Expected TitilliumWeb-BoldItalic in " + fontNames);
    }

    @Test
    void testXmlConfigurationPlainPdf() throws Exception {
        FreemarkerDocProcessConfig fmConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe(CONFIG_PATH);
        Assertions.assertNotNull(fmConfig);

        DocTypeHandler plainHandler = fmConfig.getFacade().findHandler("pdf-pdfbox-plain");
        Assertions.assertInstanceOf(PdfBoxTypeHandler.class, plainHandler);
        PdfBoxTypeHandler plain = (PdfBoxTypeHandler) plainHandler;
        Assertions.assertEquals(StandardCharsets.UTF_8, plain.getCharset());
        Assertions.assertFalse(plain.getConfigCopy().isPdfAEnabled());
        Assertions.assertFalse(plain.getConfigCopy().isPdfUAEnabled());
        byte[] plainBytes = renderXml(plain, buildSampleDocXml("Plain Doc", "Author A", "Subject A", "en", false));
        try (PDDocument doc = Loader.loadPDF(plainBytes)) {
            Assertions.assertEquals(1, doc.getNumberOfPages());
            Assertions.assertEquals("Plain Doc", doc.getDocumentInformation().getTitle());
        }
    }

    @Test
    void testXmlConfigurationPdfA1b() throws Exception {
        FreemarkerDocProcessConfig fmConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe(CONFIG_PATH);
        DocTypeHandler pdfAHandler = fmConfig.getFacade().findHandler("pdf-pdfbox-pdf-a");
        Assertions.assertInstanceOf(PdfBoxTypeHandler.class, pdfAHandler);
        PdfBoxTypeHandler pdfA = (PdfBoxTypeHandler) pdfAHandler;
        Assertions.assertEquals("PDF/A-1b", pdfA.getFormat());
        PdfBoxConfig pdfACfg = pdfA.getConfigCopy();
        Assertions.assertTrue(pdfACfg.isPdfAEnabled());
        Assertions.assertFalse(pdfACfg.isPdfUAEnabled());
        Assertions.assertEquals("1b", pdfACfg.getPdfALevel());
        Assertions.assertEquals("en-US", pdfACfg.getDocumentLanguage());
        Assertions.assertEquals("font", pdfACfg.getFontClasspath());
        Assertions.assertEquals("TitilliumWeb", pdfACfg.getDefaultFontName());

        byte[] pdfABytes = renderXml(pdfA, buildSampleDocXml("Archive Doc", "Archivist", "PDF/A test", null, false));
        try (PDDocument doc = Loader.loadPDF(pdfABytes)) {
            Assertions.assertTrue(doc.getVersion() >= 1.4f);
            Assertions.assertFalse(doc.getDocumentCatalog().getOutputIntents().isEmpty());
            PDMetadata meta = doc.getDocumentCatalog().getMetadata();
            Assertions.assertNotNull(meta);
            String xmp = new String(meta.toByteArray(), StandardCharsets.UTF_8);
            Assertions.assertTrue(xmp.contains("pdfaid:part"), "Expected pdfaid:part in XMP: " + xmp);
            Set<String> fonts = collectEmbeddedFontNames(doc);
            assertAllFourTitilliumStylesPresent(fonts);
        }
    }

    @Test
    void testXmlConfigurationPdfUA1() {
        FreemarkerDocProcessConfig fmConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe(CONFIG_PATH);
        DocTypeHandler pdfUAHandler = fmConfig.getFacade().findHandler("pdf-pdfbox-pdf-ua");
        Assertions.assertInstanceOf(PdfBoxTypeHandler.class, pdfUAHandler);
        PdfBoxTypeHandler pdfUA = (PdfBoxTypeHandler) pdfUAHandler;
        Assertions.assertEquals("PDF/UA-1", pdfUA.getFormat());
        PdfBoxConfig pdfUACfg = pdfUA.getConfigCopy();
        Assertions.assertFalse(pdfUACfg.isPdfAEnabled());
        Assertions.assertTrue(pdfUACfg.isPdfUAEnabled());
        Assertions.assertEquals("it", pdfUACfg.getDocumentLanguage());
        Assertions.assertEquals("font", pdfUACfg.getFontClasspath());
        Assertions.assertEquals("TitilliumWeb", pdfUACfg.getDefaultFontName());
    }

    @Test
    void testSequentialMetadataIsolationNoLeakage() throws Exception {
        FreemarkerDocProcessConfig fmConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe(CONFIG_PATH);
        PdfBoxTypeHandler handler = (PdfBoxTypeHandler) fmConfig.getFacade().findHandler("pdf-pdfbox-pdf-ua");

        // Render 1: custom metadata
        byte[] pdf1 = renderXml(handler, buildSampleDocXml("First Title", "First Author", "First Subject", "fr-FR", false));
        try (PDDocument doc1 = Loader.loadPDF(pdf1)) {
            PDDocumentInformation info = doc1.getDocumentInformation();
            Assertions.assertEquals("First Title", info.getTitle());
            Assertions.assertEquals("First Author", info.getAuthor());
            Assertions.assertEquals("First Subject", info.getSubject());
            Assertions.assertEquals("fr-FR", doc1.getDocumentCatalog().getLanguage());
        }

        // Render 2: no metadata specified -> must fall back to handler defaults, NOT leak Render 1
        byte[] pdf2 = renderXml(handler, buildSampleDocXml(null, null, null, null, false));
        try (PDDocument doc2 = Loader.loadPDF(pdf2)) {
            PDDocumentInformation info = doc2.getDocumentInformation();
            Assertions.assertEquals("Untitled", info.getTitle());
            Assertions.assertEquals("Unknown", info.getAuthor());
            Assertions.assertNull(info.getSubject());
            Assertions.assertEquals("it", doc2.getDocumentCatalog().getLanguage());
            String xmp = new String(doc2.getDocumentCatalog().getMetadata().toByteArray(), StandardCharsets.UTF_8);
            Assertions.assertFalse(xmp.contains("First Title"), "Title from first render leaked into XMP");
            Assertions.assertFalse(xmp.contains("First Author"), "Author from first render leaked into XMP");
        }

        // Verify handler config itself was never mutated
        PdfBoxConfig afterCfg = handler.getConfigCopy();
        Assertions.assertEquals("Untitled", afterCfg.getDocumentTitle());
        Assertions.assertEquals("Unknown", afterCfg.getDocumentAuthor());
        Assertions.assertEquals("", afterCfg.getDocumentSubject());
        Assertions.assertEquals("it", afterCfg.getDocumentLanguage());
    }

    @Test
    void testConcurrentMetadataIsolationThreadSafety() throws Exception {
        FreemarkerDocProcessConfig fmConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe(CONFIG_PATH);
        final PdfBoxTypeHandler sharedHandler = (PdfBoxTypeHandler) fmConfig.getFacade().findHandler("pdf-pdfbox-pdf-ua");

        final int threadCount = 8;
        final int iterationsPerThread = 4;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<Void>> futures = new ArrayList<>();

        try {
            for (int t = 0; t < threadCount; t++) {
                final int threadIdx = t;
                Callable<Void> task = () -> {
                    startLatch.await(10, TimeUnit.SECONDS);
                    for (int i = 0; i < iterationsPerThread; i++) {
                        String expectedTitle = "Title-T" + threadIdx + "-I" + i;
                        String expectedAuthor = "Author-T" + threadIdx + "-I" + i;
                        String expectedSubject = "Subject-T" + threadIdx + "-I" + i;
                        String expectedLang = (threadIdx % 2 == 0) ? "en-US" : "de-DE";

                        byte[] pdfBytes = renderXml(sharedHandler,
                                buildSampleDocXml(expectedTitle, expectedAuthor, expectedSubject, expectedLang, false));

                        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
                            PDDocumentInformation info = doc.getDocumentInformation();
                            Assertions.assertEquals(expectedTitle, info.getTitle());
                            Assertions.assertEquals(expectedAuthor, info.getAuthor());
                            Assertions.assertEquals(expectedSubject, info.getSubject());
                            Assertions.assertEquals(expectedLang, doc.getDocumentCatalog().getLanguage());
                            String xmp = new String(doc.getDocumentCatalog().getMetadata().toByteArray(), StandardCharsets.UTF_8);
                            Assertions.assertTrue(xmp.contains(expectedTitle), "XMP missing expected title " + expectedTitle);
                            Assertions.assertTrue(xmp.contains(expectedAuthor), "XMP missing expected author " + expectedAuthor);
                        }
                    }
                    return null;
                };
                futures.add(executor.submit(task));
            }

            startLatch.countDown();
            for (Future<Void> f : futures) {
                f.get(60, TimeUnit.SECONDS);
            }
        } finally {
            executor.shutdownNow();
        }

        // Handler's shared config must still hold original defaults
        PdfBoxConfig finalCfg = sharedHandler.getConfigCopy();
        Assertions.assertEquals("Untitled", finalCfg.getDocumentTitle());
        Assertions.assertEquals("Unknown", finalCfg.getDocumentAuthor());
        Assertions.assertEquals("", finalCfg.getDocumentSubject());
        Assertions.assertEquals("it", finalCfg.getDocumentLanguage());
    }

    @Test
    void testClasspathFontsViaClassloaderPrefixAndNestedXmlElements() throws Exception {
        FreemarkerDocProcessConfig fmConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe(CONFIG_PATH);

        // Via font.directory="classloader://font"
        PdfBoxTypeHandler clDirHandler = (PdfBoxTypeHandler) fmConfig.getFacade().findHandler("pdf-pdfbox-cl-dir");
        byte[] clDirBytes = renderXml(clDirHandler, buildSampleDocXml("CL Dir Fonts", "Tester", "Fonts", "it", false));
        try (PDDocument doc = Loader.loadPDF(clDirBytes)) {
            assertAllFourTitilliumStylesPresent(collectEmbeddedFontNames(doc));
        }

        // Via nested <font .../> elements inside <docHandlerCustomConfig>
        PdfBoxTypeHandler nestedHandler = (PdfBoxTypeHandler) fmConfig.getFacade().findHandler("pdf-pdfbox-nested-fonts");
        byte[] nestedBytes = renderXml(nestedHandler, buildSampleDocXml("Nested Fonts", "Tester", "Fonts", "it", false));
        try (PDDocument doc = Loader.loadPDF(nestedBytes)) {
            assertAllFourTitilliumStylesPresent(collectEmbeddedFontNames(doc));
        }
    }

    @Test
    void testFilesystemFontDirectoryBackwardsCompatibility(@TempDir Path tempFontDir) throws Exception {
        // Copy TitilliumWeb TTF files using lowercase naming convention (custom-regular.ttf, etc.)
        copyResourceToFile("font/TitilliumWeb-Regular.ttf", tempFontDir.resolve("custom-regular.ttf"));
        copyResourceToFile("font/TitilliumWeb-Bold.ttf", tempFontDir.resolve("custom-bold.ttf"));
        copyResourceToFile("font/TitilliumWeb-Italic.ttf", tempFontDir.resolve("custom-italic.ttf"));
        copyResourceToFile("font/TitilliumWeb-BoldItalic.ttf", tempFontDir.resolve("custom-bolditalic.ttf"));

        Properties props = new Properties();
        props.setProperty(PdfBoxConfig.PDFUA_ENABLED, "true");
        props.setProperty(PdfBoxConfig.FONT_DIRECTORY, tempFontDir.toAbsolutePath().toString());
        props.setProperty(PdfBoxConfig.DEFAULT_FONT_NAME, "custom");
        props.setProperty(PdfBoxConfig.FORMAT, "PDF/UA-1");

        PdfBoxTypeHandler handler = new PdfBoxTypeHandler();
        handler.configure(props);
        Assertions.assertEquals("PDF/UA-1", handler.getFormat());

        byte[] pdfBytes = renderXml(handler, buildSampleDocXml("FS Fonts", "Tester", "FS", "en", false));
        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            assertAllFourTitilliumStylesPresent(collectEmbeddedFontNames(doc));
        }
    }

    @Test
    void testPlainPdfWithClasspathFontsAndSingleStyleFallback(@TempDir Path tempFontDir) throws Exception {
        // Plain PDF (non-PDF/A, non-PDF/UA) with classpath fonts should still embed configured custom fonts
        Properties cpProps = new Properties();
        cpProps.setProperty(PdfBoxConfig.FONT_CLASSPATH, "/font/");
        cpProps.setProperty(PdfBoxConfig.DEFAULT_FONT_NAME, "TitilliumWeb");
        PdfBoxTypeHandler plainWithFonts = new PdfBoxTypeHandler();
        plainWithFonts.configure(cpProps);

        byte[] plainBytes = renderXml(plainWithFonts, buildSampleDocXml("Plain Custom Font", "Tester", "Fonts", "en", false));
        try (PDDocument doc = Loader.loadPDF(plainBytes)) {
            assertAllFourTitilliumStylesPresent(collectEmbeddedFontNames(doc));
        }

        // Single-file custom font (only regular exists) should gracefully fall back to regular for bold/italic
        copyResourceToFile("font/TitilliumWeb-Regular.ttf", tempFontDir.resolve("single.ttf"));
        Properties singleProps = new Properties();
        singleProps.setProperty(PdfBoxConfig.PDFUA_ENABLED, "true");
        singleProps.setProperty(PdfBoxConfig.FONT_DIRECTORY, tempFontDir.toAbsolutePath().toString());
        singleProps.setProperty(PdfBoxConfig.DEFAULT_FONT_NAME, "single");
        PdfBoxTypeHandler singleHandler = new PdfBoxTypeHandler();
        singleHandler.configure(singleProps);

        byte[] singleBytes = renderXml(singleHandler, buildSampleDocXml("Single Style", "Tester", "Single", "en", false));
        try (PDDocument doc = Loader.loadPDF(singleBytes)) {
            Set<String> fonts = collectEmbeddedFontNames(doc);
            Assertions.assertFalse(fonts.isEmpty());
            Assertions.assertTrue(fonts.stream().anyMatch(n -> n.contains("TitilliumWeb-Regular")),
                    "Expected fallback to TitilliumWeb-Regular in " + fonts);
        }
    }

    private static void copyResourceToFile(String resourcePath, Path target) throws IOException {
        try (InputStream is = ClassHelper.loadFromDefaultClassLoader(resourcePath)) {
            Assertions.assertNotNull(is, "Missing test font resource: " + resourcePath);
            Files.write(target, StreamIO.readBytes(is));
        }
    }

    @Test
    void testPdfUA1AndWcag20ComplianceAssertions() throws Exception {
        FreemarkerDocProcessConfig fmConfig = FreemarkerDocProcessConfigFacade.loadConfigSafe(CONFIG_PATH);
        PdfBoxTypeHandler handler = (PdfBoxTypeHandler) fmConfig.getFacade().findHandler("pdf-pdfbox-pdf-ua");

        byte[] pdfBytes = renderXml(handler, buildSampleDocXml("Accessible Invoice", "Fugerit", "PDF/UA-1 Test", null, true));

        try (PDDocument doc = Loader.loadPDF(pdfBytes)) {
            // 1. PDF version >= 1.7 (ISO 14289-1)
            Assertions.assertTrue(doc.getVersion() >= 1.7f,
                    "PDF/UA-1 requires PDF version >= 1.7, found: " + doc.getVersion());

            // 2. Multiple pages, each with /Tabs /S and /StructParents >= 0
            Assertions.assertTrue(doc.getNumberOfPages() >= 2, "Expected at least 2 pages");
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                PDPage page = doc.getPage(i);
                Assertions.assertEquals("S", page.getCOSObject().getNameAsString(COSName.getPDFName("Tabs")),
                        "Page " + i + " must have /Tabs /S");
                Assertions.assertTrue(page.getStructParents() >= 0,
                        "Page " + i + " must have /StructParents >= 0");
            }

            PDDocumentCatalog catalog = doc.getDocumentCatalog();

            // 3. Language set from XML handler config ("it")
            Assertions.assertEquals("it", catalog.getLanguage());

            // 4. DisplayDocTitle == true
            PDViewerPreferences viewerPrefs = catalog.getViewerPreferences();
            Assertions.assertNotNull(viewerPrefs);
            Assertions.assertTrue(viewerPrefs.displayDocTitle(), "ViewerPreferences.DisplayDocTitle must be true");

            // 5. MarkInfo.isMarked() == true
            PDMarkInfo markInfo = catalog.getMarkInfo();
            Assertions.assertNotNull(markInfo);
            Assertions.assertTrue(markInfo.isMarked(), "MarkInfo.Marked must be true");

            // 6. StructureTreeRoot + ParentTree
            PDStructureTreeRoot structTreeRoot = catalog.getStructureTreeRoot();
            Assertions.assertNotNull(structTreeRoot);
            Assertions.assertNotNull(structTreeRoot.getK(), "StructureTreeRoot must have kids");
            Assertions.assertNotNull(structTreeRoot.getParentTree(), "StructureTreeRoot must have a ParentTree");

            // 7. XMP metadata with pdfuaid:part
            PDMetadata metadata = catalog.getMetadata();
            Assertions.assertNotNull(metadata);
            String xmp = new String(metadata.toByteArray(), StandardCharsets.UTF_8);
            Assertions.assertTrue(xmp.contains("http://www.aiim.org/pdfua/ns/id/"), "Missing PDF/UA namespace in XMP");
            Assertions.assertTrue(xmp.contains("pdfuaid:part"), "Missing pdfuaid:part in XMP");

            // 8. All fonts embedded across all 4 styles
            Set<String> embeddedFonts = collectEmbeddedFontNames(doc);
            assertAllFourTitilliumStylesPresent(embeddedFonts);

            // 9. Bookmarks / outline items all have non-null destinations (WCAG 2.0 PDF2)
            PDDocumentOutline outline = catalog.getDocumentOutline();
            Assertions.assertNotNull(outline);
            int bookmarkCount = 0;
            for (PDOutlineItem item : outline.children()) {
                bookmarkCount++;
                PDDestination dest = item.getDestination();
                Assertions.assertTrue(dest != null || item.getAction() != null,
                        "Bookmark '" + item.getTitle() + "' must have a valid destination or action");
            }
            Assertions.assertEquals(3, bookmarkCount, "Expected 3 bookmarks (including unresolved fallback)");
        }
    }

    @Test
    void testPdfBoxConfigCopyAndCustomFontKeyNormalization() {
        PdfBoxConfig original = new PdfBoxConfig();
        original.setPdfAEnabled(true);
        original.setPdfUAEnabled(true);
        original.setPdfALevel("2b");
        original.setDocumentTitle("Orig Title");
        original.setDocumentAuthor("Orig Author");
        original.setDocumentSubject("Orig Subject");
        original.setDocumentLanguage("es-ES");
        original.setFontDirectory("/tmp/fonts");
        original.setFontClasspath("font");
        original.setDefaultFontName("TitilliumWeb");
        original.registerCustomFont("TitilliumWeb", "font/TitilliumWeb-Regular.ttf");
        original.registerCustomFont("TitilliumWeb", "BOLD-ITALIC", "font/TitilliumWeb-BoldItalic.ttf");

        PdfBoxConfig copy = original.copy();
        copy.setDocumentTitle("Changed");
        copy.registerCustomFont("Other", "bold", "font/Other-Bold.ttf");

        Assertions.assertEquals("Orig Title", original.getDocumentTitle());
        Assertions.assertEquals("Changed", copy.getDocumentTitle());
        Assertions.assertEquals("font/TitilliumWeb-Regular.ttf",
                copy.getCustomFontPath("titilliumweb", PdfBoxConfig.FONT_STYLE_REGULAR));
        Assertions.assertEquals("font/TitilliumWeb-BoldItalic.ttf",
                copy.getCustomFontPath("TitilliumWeb", "bolditalic"));
        Assertions.assertNull(original.getCustomFontPath("Other", "bold"));
        Assertions.assertEquals(PdfBoxConfig.FONT_STYLE_ITALIC, PdfBoxConfig.normalizeFontStyleKey("Oblique"));
        Assertions.assertEquals(PdfBoxConfig.FONT_STYLE_REGULAR, PdfBoxConfig.normalizeFontStyleKey(null));
    }
}
