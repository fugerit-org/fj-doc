package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fugerit.java.core.lang.helpers.ClassHelper;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;

@Slf4j
class TestRealisticInvoicePdfBox {

    private static final String REALISTIC_INVOICE = "realistic_invoice";
    private static final File OUTPUT_FILE = new File("target", REALISTIC_INVOICE + "-pdfbox.pdf");

    @Test
    void testPdfBoxRealisticInvoice() throws Exception {
        PdfBoxTypeHandler handler = new PdfBoxTypeHandler();
        String inputXml = "xml/" + REALISTIC_INVOICE + ".xml";
        try (InputStreamReader reader = new InputStreamReader(ClassHelper.loadFromDefaultClassLoader(inputXml));
             OutputStream os = Files.newOutputStream(OUTPUT_FILE.toPath())) {
            handler.handle(DocInput.newInput(handler.getType(), reader), DocOutput.newOutput(os));
        }
        Assertions.assertTrue(OUTPUT_FILE.exists());
        Assertions.assertTrue(OUTPUT_FILE.length() > 0);
        log.info("Rendered PDFBox invoice to {}, size: {} bytes", OUTPUT_FILE.getAbsolutePath(), OUTPUT_FILE.length());
        try (PDDocument pdfDoc = Loader.loadPDF(OUTPUT_FILE)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(pdfDoc);
            Assertions.assertTrue(pdfText.contains("INVOICE #INV-2026-0685"));
            Assertions.assertTrue(pdfText.contains("Acme Cloud Integration Ltd."));
            Assertions.assertTrue(pdfText.contains("Enterprise Corp S.p.A."));
            Assertions.assertTrue(pdfText.contains("11,150.00"));
        }
    }
}
