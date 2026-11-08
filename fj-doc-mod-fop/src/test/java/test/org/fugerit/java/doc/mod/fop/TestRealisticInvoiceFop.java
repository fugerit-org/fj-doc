package test.org.fugerit.java.doc.mod.fop;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fugerit.java.core.function.SafeFunction;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandler;
import org.fugerit.java.doc.mod.fop.FreeMarkerFopTypeHandler;
import org.fugerit.java.doc.mod.fop.InitFopHandler;
import org.fugerit.java.doc.mod.fop.PdfFopTypeHandler;
import org.fugerit.java.doc.mod.fop.config.FopConfigClassLoaderWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.org.fugerit.java.BasicTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
class TestRealisticInvoiceFop extends BasicTest {

    private static final String REALISTIC_INVOICE = "realistic_invoice";
    private static final File OUTPUT_FILE = new File("target", REALISTIC_INVOICE + "-fop.pdf");
    private static final File FO_FILE = new File("target", REALISTIC_INVOICE + "-fop.fo");

    @BeforeAll
    static void init() {
        SafeFunction.apply(InitFopHandler::initDocAsync);
    }

    private DocTypeHandler createAccessibleHandler() {
        FopConfigClassLoaderWrapper fopConfig = new FopConfigClassLoaderWrapper("fop-config-realistic-invoice.xml");
        PdfFopTypeHandler handler = new PdfFopTypeHandler(StandardCharsets.UTF_8, fopConfig, true, false);
        handler.setPdfUAMode(PdfFopTypeHandler.ATT_PDF_UA_MODE_PDF_UA_1);
        return handler;
    }

    @Test
    void testRealisticInvoiceFop() throws Exception {
        String inputXml = "xml/" + REALISTIC_INVOICE + ".xml";

        DocTypeHandler foHandler = FreeMarkerFopTypeHandler.HANDLER;
        try (InputStreamReader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader(inputXml));
             FileOutputStream fos = new FileOutputStream(FO_FILE)) {
            foHandler.handle(DocInput.newInput(foHandler.getType(), reader), DocOutput.newOutput(fos));
        }

        DocTypeHandler handler = this.createAccessibleHandler();
        try (InputStreamReader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader(inputXml));
             FileOutputStream fos = new FileOutputStream(OUTPUT_FILE)) {
            handler.handle(DocInput.newInput(handler.getType(), reader), DocOutput.newOutput(fos));
        }
        Assertions.assertTrue(OUTPUT_FILE.exists());
        Assertions.assertTrue(OUTPUT_FILE.length() > 0);
        log.info("Rendered FOP invoice to {}, size: {} bytes", OUTPUT_FILE.getAbsolutePath(), OUTPUT_FILE.length());
        try (PDDocument pdfDoc = PDDocument.load(OUTPUT_FILE)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(pdfDoc);
            Assertions.assertTrue(pdfText.contains("INVOICE #INV-2026-0685"));
            Assertions.assertTrue(pdfText.contains("Acme Cloud Integration Ltd."));
            Assertions.assertTrue(pdfText.contains("Enterprise Corp S.p.A."));
            Assertions.assertTrue(pdfText.contains("11,150.00"));
            Assertions.assertNotNull(pdfDoc.getDocumentCatalog().getMarkInfo());
            Assertions.assertTrue(pdfDoc.getDocumentCatalog().getMarkInfo().isMarked());
            Assertions.assertNotNull(pdfDoc.getDocumentCatalog().getStructureTreeRoot());
        }
    }
}
