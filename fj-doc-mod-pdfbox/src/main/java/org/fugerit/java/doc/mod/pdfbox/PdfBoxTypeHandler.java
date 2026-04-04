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

    private transient PdfBoxConfig config;

    public PdfBoxTypeHandler() {
        super(DocConfig.TYPE_PDF, MODULE);
        this.config = new PdfBoxConfig();
    }

    @Override
    public void configure(Properties config) throws ConfigException {
        if (config != null) {
            this.config = PdfBoxConfig.fromProperties(config);
        }
    }

    /**
     * Enriches the base config with metadata from the document itself.
     * Document metadata (doc-title, doc-author, doc-subject, doc-language) takes
     * priority over the default values in the config, but configured values set
     * explicitly via {@link #configure(Properties)} are preserved when no document
     * metadata is present.
     */
    private PdfBoxConfig enrichConfigFromDocBase(DocBase docBase, PdfBoxConfig baseConfig) {
        PdfBoxConfig enriched = new PdfBoxConfig();
        enriched.setPdfAEnabled(baseConfig.isPdfAEnabled());
        enriched.setPdfUAEnabled(baseConfig.isPdfUAEnabled());
        enriched.setPdfALevel(baseConfig.getPdfALevel());
        enriched.setFontDirectory(baseConfig.getFontDirectory());

        String title = docBase.getInfoDocTitle();
        enriched.setDocumentTitle(title != null ? title : baseConfig.getDocumentTitle());

        String author = docBase.getInfoDocAuthor();
        enriched.setDocumentAuthor(author != null ? author : baseConfig.getDocumentAuthor());

        String subject = docBase.getInfoDocSubject();
        enriched.setDocumentSubject(subject != null ? subject : baseConfig.getDocumentSubject());

        String language = docBase.getInfoDocLanguage();
        enriched.setDocumentLanguage(language != null ? language : baseConfig.getDocumentLanguage());

        return enriched;
    }

    @Override
    public void handle(DocInput docInput, DocOutput docOutput) throws Exception {
        log.debug("PdfBoxTypeHandler.handle() called");

        DocBase docBase = docInput.getDoc();

        if (docBase == null) {
            throw new IllegalStateException("DocBase is null - document was not parsed");
        }

        // Enrich the config with metadata declared inside the document
        docBase.getStableInfoSafe();
        PdfBoxConfig effectiveConfig = enrichConfigFromDocBase(docBase, config);

        try (PDDocument document = new PDDocument()) {
            if (effectiveConfig.isPdfAEnabled()) {
                log.debug("Configuring PDF/A");
                PdfAConfigurator.configurePdfA1b(document, effectiveConfig);
            }
            if (effectiveConfig.isPdfUAEnabled()) {
                log.debug("Configuring PDF/UA");
                PdfUAConfigurator.configurePdfUA1(document, effectiveConfig);
            }

            PdfBoxDocumentRenderer renderer = new PdfBoxDocumentRenderer(document, effectiveConfig);
            renderer.renderDocument(docBase);

            document.save(docOutput.getOs());
            log.debug("PDF saved successfully");
        } catch (Exception e) {
            log.error("Error rendering PDF", e);
            throw e;
        }
    }
}