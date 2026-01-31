package org.fugerit.java.doc.mod.pdfbox;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fugerit.java.core.cfg.ConfigException;
import org.fugerit.java.doc.base.config.DocConfig;
import org.fugerit.java.doc.base.config.DocInput;
import org.fugerit.java.doc.base.config.DocOutput;
import org.fugerit.java.doc.base.config.DocTypeHandlerDefault;
import org.fugerit.java.doc.base.model.DocBase;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PdfBoxTypeHandler extends DocTypeHandlerDefault {

    private static final String MODULE = "pdfbox";

    private PdfBoxConfig config;

    public PdfBoxTypeHandler() {
        super(DocConfig.TYPE_PDF, MODULE);
        this.config = new PdfBoxConfig();
    }

    @Override
    public void configure(Properties config) throws ConfigException {
        super.configure(config);
        if (config != null) {
            this.config = PdfBoxConfig.fromProperties(config);
        }
    }

    @Override
    public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
        log.info("=== PdfBoxTypeHandler.handle() called ===");

        // Get the parsed document model
        DocBase docBase = docInput.getDoc();
        log.info("DocBase: {}", docBase != null ? docBase.getClass().getName() : "NULL");

        if (docBase == null) {
            throw new IllegalStateException("DocBase is null - document was not parsed");
        }

        // Debug: Check document structure
        log.info("DocBase body: {}", docBase.getDocBody() != null ? "present" : "null");
        if (docBase.getDocBody() != null) {
            log.info("Body element count: {}",
                    docBase.getDocBody().getElementList() != null ?
                            docBase.getDocBody().getElementList().size() : 0);
        }

        try (PDDocument document = new PDDocument()) {
            // Configure PDF/A or PDF/UA if requested
            if (config.isPdfAEnabled()) {
                log.info("Configuring PDF/A");
                configurePdfA(document);
            }
            if (config.isPdfUAEnabled()) {
                log.info("Configuring PDF/UA");
                configurePdfUA(document);
            }

            // Process document structure
            log.info("Creating renderer");
            PdfBoxDocumentRenderer renderer = new PdfBoxDocumentRenderer(document, config);

            log.info("Calling renderDocument");
            renderer.renderDocument(docBase);

            // Save document
            log.info("Saving PDF");
            if (config.isPdfAEnabled()) {
                // PDF/A-1b might need special save options
                document.save(docOutput.getOs());
            } else {
                document.save(docOutput.getOs());
            }

            log.info("PDF saved successfully");
        } catch (Exception e) {
            log.error("Error rendering PDF", e);
            throw e;
        }
    }

    private void configurePdfA(PDDocument document) throws IOException {
        PdfAConfigurator.configurePdfA1b(document, config);
    }

    private void configurePdfUA(PDDocument document) throws IOException {
        PdfUAConfigurator.configurePdfUA1(document, config);
    }
}