package org.fugerit.java.doc.mod.pdfbox;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.*;

import java.io.IOException;
import java.util.Properties;

public class PdfBoxTypeHandler extends DocTypeHandlerDefault {

    private static final String MODULE = "pdfbox";

    public PdfBoxTypeHandler() {
        super(DocConfig.TYPE_PDF, MODULE);
        this.config = new PdfBoxConfig();
    }

    private PdfBoxConfig config;

    @Override
    public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
        try (PDDocument document = new PDDocument()) {
            // Configure PDF/A or PDF/UA if requested
            if (config.isPdfAEnabled()) {
                configurePdfA(document);
            }
            if (config.isPdfUAEnabled()) {
                configurePdfUA(document);
            }

            // Process document structure
            PdfBoxDocumentRenderer renderer = new PdfBoxDocumentRenderer(document, config);
            renderer.renderDocument(docInput.getDoc());

            // Save with appropriate compression settings
            if (config.isPdfAEnabled()) {
                // PDF/A-1b requires no compression
                //document.save(docOutput.getOs(), org.apache.pdfbox.io.IOUtils.createScratchFile());
            } else {
                document.save(docOutput.getOs());
            }
        }
    }

    private void configurePdfA(PDDocument document) throws IOException {
        // PDF/A configuration will be implemented in PdfAConfigurator
        PdfAConfigurator.configurePdfA1b(document, config);
    }

    private void configurePdfUA(PDDocument document) throws IOException {
        // PDF/UA configuration will be implemented in PdfUAConfigurator
        PdfUAConfigurator.configurePdfUA1(document, config);
    }

}